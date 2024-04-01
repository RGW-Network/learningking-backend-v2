package com.byaffe.learningking.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author User
 */
public class FilterUtils {

    private static String v2EndPoint = "/v2";
    private static Set<String> allowedEndpoints = new HashSet<>(
            Arrays.asList("/api/health",
                    "/api/v1/status",
                    "/h2-console",
                    "/swagger-ui/index.html",
                    "/swagger-ui/swagger-ui.css",
                    "/swagger-ui/swagger-ui-bundle.js",
                    "/swagger-ui/swagger-ui-standalone-preset.js",
                    "/swagger-ui/favicon-32x32.png",
                    "/swagger-ui/favicon-16x16.png",

                    "/favicon.ico",
                    "/swagger-ui",
                    "/api-docs",
                    "/api/v1/auth/refresh/token",
                    "/api/v1/auth/login",
                    "/api/v1/auth/register",
                    "/api/v1/auth/verify-otp",
                    "/api/v1/auth/send-otp",
                    "/static/index.html",
                    "/"
            )
    );

    public static boolean allowedAuth(String path) {

        for (String string : allowedEndpoints) {
            if (path.startsWith(string)) {
                return true;
            }
        }
        return false;
    }
}
