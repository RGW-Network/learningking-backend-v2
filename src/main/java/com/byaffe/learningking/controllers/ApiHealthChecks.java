package com.byaffe.learningking.controllers;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import javax.ws.rs.GET;

@Path("/health/check")
public class ApiHealthChecks {

    private static final Logger LOGGER = Logger.getLogger(ApiHealthChecks.class.getName());

    @GET
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    public Response isAppAlive() {
        try {

            return ApiUtils.composeSuccessMessage("App is Running");

        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            return ApiUtils.composeServerFailure(e.getMessage());
        }
    }

}
