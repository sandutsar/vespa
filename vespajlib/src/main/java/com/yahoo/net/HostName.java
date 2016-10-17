// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities for getting the hostname of the system running the JVM.
 *
 * @author lulf
 * @author bratseth
 * @author hakon
 */
public class HostName {

    private static final Logger logger = Logger.getLogger(HostName.class.getName());

    private static String cachedHostName = null;

    /**
     * Return a fully qualified hostname that resolves to an IP address on a network interface.
     * Normally this is the same as the 'hostname' command, but if that's not reachable a hostname
     * for a reachable IP address is returned which is reachable , but on dev machines on WiFi,
     * that IP isn't configured so we prefer a WiFi network interface IP address which is both reachable and
     * has a DNS entry.
     *
     * @return the preferred name of localhost
     * @throws RuntimeException if accessing the network or the 'hostname' command fails
     */
    public static synchronized String getLocalhost() {
        if (cachedHostName == null) {
            try {
                cachedHostName = getPreferredHostName();
            } catch (Exception e) {
                throw new RuntimeException("Failed to find a preferred hostname", e);
            }
        }
        return cachedHostName;
    }

    private static String getPreferredHostName() throws Exception {
        // Prefer the system hostname
        String systemHostName = getSystemHostName();
        if (isReachable(systemHostName)) {
            return systemHostName;
        }

        // Try to find an IP address that resolves in DNS, starting with IPv4 addresses as an optimization.

        List<InetAddress> reachableNonLocalIp4Addresses = new ArrayList<>();
        List<InetAddress> reachableNonLocalIp6Addresses = new ArrayList<>();
        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            for (InetAddress ipAddress : Collections.list(networkInterface.getInetAddresses())) {
                if (!isReachable(ipAddress)) {
                    continue;
                }

                if (ipAddress instanceof Inet4Address) {
                    reachableNonLocalIp4Addresses.add(ipAddress);
                } else {
                    reachableNonLocalIp6Addresses.add(ipAddress);
                }
            }
        }

        Optional<String> reachableHostName = getAnyHostNameInDns(reachableNonLocalIp4Addresses);
        if (reachableHostName.isPresent()) {
            return reachableHostName.get();
        }

        reachableHostName = getAnyHostNameInDns(reachableNonLocalIp6Addresses);
        if (reachableHostName.isPresent()) {
            return reachableHostName.get();
        }

        // Use textual representation of IP address since we failed to find a canonical DNS name above.

        if (!reachableNonLocalIp4Addresses.isEmpty()) {
            return reachableNonLocalIp4Addresses.get(0).getHostName();
        } else if (!reachableNonLocalIp6Addresses.isEmpty()) {
            return reachableNonLocalIp6Addresses.get(0).getHostName();
        }

        // Fall back to localhost.

        return InetAddress.getLocalHost().getCanonicalHostName();
    }

    private static Optional<String> getAnyHostNameInDns(List<InetAddress> ipAddresses) {
        for (InetAddress ipAddress : ipAddresses) {
            // Caveat: This call typically takes seconds on a Mac, and with 5 or so addresses
            // it is important to avoid calling this too often. That's why we do it here.
            // We should actually have called this first, then gotten the hostname's InetAddress
            // address (which may not match ipAddress), and used it for the above reachability test.
            String hostname = ipAddress.getCanonicalHostName();
            if (Objects.equals(hostname, ipAddress.getHostAddress())) {
                // getCanonicalHostName() failed to get the fully qualified domain name
                continue;
            }

            return Optional.of(hostname);
        }

        return Optional.empty();
    }

    // public for testing purposes (all testing machines should have a hostname
    public static String getSystemHostName() throws Exception {
        Process process = Runtime.getRuntime().exec("hostname");
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String hostname = in.readLine();
        process.waitFor();
        if (process.exitValue() != 0) {
            throw new RuntimeException("Command 'hostname' failed with exit code " + process.exitValue());
        }

        return hostname;
    }

    private static Optional<String> getHostNameIfReachable(InetAddress ipAddress) {
        if (!isReachable(ipAddress)) {
            return Optional.empty();
        }

        // Caveat: This call typically takes seconds on a Mac, and with 5 or so addresses
        // it is important to avoid calling this too often. That's why we do it here.
        // We should actually have called this first, then gotten the hostname's InetAddress
        // address (which may not match ipAddress), and used it for the above reachability test.
        String hostname = ipAddress.getCanonicalHostName();
        if (Objects.equals(hostname, ipAddress.getHostAddress())) {
            // getCanonicalHostName() failed to get the fully qualified domain name
            return Optional.empty();
        }

        return Optional.of(hostname);
    }

    private static boolean isReachable(String hostname) {
        try {
            InetAddress ipAddress = InetAddress.getByName(hostname);
            return isReachable(ipAddress);
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private static boolean isReachable(InetAddress ipAddress) {
        try {
            // ping says ~50ms on my Fedora Lenovo, but that seems a lot for pinging oneself  - hakon
            int timeoutMs = 100;
            if ( ! ipAddress.isReachable(timeoutMs)) {
                // The network interface may be down, ignore address
                logger.log(Level.INFO, ipAddress.toString() +
                        " is unreachable w/" + timeoutMs + "ms timeout, ignoring address");
                return false;
            }

            return true;
        } catch (IOException e) {
            // Why would this be different from !isReachable ?
            logger.log(Level.INFO, "Failed testing reachability of " + ipAddress + ", ignoring address", e);
            return false;
        }
    }

}
