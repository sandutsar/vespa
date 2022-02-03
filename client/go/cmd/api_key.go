// Copyright Yahoo. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
// vespa api-key command
// Author: mpolden
package cmd

import (
	"fmt"
	"io/ioutil"
	"log"

	"github.com/spf13/cobra"
	"github.com/vespa-engine/vespa/client/go/util"
	"github.com/vespa-engine/vespa/client/go/vespa"
)

var overwriteKey bool

const apiKeyLongDoc = `Create a new user API key for authentication with Vespa Cloud.

The API key will be stored in the Vespa CLI home directory
(see 'vespa help config'). Other commands will then automatically load the API
key as necessary.

It's possible to override the API key used through environment variables. This
can be useful in continuous integration systems.

* VESPA_CLI_API_KEY containing the key directly:

  export VESPA_CLI_API_KEY="my api key"

* VESPA_CLI_API_KEY_FILE containing path to the key:

  export VESPA_CLI_API_KEY_FILE=/path/to/api-key

Note that when overriding API key through environment variables, that key will
always be used. It's not possible to specify a tenant-specific key.`

func init() {
	apiKeyCmd.Flags().BoolVarP(&overwriteKey, "force", "f", false, "Force overwrite of existing API key")
	apiKeyCmd.MarkPersistentFlagRequired(applicationFlag)
}

func apiKeyExample() string {
	if vespa.Auth0AccessTokenEnabled() {
		return "$ vespa auth api-key -a my-tenant.my-app.my-instance"
	} else {
		return "$ vespa api-key -a my-tenant.my-app.my-instance"
	}
}

var apiKeyCmd = &cobra.Command{
	Use:               "api-key",
	Short:             "Create a new user API key for authentication with Vespa Cloud",
	Long:              apiKeyLongDoc,
	Example:           apiKeyExample(),
	DisableAutoGenTag: true,
	Args:              cobra.ExactArgs(0),
	Run:               doApiKey,
}

var deprecatedApiKeyCmd = &cobra.Command{
	Use:               "api-key",
	Short:             "Create a new user API key for authentication with Vespa Cloud",
	Long:              apiKeyLongDoc,
	Example:           apiKeyExample(),
	DisableAutoGenTag: true,
	Args:              cobra.ExactArgs(0),
	Hidden:            true,
	Deprecated:        "use 'vespa auth api-key' instead",
	Run:               doApiKey,
}

func doApiKey(_ *cobra.Command, _ []string) {
	cfg, err := LoadConfig()
	if err != nil {
		fatalErr(err, "Could not load config")
		return
	}
	app := getApplication()
	apiKeyFile := cfg.APIKeyPath(app.Tenant)
	if util.PathExists(apiKeyFile) && !overwriteKey {
		printErrHint(fmt.Errorf("File %s already exists", apiKeyFile), "Use -f to overwrite it")
		printPublicKey(apiKeyFile, app.Tenant)
		return
	}
	apiKey, err := vespa.CreateAPIKey()
	if err != nil {
		fatalErr(err, "Could not create API key")
		return
	}
	if err := ioutil.WriteFile(apiKeyFile, apiKey, 0600); err == nil {
		printSuccess("API private key written to ", apiKeyFile)
		printPublicKey(apiKeyFile, app.Tenant)
		if vespa.Auth0AccessTokenEnabled() {
			if err == nil {
				if err := cfg.Set(cloudAuthFlag, "api-key"); err != nil {
					fatalErr(err, "Could not write config")
				}
				if err := cfg.Write(); err != nil {
					fatalErr(err)
				}
			}
		}
	} else {
		fatalErr(err, "Failed to write ", apiKeyFile)
	}
}

func printPublicKey(apiKeyFile, tenant string) {
	pemKeyData, err := ioutil.ReadFile(apiKeyFile)
	if err != nil {
		fatalErr(err, "Failed to read ", apiKeyFile)
		return
	}
	key, err := vespa.ECPrivateKeyFrom(pemKeyData)
	if err != nil {
		fatalErr(err, "Failed to load key")
		return
	}
	pemPublicKey, err := vespa.PEMPublicKeyFrom(key)
	if err != nil {
		fatalErr(err, "Failed to extract public key")
		return
	}
	fingerprint, err := vespa.FingerprintMD5(pemPublicKey)
	if err != nil {
		fatalErr(err, "Failed to extract fingerprint")
	}
	log.Printf("\nThis is your public key:\n%s", color.Green(pemPublicKey))
	log.Printf("Its fingerprint is:\n%s\n", color.Cyan(fingerprint))
	log.Print("\nTo use this key in Vespa Cloud click 'Add custom key' at")
	log.Printf(color.Cyan("%s/tenant/%s/keys").String(), getConsoleURL(), tenant)
	log.Print("and paste the entire public key including the BEGIN and END lines.")
}
