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
                    "/api/v1/auth/refresh/token",
                    "/api/v1/auth/login",
                    "/api/v1/auth/register",
                    "/api/v1/auth/verify-otp",
                    "/api/v1/auth/send-otp"
            )
    );

    public static boolean allowedAuth(String path) {
if(!path.startsWith("/api/")){
    System.out.println("Auth Passed : true");
    return  true;
}
        for (String string : allowedEndpoints) {
            if (path.equals(string)) {
                return true;
            }
        }
        System.out.println("Auth Failed : true");
        return false;
    }


}
