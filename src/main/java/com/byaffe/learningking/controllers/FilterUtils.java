package com.byaffe.learningking.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author User
 */
public class FilterUtils {

    private static String v2EndPoint = "/v1";
    private static Set<String> allowedEndpoints = new HashSet<>(
            Arrays.asList("/v1/auth",
                    "v1/user/requestPasswordReset",
                    "v1/user/verifyAccount",
                    "v1/user/verifyResetCode",
                    "health/check",
                    "v1/user/resetPassword")
    );

    public static boolean allowedAuth(String path) {
        
        for (String string : allowedEndpoints) {
            if (path.contains(string)) {
                return true;
            }
        }
        return false;
    }
}
