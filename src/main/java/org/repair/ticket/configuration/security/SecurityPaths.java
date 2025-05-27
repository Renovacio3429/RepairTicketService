package org.repair.ticket.configuration.security;


public final class SecurityPaths {
    public static final String API_AUTH = "/api/auth/login";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String OPENAPI_DOC = "/openapi.yaml";
    public static final String API_DOCS = "/v3/api-docs/**";

    private SecurityPaths() {
        // utility class, no instances
    }
}
