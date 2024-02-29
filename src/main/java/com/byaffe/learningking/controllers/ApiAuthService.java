package com.byaffe.learningking.controllers;

import org.byaffe.systems.api.models.ApiUserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import org.apache.commons.lang3.StringUtils;
import org.byaffe.systems.constants.AccountStatus;
import org.byaffe.systems.core.services.EmailTemplateService;
import org.byaffe.systems.core.services.MemberHeaderService;
import org.byaffe.systems.core.services.MemberService;
import org.byaffe.systems.core.services.SystemSettingService;
import org.byaffe.systems.models.Member;
import org.springframework.http.HttpStatus;

@Path("/v1/auth")
public class ApiAuthService {

    private static final Logger LOGGER = Logger.getLogger(ApiAuthService.class.getName());

    @POST
    @Path("/register")
    @Produces("application/json")
    @Consumes("application/json")
    public Response registerUser(ApiUserModel apiUserModel)
            throws JSONException, IOException {
        JSONObject result = new JSONObject();
     
        if (apiUserModel == null) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "No Data specified.");
        } else {

            if (StringUtils.isEmpty(apiUserModel.getEmail())) {
                return ApiUtils.composeFailureMessage("Email is missing.");
            }

            if (StringUtils.isEmpty(apiUserModel.getFirstName())) {
                return ApiUtils.composeFailureMessage("First name is missing.");
            }
              if (StringUtils.isEmpty(apiUserModel.getLastName())) {
                return ApiUtils.composeFailureMessage("Last name is missing.");
            }

            if (StringUtils.isEmpty(apiUserModel.getPassword() )) {
                return ApiUtils.composeFailureMessage("Password is missing.");
            }

            try {
                Member newMember = ApplicationContextProvider.getBean(MemberService.class).doRegister(apiUserModel.getFirstName(), apiUserModel.getLastName(), apiUserModel.getEmail(), apiUserModel.getPassword());

                JSONObject userObj = new JSONObject();
                userObj.put("username", newMember.getEmailAddress());
                userObj.put("emailAddress", newMember.getEmailAddress());
                userObj.put("firstName", newMember.getFirstName());
                userObj.put("lastName", newMember.getLastName());
                userObj.put("email", newMember.getEmailAddress());

                result.put("user", userObj);
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, "Check verification code sent to your email");
                return ApiUtils.buidResponse(200, result);

            } catch (Exception e) {
                return ApiUtils.composeFailureMessage(e.getMessage());
            }
        }
        return ApiUtils.buidResponse(ApiConstants.MALFORMED_REQUEST_CODE, result);
    }

    @POST
    @Path("/verifyAccount")
    @Produces("application/json")
    @Consumes("application/json")
    public Response verifyAccount(ApiUserModel apiSecurity) throws JSONException {
        JSONObject result = new JSONObject();

        if (apiSecurity == null) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "No Data specified.");
        } else {
            try {
              Member member=  ApplicationContextProvider.getBean(MemberHeaderService.class).activateMemberAccount(apiSecurity.getUsername(), apiSecurity.getCode());
                System.err.println("Member activated>>>"+new Gson().toJson(member.getAccountStatus()));
            
            } catch (Exception e) {
                return ApiUtils.composeFailureMessage(e.getLocalizedMessage());
            }
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "Account successfuly verified");
            return ApiUtils.buidResponse(200, result);
        }
        return ApiUtils.buidResponse(ApiConstants.MALFORMED_REQUEST_CODE, result);
    }

    @POST
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/json")
    public Response loginUser(@Context HttpServletRequest request, ApiSecurity apiSecurity) throws JSONException, IOException, Exception {
        JSONObject result = new JSONObject();

        if (apiSecurity == null) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "No Data specified.");
        } else {

            try {
                MemberService memberService = ApplicationContextProvider.getBean(MemberService.class);
                Member member = memberService.doLogin(apiSecurity.getUsername(), apiSecurity.getPassword());

                if (member == null || member.getAccountStatus().equals(AccountStatus.Blocked)) {
                    return ApiUtils.composeFailureMessage("Member acoount not found or blocked.");
                }

                Algorithm algorithm = Algorithm.HMAC256(HttpConstants.TOKEN_CIPHER.getBytes());
                Date dateNow = new Date();
                String access_token = JWT.create()
                        .withSubject(member.getEmailAddress())
                        .withExpiresAt(new Date(dateNow.getTime() + HttpConstants.ACCESS_TOKEN_DURATION))
                        .withIssuer(request.getRequestURL().toString())
                        // .withClaim("permissions", userAccount.findPermissions().stream().collect(Collectors.toList()))
                        .sign(algorithm);

                String refresh_token = JWT.create()
                        .withSubject(member.getEmailAddress())
                        .withExpiresAt(new Date(dateNow.getTime() + HttpConstants.REFRESH_TOKEN_DURATION))
                        .withIssuer(request.getRequestURL().toString())
                        //.withClaim("permissions", userAccount.findPermissions().stream().collect(Collectors.toList()))
                        .sign(algorithm);

                JSONObject userObj = new JSONObject(member);
                userObj.put("username", member.getUsername());
                userObj.put("firstName", member.getFirstName());
                userObj.put("lastName", member.getLastName());
                userObj.put("niceName", member.getNiceName());
                userObj.put("displayName", member.getDisplayName());
                userObj.put("email", member.getEmailAddress());
                userObj.put("imageUrl", member.getProfileImageUrl());
                result.put("refresh_token", refresh_token);
                result.put("access_token", access_token);
                result.put("learndash_token", access_token);

                if (member.getInterestNames() != null) {
                    result.put("interests", member.getInterestNames().toArray());
                }
                JSONObject settingsONObject = new JSONObject();

                result.put("settings", settingsONObject);
                result.put("user", userObj);
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, "User logged in succesfully");

                
                return ApiUtils.buidResponse(200, result);
            } catch (Exception e) {
                e.printStackTrace();
                return ApiUtils.composeFailureMessage(e.getMessage(), 403);
            }
        }
        return ApiUtils.buidResponse(ApiConstants.MALFORMED_REQUEST_CODE, result);
    }

    @GET
    @Path("/refreshToken")
    @Produces("application/json")
    @Consumes("application/json")
    public Response refreshToken(@Context HttpServletRequest request, @Context HttpServletResponse response) throws JSONException, IOException, Exception {
        JSONObject result = new JSONObject();

        String authorisationHeader = request.getHeader(HttpConstants.AUTHORIZATION);
        if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {

            try {
                String refresh_token = authorisationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(HttpConstants.TOKEN_CIPHER.getBytes());
                JWTVerifier jWTVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jWTVerifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Member member = ApplicationContextProvider.getBean(MemberService.class).getMemberByEmail(username);
                User userAccount = ApplicationContextProvider.getBean(UserService.class).getUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(member.getEmailAddress())
                        .withExpiresAt(new Date(new Date().getTime() + HttpConstants.ACCESS_TOKEN_DURATION))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("permissions", userAccount.findPermissions().stream().collect(Collectors.toList()))
                        .sign(algorithm);
                result.put("refresh_token", refresh_token);
                result.put("access_token", access_token);
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, "User logged in succesfully");

                return ApiUtils.buidResponse(200, result);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error refreshing token in: {0}", ex.getMessage());
                response.setHeader("error", ex.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.sendError(HttpStatus.FORBIDDEN.value());
            }
        }

        return ApiUtils.buidResponse(ApiConstants.MALFORMED_REQUEST_CODE, result);
    }

    @POST
    @Path("/getUserProfile")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getUserProfile(ApiSecurity apiSecurity) throws JSONException {
        JSONObject result = new JSONObject();

        if (apiSecurity == null) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "No Data specified.");
        } else {

            User userAccount = ApplicationContextProvider.getBean(UserService.class)
                    .getUserByUsername(apiSecurity.getUsername());

            if (userAccount == null) {
                return ApiUtils.composeFailureMessage("User with that username does not exist");
            }

            Member member = ApplicationContextProvider.getBean(MemberService.class).getMemberByUserAccount(userAccount);

            if (member == null || member.getAccountStatus() == AccountStatus.Active) {
                return ApiUtils.composeFailureMessage("User account is not active");
            }

            JSONObject memberObj = new JSONObject();
            memberObj.put("id", member.getId());
            memberObj.put("username", member.getUsername());
            memberObj.put("firstName", member.getFirstName());
            memberObj.put("lastName", member.getLastName());
            memberObj.put("email", member.getEmailAddress());
            memberObj.put("gender", member.getGender());
            memberObj.put("imageUrl", member.getProfileImageUrl());

            result.put("user", memberObj);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            return Response.status(200).entity("" + result).build();
        }
        return Response.status(ApiConstants.MALFORMED_REQUEST_CODE).entity("" + result).build();
    }
}
