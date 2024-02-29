package com.byaffe.learningking.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.byaffe.systems.api.constants.ApiUtils;
com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import org.apache.commons.lang3.StringUtils;
import org.byaffe.systems.api.models.ApiUserModel;
import org.byaffe.systems.constants.AccountStatus;
import org.byaffe.systems.constants.TemplateType;
import org.byaffe.systems.core.services.EmailTemplateService;
import org.byaffe.systems.core.services.MemberService;
import org.byaffe.systems.core.services.SystemSettingService;
import org.byaffe.systems.core.utilities.AppUtils;
import org.byaffe.systems.core.utilities.EmailService;
import org.byaffe.systems.models.EmailTemplate;
import org.byaffe.systems.models.Member;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.server.core.security.service.impl.ApiAuthenticationHandler;
import org.sers.webutils.server.shared.CustomLogger;

@Path("/v1/member")
public class ApiMemberService {

    private static final Logger LOGGER = Logger.getLogger(ApiMemberService.class.getName());
   @POST
    @Path("/updateDeviceID")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateDeviceID(@Context HttpServletRequest request, ApiUserModel apiSecurity) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not exist");
            }
            
             if (apiSecurity == null) {
                return ApiUtils.composeFailureMessage("No data specified");
            }
            member.setDeviceId(apiSecurity.getDeviceId());
            Member savedMember = ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);

            System.err.println("Device ID>>>>>>>>>.."+apiSecurity.getDeviceId());
          
            result.put("deviceID", member.getDeviceId());
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            return Response.status(200).entity("" + result).build();
        } catch (Exception ex) {
            Logger.getLogger(ApiMemberService.class.getName()).log(Level.WARNING, null, ex);

            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }

    
     @GET
    @Path("/interests")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getInterests(@Context HttpServletRequest request) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not exist");
            }
            
            result.put("interests", member.getInterestNames());
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            return Response.status(200).entity("" + result).build();
        } catch (Exception ex) {
            Logger.getLogger(ApiMemberService.class.getName()).log(Level.WARNING, null, ex);

            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }
   
    
  

    
    @GET
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getUserProfile(@Context HttpServletRequest request) throws JSONException {
        
        try{
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (member == null) {
            return ApiUtils.composeFailureMessage("User with that username does not exist");
        }

        JSONObject memberObj = new JSONObject();
        memberObj.put("id", member.getId());
        memberObj.put("username", member.getUsername());
        memberObj.put("firstName", member.getFirstName());
        memberObj.put("lastName", member.getLastName());
        memberObj.put("fullName", member.composeFullName());
        memberObj.put("email", member.getEmailAddress());
        memberObj.put("country", member.getCountry());
        memberObj.put("gender", member.getGender());
        memberObj.put("phoneNumber", member.getPhoneNumber());

        JSONObject settingsObject = new JSONObject();
//        MemberSettings memberSettings = ApplicationContextProvider.getBean(MemberService.class).getMemberSettings(member);
//        if (memberSettings == null) {
//
//            memberSettings = new MemberSettings();
//            memberSettings.setMember(member);
//            memberSettings = ApplicationContextProvider.getBean(MemberService.class).save(memberSettings);
//        }
//        if (memberSettings != null) {
//            settingsObject.put("devotionTime", memberSettings.getDevotionTime())
//                    .put("appTheme", memberSettings.getAppTheme())
//                    .put("id", memberSettings.getId())
//                    .put("preferredCurrency", memberSettings.getPreferredCurrency())
//                    .put("preferredLanguage", memberSettings.getPrefferedLanguage())
//                    .put("devotionTime", memberSettings.getDevotionTimeInHours())
//                    .put("bookNotificationsOn", memberSettings.isBookNotificationsOn())
//                    .put("churchNotificationsOn", memberSettings.isChurchNotificationsOn())
//                    .put("bookNotificationsOn", memberSettings.isDevotionNotificationsOn())
//                    .put("prayerGroupNotificationsOn", memberSettings.isPrayerGroupNotificationsOn())
//                    .put("prayerWatchNotificationsOn", memberSettings.isPrayerWatchNotificationsOn())
//                    .put("nationalPrayerNotificationsOn", memberSettings.isNationalPrayerNotificationsOn())
//                    .put("seriesNotificationsOn", memberSettings.isSeriesNotificationsOn());
//        }

        memberObj.put("settings", settingsObject);
        result.put("user", memberObj);
        result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
        return Response.status(200).entity("" + result).build();
    
        }catch(Exception e){
         return ApiUtils.composeFailureMessage(e.getMessage()); 
        }
        }

    @POST
    @Path("/requestPasswordReset")
    @Produces("application/json")
    @Consumes("application/json")
    public Response requestPasswordReset(@Context HttpServletRequest request, ApiUserModel apiSecurity) throws JSONException, IOException, Exception {
        JSONObject result = new JSONObject();
        EmailTemplateService emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);
        SystemSettingService settingService = ApplicationContextProvider.getBean(SystemSettingService.class);
        if (StringUtils.isBlank(apiSecurity.getUsername())) {
            return ApiUtils.composeFailureMessage("Missing username");
        }

        Member member = ApplicationContextProvider.getBean(MemberService.class).getMemberByEmail(apiSecurity.getUsername());

        if (member == null || member.getAccountStatus() == null || !AccountStatus.Active.equals(member.getAccountStatus())) {
            return ApiUtils.composeFailureMessage("No active User account found for username");
        }
        member.setLastEmailVerificationCode(AppUtils.generateOTP(6));
        member = ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);
        if (settingService.getAppSetting()!= null && member.getLastEmailVerificationCode() != null) {
            EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByType(TemplateType.RESET_PASSWORD);

            if (emailTemplate != null) {
                String html = emailTemplate.getTemplate();
                CustomLogger.log("Html " + html);

                html = html.replace("{username}", member.getUsername());
                html = html.replace("{token}", member.getLastEmailVerificationCode());

                CustomLogger.log("Html " + html);

              
                    new EmailService().sendMail(member.getEmailAddress(), "Revival Gateway", html);
                
            }
            CustomLogger.log(ApiMemberService.class, CustomLogger.LogSeverity.LEVEL_DEBUG,
                    "Token " + member.getLastEmailVerificationCode());
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "Check your email for password reset code.");
            return Response.status(200).entity("" + result).build();
        }

        return Response.status(400).entity("" + result).build();
    }

    @POST
    @Path("/verifyResetCode")
    @Produces("application/json")
    @Consumes("application/json")
    public Response verifyResetCode(@Context HttpServletRequest request, ApiUserModel apiSecurity) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = ApplicationContextProvider.getBean(MemberService.class).getMemberByEmail(apiSecurity.getUsername());
        if (member == null) {
            return ApiUtils.composeFailureMessage("Authenticated user not found");
        }

        if (!member.getLastEmailVerificationCode().equals(apiSecurity.getApiToken())) {
            return ApiUtils.composeFailureMessage("Invalid verification code used");
        }

        result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
        result.put(ApiUtils.RESPONSE_PARAM, "Password Reset Code Verified");
        return ApiUtils.buidResponse(200, result);
    }

    @POST
    @Path("/resetPassword")
    @Produces("application/json")
    @Consumes("application/json")
    public Response resetPassword(@Context HttpServletRequest request, ApiUserModel apiSecurity) throws JSONException {
        JSONObject result = new JSONObject();
        if (apiSecurity == null || apiSecurity.getPassword() == null) {
            return ApiUtils.composeFailureMessage("Missing new password", 401);
        }
        Member member = ApplicationContextProvider.getBean(MemberService.class).getMemberByEmail(apiSecurity.getUsername());
        if (member == null) {
            return ApiUtils.composeFailureMessage("Authenticated user not found");
        }

        if (!member.getLastEmailVerificationCode().equals(apiSecurity.getApiToken())) {
            return ApiUtils.composeFailureMessage("Invalid verification code used");
        }
        try {
            member.setClearTextPassword(apiSecurity.getPassword());
            ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "Password is successfully updated");
            return Response.status(200).entity("" + result).build();
        } catch (Exception e) {
            e.printStackTrace();
             return ApiUtils.composeFailureMessage(e.getMessage());
        }
    }

    @POST
    @Path("/resetPasswordInternal")
    @Produces("application/json")
    @Consumes("application/json")
    public Response resetPasswordInternal(@Context HttpServletRequest request, ApiUserModel apiSecurity) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (member == null) {
            return ApiUtils.composeFailureMessage("Authenticated active member account not found", 401);
        }
        if (StringUtils.isBlank(apiSecurity.getOldPassword())) {
            return ApiUtils.composeFailureMessage("Authenticated active member account not found");
       
        }
        if (StringUtils.isBlank(apiSecurity.getNewPassword())) {
          return ApiUtils.composeFailureMessage("Authenticated active member account not found");
       
        }

        if (!StringUtils.equals(apiSecurity.getNewConfirmedPassword(), apiSecurity.getNewPassword())) {
            return ApiUtils.composeFailureMessage("Authenticated active member account not found");
       
        }

        User userAccount = ApplicationContextProvider.getBean(ApiAuthenticationHandler.class)
                .getWebPortalUser(member.getEmailAddress(), apiSecurity.getOldPassword());

        if (userAccount == null) {
            return ApiUtils.composeFailureMessage("Invalid Old password");
        }

        try {
            userAccount.setClearTextPassword(apiSecurity.getNewPassword());
            ApplicationContextProvider.getBean(UserService.class).saveUser(userAccount);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "Password is successfully updated");
            return Response.status(200).entity("" + result).build();
        } catch (ValidationFailedException e) {
            return Response.status(500).entity("" + e).build();
        }
    }

   
    @POST
    @Path("/updateUserProfile")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateUserProfile(@Context HttpServletRequest request, ApiUserModel apiUserModel) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (apiUserModel == null) {
            return ApiUtils.composeFailureMessage("No details supplied not found");
        }
        if (member == null) {
            return ApiUtils.composeFailureMessage("Authenticated user not found");
        }

        try {

            if (StringUtils.isNotBlank(apiUserModel.getImageBase64())) {

                String partSeparator = ",";
                String encodedImg = apiUserModel.getImageBase64();
                if (encodedImg.contains(partSeparator)) {
                    encodedImg = encodedImg.split(partSeparator)[1];

                }
                byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));

                String imageUrl = new AppUtils().uploadCloudinaryImage(decodedImg, "user/" + member.getId());
                member.setProfileImageUrl(imageUrl);

            }
            if (StringUtils.isNotBlank(apiUserModel.getFirstName())) {
                member.setFirstName(apiUserModel.getFirstName());
            }
            if (StringUtils.isNotBlank(apiUserModel.getLastName())) {
                member.setLastName(apiUserModel.getLastName());

            }

            if (StringUtils.isNotBlank(apiUserModel.getPhoneNumber())) {
                member.setPhoneNumber(apiUserModel.getPhoneNumber());
            }

            if (StringUtils.isNotBlank(apiUserModel.getCountryName())) {
               // member.setCountry(ApplicationContextProvider.getBean(MemberService.class).getCountryByName(apiUserModel.getCountryName()));
            }


            member = ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);
           
            member.setCountry(member.getCountry());
            member.setFirstName(member.getFirstName());
            member.setLastName(member.getLastName());

            ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);

            JSONObject memberObj = new JSONObject();
            memberObj.put("id", member.getId());
            memberObj.put("username", member.getUsername());
            memberObj.put("firstName", member.getFirstName());
            memberObj.put("lastName", member.getLastName());
            memberObj.put("fullName", member.composeFullName());
            memberObj.put("email", member.getEmailAddress());
            memberObj.put("country", member.getCountry());
            memberObj.put("imageUrl", member.getProfileImageUrl());
            memberObj.put("gender", member.getGender());
            memberObj.put("phoneNumber", member.getPhoneNumber());

            result.put("user", memberObj);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            return Response.status(200).entity("" + result).build();

        } catch (Exception e) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            e.printStackTrace();
            return Response.status(500).entity("" + result).build();
        }

    }

  
}
