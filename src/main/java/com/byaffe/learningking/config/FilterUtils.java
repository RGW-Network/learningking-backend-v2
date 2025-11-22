package com.byaffe.learningking.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author User
 */
public class FilterUtils {

    private static String v2EndPoint = "/v2";
    private static final Set<String> allowedEndpoints = new HashSet<>(
            Arrays.asList("/api/health",
                    "/api/v1/auth/login",
                    "/api/v1/auth/refresh/token",
                    "/api/v1/auth/student/register",
                    "/api/v1/auth/instructor/register",
                    "/api/v1/auth/student/send-otp",
                    "/api/v1/auth/reset-password/initiate",
                    "/api/v1/auth/reset-password",
                    "/api/v1/auth/instructor/send-otp",
                    "/api/v1/auth/student/verify-otp",
                    "/api/v1/auth/instructor/verify-otp",
                    "/api/v1/payments/subscription-plans",
                    "/api/v1/reviews",
"api/v1/admin/videos/webhook",
                    "/api/v1/status"
            )
    );

    public static boolean allowedAuth(String path) {
        if(!path.startsWith("/api/")){
            return true;
        }
        if ((path.startsWith("/api/v1/courses")&&!(path.contains("enroll")||path.contains("complete"))) ||
                path.startsWith("/api/v1/lessons")||
                path.startsWith("/api/v1/lookups") ||
                path.startsWith("/api/v1/categories") ||
                path.startsWith("/api/v1/articles")||
                path.startsWith("/api/v1/events")||
                path.startsWith("/api/v1/organisations/verify-email")||
                path.equals("/api/v1/user-submissions")||
                path.contains("download-certificate")) {
            return true;
        }
        for (String string : allowedEndpoints) {
            if (path.equals(string)) {
                return true;
            }
        }
        return false;
    }


}
