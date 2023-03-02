package com.yahoo.metrics;

/**
 * @author gjoranv
 */
public enum ContainerMetrics implements VespaMetrics {

    HTTP_STATUS_1XX("http.status.1xx", Unit.RESPONSE, "Number of responses with a 1xx status"),
    HTTP_STATUS_2XX("http.status.2xx", Unit.RESPONSE, "Number of responses with a 2xx status"),
    HTTP_STATUS_3XX("http.status.3xx", Unit.RESPONSE, "Number of responses with a 3xx status"),
    HTTP_STATUS_4XX("http.status.4xx", Unit.RESPONSE, "Number of responses with a 4xx status"),
    HTTP_STATUS_5XX("http.status.5xx", Unit.RESPONSE, "Number of responses with a 5xx status"),

    APPLICATION_GENERATION("application_generation", Unit.VERSION, "The currently live application config generation (aka session id)"),

    JDISC_GC_COUNT("jdisc.gc.count", Unit.OPERATION, "Number of JVM garbage collections done"),
    JDISC_GC_MS("jdisc.gc.ms", Unit.MILLISECOND, "Time spent in JVM garbage collection"),
    JDISC_JVM("jdisc.jvm", Unit.VERSION, "JVM runtime version"),
    JDISC_MEMORY_MAPPINGS("jdisc.memory_mappings", Unit.OPERATION, "JDISC Memory mappings"),
    JDISC_OPEN_FILE_DESCRIPTORS("jdisc.open_file_descriptors", Unit.ITEM, "JDISC Open file descriptors"),

    JDISC_THREAD_POOL_UNHANDLED_EXCEPTIONS("jdisc.thread_pool.unhandled_exceptions", Unit.THREAD, "Number of exceptions thrown by tasks"),
    JDISC_THREAD_POOL_WORK_QUEUE_CAPACITY("jdisc.thread_pool.work_queue.capacity", Unit.THREAD, "Capacity of the task queue"),
    JDISC_THREAD_POOL_WORK_QUEUE_SIZE("jdisc.thread_pool.work_queue.size", Unit.THREAD, "Size of the task queue"),
    JDISC_THREAD_POOL_REJECTED_TASKS("jdisc.thread_pool.rejected_tasks", Unit.THREAD, "Number of tasks rejected by the thread pool"),
    JDISC_THREAD_POOL_SIZE("jdisc.thread_pool.size", Unit.THREAD, "Size of the thread pool"),
    JDISC_THREAD_POOL_MAX_ALLOWED_SIZE("jdisc.thread_pool.max_allowed_size", Unit.THREAD, "The maximum allowed number of threads in the pool"),
    JDISC_THREAD_POOL_ACTIVE_THREADS("jdisc.thread_pool.active_threads", Unit.THREAD, "Number of threads that are active"),
    
    JDISC_DEACTIVATED_CONTAINERS("jdisc.deactivated_containers.total", Unit.ITEM, "JDISC Deactivated container instances"),
    JDISC_DEACTIVATED_CONTAINERS_WITH_RETAINED_REFS("jdisc.deactivated_containers.with_retained_refs.last", Unit.ITEM, "JDISC Deactivated container nodes with retained refs"),
    JDISC_APPLICATION_FAILED_COMPONENT_GRAPHS("jdisc.application.failed_component_graphs", Unit.ITEM, "JDISC Application failed component graphs"),

    JDISC_SINGLETON_IS_ACTIVE("jdisc.singleton.is_active", Unit.ITEM, "JDISC Singleton is active"),
    JDISC_SINGLETON_ACTIVATION_COUNT("jdisc.singleton.activation.count", Unit.OPERATION, "JDISC Singleton activations"),
    JDISC_SINGLETON_ACTIVATION_FAILURE_COUNT("jdisc.singleton.activation.failure.count", Unit.OPERATION, "JDISC Singleton activation failures"),
    JDISC_SINGLETON_ACTIVATION_MILLIS("jdisc.singleton.activation.millis", Unit.MILLISECOND, "JDISC Singleton activation time"),
    JDISC_SINGLETON_DEACTIVATION_COUNT("jdisc.singleton.deactivation.count", Unit.OPERATION, "JDISC Singleton deactivations"),
    JDISC_SINGLETON_DEACTIVATION_FAILURE_COUNT("jdisc.singleton.deactivation.failure.count", Unit.OPERATION, "JDISC Singleton deactivation failures"),
    JDISC_SINGLETON_DEACTIVATION_MILLIS("jdisc.singleton.deactivation.millis", Unit.MILLISECOND, "JDISC Singleton deactivation time"),

    JDISC_HTTP_SSL_HANDSHAKE_FAILURE_MISSING_CLIENT_CERT("jdisc.http.ssl.handshake.failure.missing_client_cert", Unit.OPERATION, "JDISC HTTP SSL Handshake failures due to missing client certificate"),
    JDISC_HTTP_SSL_HANDSHAKE_FAILURE_EXPIRED_CLIENT_CERT("jdisc.http.ssl.handshake.failure.expired_client_cert", Unit.OPERATION, "JDISC HTTP SSL Handshake failures due to expired client certificate"),
    JDISC_HTTP_SSL_HANDSHAKE_FAILURE_INVALID_CLIENT_CERT("jdisc.http.ssl.handshake.failure.invalid_client_cert", Unit.OPERATION, "JDISC HTTP SSL Handshake failures due to invalid client certificate"),
    JDISC_HTTP_SSL_HANDSHAKE_FAILURE_INCOMPATIBLE_PROTOCOLS("jdisc.http.ssl.handshake.failure.incompatible_protocols", Unit.OPERATION, "JDISC HTTP SSL Handshake failures due to inincompatible protocols"),
    JDISC_HTTP_SSL_HANDSHAKE_FAILURE_INCOMPATIBLE_CHIFERS("jdisc.http.ssl.handshake.failure.incompatible_chifers", Unit.OPERATION, "JDISC HTTP SSL Handshake failures due to incompatible chifers"),
    JDISC_HTTP_SSL_HANDSHAKE_FAILURE_CONNECTION_CLOSED("jdisc.http.ssl.handshake.failure.connection_closed", Unit.OPERATION, "JDISC HTTP SSL Handshake failures due to connection closed"),
    JDISC_HTTP_SSL_HANDSHAKE_FAILURE_UNKNOWN("jdisc.http.ssl.handshake.failure.unknown", Unit.OPERATION, "JDISC HTTP SSL Handshake failures for unknown reason"),

    JDISC_HTTP_REQUEST_PREMATURELY_CLOSED("jdisc.http.request.prematurely_closed", Unit.REQUEST, "HTTP requests prematurely closed"),
    JDISC_HTTP_REQUEST_REQUESTS_PER_CONNECTION("jdisc.http.request.requests_per_connection", Unit.REQUEST, "HTTP requests per connection"),
    JDISC_HTTP_REQUEST_URI_LENGTH("jdisc.http.request.uri_length", Unit.BYTE, "HTTP URI length"),
    JDISC_HTTP_REQUEST_CONTENT_SIZE("jdisc.http.request.content_size", Unit.BYTE, "HTTP request content size"),
    JDISC_HTTP_REQUESTS("jdisc.http.requests", Unit.REQUEST, "HTTP requests"),
    JDISC_HTTP_REQUESTS_STATUS("jdisc.http.requests.status", Unit.REQUEST, "Number of requests to the built-in status handler"),

    JDISC_HTTP_FILTER_RULE_BLOCKED_REQUESTS("jdisc.http.filter.rule.blocked_requests", Unit.REQUEST, "Number of requests blocked by filter"),
    JDISC_HTTP_FILTER_RULE_ALLOWED_REQUESTS("jdisc.http.filter.rule.allowed_requests", Unit.REQUEST, "Number of requests allowed by filter"),
    JDISC_HTTP_FILTERING_REQUEST_HANDLED("jdisc.http.filtering.request.handled", Unit.REQUEST, "Number of filtering requests handled"),
    JDISC_HTTP_FILTERING_REQUEST_UNHANDLED("jdisc.http.filtering.request.unhandled", Unit.REQUEST, "Number of filtering requests unhandled"),
    JDISC_HTTP_FILTERING_RESPONSE_HANDLED("jdisc.http.filtering.response.handled", Unit.REQUEST, "Number of filtering responses handled"),
    JDISC_HTTP_FILTERING_RESPONSE_UNHANDLED("jdisc.http.filtering.response.unhandled", Unit.REQUEST, "Number of filtering responses unhandled"),
    JDISC_HTTP_HANDLER_UNHANDLED_EXCEPTIONS("jdisc.http.handler.unhandled_exceptions", Unit.REQUEST, "Number of unhandled exceptions in handler"),

    JDISC_TLS_CAPABILITY_CHECKS_SUCCEEDED("jdisc.tls.capability_checks.succeeded", Unit.OPERATION, "Number of TLS capability checks succeeded"),
    JDISC_TLS_CAPABILITY_CHECKS_FAILED("jdisc.tls.capability_checks.failed", Unit.OPERATION, "Number of TLS capability checks failed"),

    JETTY_THREADPOOL_MAX_THREADS("jdisc.http.jetty.threadpool.thread.max", Unit.THREAD, "Configured maximum number of threads"),
    JETTY_THREADPOOL_MIN_THREADS("jdisc.http.jetty.threadpool.thread.min", Unit.THREAD, "Configured minimum number of threads"),
    JETTY_THREADPOOL_RESERVED_THREADS("jdisc.http.jetty.threadpool.thread.reserved", Unit.THREAD, "Configured number of reserved threads or -1 for heuristic"),
    JETTY_THREADPOOL_BUSY_THREADS("jdisc.http.jetty.threadpool.thread.busy", Unit.THREAD, "Number of threads executing internal and transient jobs"),
    JETTY_THREADPOOL_IDLE_THREADS("jdisc.http.jetty.threadpool.thread.idle", Unit.THREAD, "Number of idle threads"),
    JETTY_THREADPOOL_TOTAL_THREADS("jdisc.http.jetty.threadpool.thread.total", Unit.THREAD, "Current number of threads"),
    JETTY_THREADPOOL_QUEUE_SIZE("jdisc.http.jetty.threadpool.queue.size", Unit.THREAD, "Current size of the job queue"),

    SERVER_NUM_OPEN_CONNECTIONS("serverNumOpenConnections", Unit.CONNECTION, "The number of currently open connections"),
    SERVER_NUM_CONNECTIONS("serverNumConnections", Unit.CONNECTION, "The total number of connections opened"),

    SERVER_BYTES_RECEIVED("serverBytesReceived", Unit.BYTE, "The number of bytes received by the server"),
    SERVER_BYTES_SENT("serverBytesSent", Unit.BYTE, "The number of bytes sent from the server"),

    HANDLED_REQUESTS("handled.requests", Unit.OPERATION, "The number of requests handled per metrics snapshot"),
    HANDLED_LATENCY("handled.latency", Unit.MILLISECOND, "The time used for requests during this metrics snapshot"),
    
    HTTPAPI_LATENCY("httpapi_latency", Unit.MILLISECOND, "Duration for requests to the HTTP document APIs"),
    HTTPAPI_PENDING("httpapi_pending", Unit.OPERATION, "Document operations pending execution"),
    HTTPAPI_NUM_OPERATIONS("httpapi_num_operations", Unit.OPERATION, "Total number of document operations performed"),
    HTTPAPI_NUM_UPDATES("httpapi_num_updates", Unit.OPERATION, "Document update operations performed"),
    HTTPAPI_NUM_REMOVES("httpapi_num_removes", Unit.OPERATION, "Document remove operations performed"),
    HTTPAPI_NUM_PUTS("httpapi_num_puts", Unit.OPERATION, "Document put operations performed"),
    HTTPAPI_SUCCEEDED("httpapi_succeeded", Unit.OPERATION, "Document operations that succeeded"),
    HTTPAPI_FAILED("httpapi_failed", Unit.OPERATION, "Document operations that failed"),
    HTTPAPI_PARSE_ERROR("httpapi_parse_error", Unit.OPERATION, "Document operations that failed due to document parse errors"),
    HTTPAPI_CONDITION_NOT_MET("httpapi_condition_not_met", Unit.OPERATION, "Document operations not applied due to condition not met"),
    HTTPAPI_NOT_FOUND("httpapi_not_found", Unit.OPERATION, "Document operations not applied due to document not found"),
    HTTPAPI_FAILED_UNKNOWN("httpapi_failed_unknown", Unit.OPERATION, "Document operations failed by unknown cause"),
    HTTPAPI_FAILED_TIMEOUT("httpapi_failed_timeout", Unit.OPERATION, "Document operations failed by timeout"),
    HTTPAPI_FAILED_INSUFFICIENT_STORAGE("httpapi_failed_insufficient_storage", Unit.OPERATION, "Document operations failed by insufficient storage"),

    MEM_HEAP_TOTAL("mem.heap.total", Unit.BYTE, "Total available heap memory"),
    MEM_HEAP_FREE("mem.heap.free", Unit.BYTE, "Free heap memory"),
    MEM_HEAP_USED("mem.heap.used", Unit.BYTE, "Currently used heap memory"),
    MEM_DIRECT_TOTAL("mem.direct.total", Unit.BYTE, "Total available direct memory"),
    MEM_DIRECT_FREE("mem.direct.free", Unit.BYTE, "Currently free direct memory"),
    MEM_DIRECT_USED("mem.direct.used", Unit.BYTE, "Direct memory currently used"),
    MEM_DIRECT_COUNT("mem.direct.count", Unit.BYTE, "Number of direct memory allocations"),
    MEM_NATIVE_TOTAL("mem.native.total", Unit.BYTE, "Total available native memory"),
    MEM_NATIVE_FREE("mem.native.free", Unit.BYTE, "Currently free native memory"),
    MEM_NATIVE_USED("mem.native.used", Unit.BYTE, "Native memory currently used"),    
    
    ATHENZ_TENANT_CERT_EXPIRY_SECONDS("athenz-tenant-cert.expiry.seconds", Unit.SECOND, "Time remaining until Athenz tenant certificate expires"),
    CONTAINER_IAM_ROLE_EXPIRY_SECONDS("container-iam-role.expiry.seconds", Unit.SECOND, "Time remaining until IAM role expires"),
    
    
    // SearchChain metrics
    PEAK_QPS("peak_qps", Unit.QUERY_PER_SECOND, "The highest number of qps for a second for this metrics shapshot"),
    SEARCH_CONNECTIONS("search_connections", Unit.CONNECTION, "Number of search connections"),
    FEED_OPERATIONS("feed.operations", Unit.OPERATION, "Number of document feed operations"),
    FEED_LATENCY("feed.latency", Unit.MILLISECOND, "Feed latency"),
    FEED_HTTP_REQUESTS("feed.http-requests", Unit.OPERATION, "Feed HTTP requests"),
    QUERIES("queries", Unit.OPERATION, "Query volume"),
    QUERY_CONTAINER_LATENCY("query_container_latency", Unit.MILLISECOND, "The query execution time consumed in the container"),
    QUERY_LATENCY("query_latency", Unit.MILLISECOND, "The overall query latency as seen by the container"),
    QUERY_TIMEOUT("query_timeout", Unit.MILLISECOND, "The amount of time allowed for query execytion, from the client"),
    FAILED_QUERIES("failed_queries", Unit.OPERATION, "The number of failed queries"),
    DEGRADED_QUERIES("degraded_queries", Unit.OPERATION, "The number of degraded queries, e.g. due to some conent nodes not responding in time"),
    HITS_PER_QUERY("hits_per_query", Unit.HIT_PER_QUERY, "The number of hits returned"),
    QUERY_HIT_OFFSET("query_hit_offset", Unit.HIT, "The offset for hits returned"),
    DOCUMENTS_COVERED("documents_covered", Unit.DOCUMENT, "The combined number of documents considered during query evaluation"),
    DOCUMENTS_TOTAL("documents_total", Unit.DOCUMENT, "The number of documents to be evaluated if all requests had been fully executed"),
    DOCUMENTS_TARGET_TOTAL("documents_target_total", Unit.DOCUMENT, "The target number of total documents to be evaluated when when all data is in sync"),
    JDISC_RENDER_LATENCY("jdisc.render.latency", Unit.NANOSECOND, "The time used by the container to render responses"),
    QUERY_ITEM_COUNT("query_item_count", Unit.ITEM, "The number of query items (terms, phrases, etc)"),
    DOCPROC_PROC_TIME("docproc.proctime", Unit.MILLISECOND, "Time spent processing document"),
    DOCPROC_DOCUMENTS("docproc.documents", Unit.DOCUMENT, "Number of processed documents"),
    
    TOTAL_HITS_PER_QUERY("totalhits_per_query", Unit.HIT_PER_QUERY, "The total number of documents found to match queries"),
    EMPTY_RESULTS("empty_results", Unit.OPERATION, "Number of queries matching no documents"),
    REQUESTS_OVER_QUOTA("requestsOverQuota", Unit.OPERATION, "The number of requests rejected due to exceeding quota"),
    
    RELEVANCE_AT_1("relevance.at_1", Unit.SCORE, "The relevance of hit number 1"),
    RELEVANCE_AT_3("relevance.at_3", Unit.SCORE, "The relevance of hit number 3"),
    RELEVANCE_AT_10("relevance.at_10", Unit.SCORE, "The relevance of hit number 10"),

    // Errors from search container
    ERROR_TIMEOUT("error.timeout", Unit.OPERATION, "Requests that timed out"),
    ERROR_BACKENDS_OOS("error.backends_oos", Unit.OPERATION, "Requests that failed due to no available backends nodes"),
    ERROR_PLUGIN_FAILURE("error.plugin_failure", Unit.OPERATION, "Requests that failed due to plugin failure"),
    ERROR_BACKEND_COMMUNICATION_ERROR("error.backend_communication_error", Unit.OPERATION, "Requests that failed due to backend communication error"),
    ERROR_EMPTY_DOCUMENT_SUMMARIES("error.empty_document_summaries", Unit.OPERATION, "Requests that failed due to missing document summaries"),
    ERROR_ILLEGAL_QUERY("error.illegal_query", Unit.OPERATION, "Requests that failed due to illegal queries"),
    ERROR_INVALID_QUERY_PARAMETER("error.invalid_query_parameter", Unit.OPERATION, "Requests that failed due to invalid query parameters"),
    ERROR_INTERNAL_SERVER_ERROR("error.internal_server_error", Unit.OPERATION, "Requests that failed due to internal server error"),
    ERROR_MISCONFIGURED_SERVER("error.misconfigured_server", Unit.OPERATION, "Requests that failed due to misconfigured server"),
    ERROR_INVALID_QUERY_TRANSFORMATION("error.invalid_query_transformation", Unit.OPERATION, "Requests that failed due to invalid query transformation"),
    ERROR_RESULTS_WITH_ERRORS("error.results_with_errors", Unit.OPERATION, "The number of queries with error payload"),
    ERROR_UNSPECIFIED("error.unspecified", Unit.OPERATION, "Requests that failed for an unspecified reason"),
    ERROR_UNHANDLED_EXCEPTION("error.unhandled_exception", Unit.OPERATION, "Requests that failed due to an unhandled exception"),

    // Deprecated metrics. TODO: Remove on Vespa 9.
    SERVER_REJECTED_REQUESTS("serverRejectedRequests", Unit.OPERATION, "Deprecated. Use jdisc.thread_pool.rejected_tasks instead."),
    SERVER_THREAD_POOL_SIZE("serverThreadPoolSize", Unit.THREAD, "Deprecated. Use jdisc.thread_pool.size instead."),
    SERVER_ACTIVE_THREADS("serverActiveThreads", Unit.THREAD, "Deprecated. Use jdisc.thread_pool.active_threads instead."),

    
    // Metrics from the cluster controller
    CLUSTER_CONTROLLER_DOWN_COUNT("cluster-controller.down.count", Unit.NODE, "Number of content nodes down"),
    CLUSTER_CONTROLLER_INITIALIZING_COUNT("cluster-controller.initializing.count", Unit.NODE, "Number of content nodes initializing"),
    CLUSTER_CONTROLLER_MAINTENANCE_COUNT("cluster-controller.maintenance.count", Unit.NODE, "Number of content nodes in maintenance"),
    CLUSTER_CONTROLLER_RETIRED_COUNT("cluster-controller.retired.count", Unit.NODE, "Number of content nodes that are retired"),
    CLUSTER_CONTROLLER_STOPPING_COUNT("cluster-controller.stopping.count", Unit.NODE, "Number of content nodes currently stopping"),
    CLUSTER_CONTROLLER_UP_COUNT("cluster-controller.up.count", Unit.NODE, "Number of content nodes up"),
    CLUSTER_CONTROLLER_CLUSTER_STATE_CHANGE_COUNT("cluster-controller.cluster-state-change.count", Unit.NODE, "Number of nodes changing state"),
    CLUSTER_CONTROLLER_BUSY_TICK_TIME_MS("cluster-controller.busy-tick-time-ms", Unit.MILLISECOND, "Time busy"),
    CLUSTER_CONTROLLER_IDLE_TICK_TIME_MS("cluster-controller.idle-tick-time-ms", Unit.MILLISECOND, "Time idle"),
    CLUSTER_CONTROLLER_WORK_MS("cluster-controller.work-ms", Unit.MILLISECOND, "Time used for actual work"),
    CLUSTER_CONTROLLER_IS_MASTER("cluster-controller.is-master", Unit.BINARY, "1 if this cluster controller is currently the master, or 0 if not"),
    CLUSTER_CONTROLLER_REMOTE_TASK_QUEUE_SIZE("cluster-controller.remote-task-queue.size", Unit.OPERATION, "Number of remote tasks queued"),
    // TODO(hakonhall): Update this name once persistent "count" metrics has been implemented.
    // DO NOT RELY ON THIS METRIC YET.
    CLUSTER_CONTROLLER_NODE_EVENT_COUNT("cluster-controller.node-event.count", Unit.OPERATION, "Number of node events"),
    CLUSTER_CONTROLLER_RESOURCE_USAGE_NODES_ABOVE_LIMIT("cluster-controller.resource_usage.nodes_above_limit", Unit.NODE, "The number of content nodes above resource limit, blocking feed"),
    CLUSTER_CONTROLLER_RESOURCE_USAGE_MAX_MEMORY_UTILIZATION("cluster-controller.resource_usage.max_memory_utilization", Unit.FRACTION, "Current memory utilisation, per content node"),
    CLUSTER_CONTROLLER_RESOURCE_USAGE_MAX_DISK_UTILIZATION("cluster-controller.resource_usage.max_disk_utilization", Unit.FRACTION, "Current disk space utilisation, per content node"),
    CLUSTER_CONTROLLER_RESOURCE_USAGE_MEMORY_LIMIT("cluster-controller.resource_usage.memory_limit", Unit.FRACTION, "Disk space limit as a fraction of available disk space"),
    CLUSTER_CONTROLLER_RESOURCE_USAGE_DISK_LIMIT("cluster-controller.resource_usage.disk_limit", Unit.FRACTION, "Memory space limit as a fraction of available memory"),
    CLUSTER_CONTROLLER_REINDEXING_PROGRESS("reindexing.progress", Unit.FRACTION, "Re-indexing progress");


    private final String name;
    private final Unit unit;
    private final String description;

    ContainerMetrics(String name, Unit unit, String description) {
        this.name = name;
        this.unit = unit;
        this.description = description;
    }

    public String baseName() {
        return name;
    }

    public Unit unit() {
        return unit;
    }

    public String description() {
        return description;
    }

}
