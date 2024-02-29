package com.byaffe.learningking.controllers;

/**
 * This class is used to store the current request's bearer token. We have used the
 * InheritableThreadLocal variable. This enables the child threads created from
 * the main thread in our application to use the bearer token of the Parent Thread.
 *
 */
public class BearerTokenContext {

    private BearerTokenContext() {
        // Add a private constructor to hide the implicit public one.
    }

    private static ThreadLocal<String> bearerToken = new InheritableThreadLocal<>();

    public static String getBearerToken() {
        return bearerToken.get();
    }

    public static void setBearerToken(String id) {
        bearerToken.set(id);
    }

    public static void clear() {
        bearerToken.remove();
    }
}
