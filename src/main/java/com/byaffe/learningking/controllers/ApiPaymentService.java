package com.byaffe.learningking.controllers;

import com.googlecode.genericdao.search.Search;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import org.byaffe.systems.api.models.ApiPaymentModel;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.byaffe.systems.core.services.CourseService;
import org.byaffe.systems.core.services.CourseSubscriptionService;
import org.byaffe.systems.core.services.MemberSubscriptionPlanService;
import org.byaffe.systems.core.services.PaymentService;
import org.byaffe.systems.core.services.SubscriptionPlanPaymentService;
import org.byaffe.systems.core.services.SubscriptionPlanService;
import org.byaffe.systems.core.services.flutterwave.FlutterReponse;
import org.byaffe.systems.core.utilities.AppUtils;
import org.byaffe.systems.models.Member;
import org.byaffe.systems.models.courses.Course;
import org.byaffe.systems.models.courses.CourseSubscription;
import org.byaffe.systems.models.courses.PublicationStatus;
import org.byaffe.systems.models.payments.CoursePayment;
import org.byaffe.systems.models.payments.MemberSubscriptionPlan;
import org.byaffe.systems.models.payments.SubscriptionPlan;
import org.byaffe.systems.models.payments.SubscriptionPlanPayment;
import org.byaffe.systems.models.payments.SubscriptionPlanStatus;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

@Path("/v1/payments")
public class ApiPaymentService {

    SortField sortField = new SortField("name", "name", false);

    @POST
    @Path("/course/initiate")
    @Produces("application/json")
    @Consumes("application/json")
    public Response initiateCoursePayment(@Context HttpServletRequest request, ApiPaymentModel apiPaymentModel) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not");
            }
            
             if (apiPaymentModel == null||StringUtils.isBlank(apiPaymentModel.getCourseId())) {
                return ApiUtils.composeFailureMessage("Missing Course ID");
            }
            Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(apiPaymentModel.getCourseId());
            if (course == null) {
                return ApiUtils.composeFailureMessage("Course not found");
            }

            CoursePayment payment = ApplicationContextProvider.getBean(PaymentService.class).initiatePayment(course, member); //Logger.getLogger("APiPaymentsService",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+new Gson().toJson(flutterReponse));
            //compose response
            result.put("payment", new JSONObject()
                    .put("link", payment.getLastRavePaymentLink())
                    .put("transactionId", payment.getTransactionId())
            );

            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "CoursePayment initiated");

            return Response.status(200).entity("" + result).build();
        } catch (Exception ex) {
            Logger.getLogger(ApiPaymentService.class.getName()).log(Level.SEVERE, null, ex);
            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }

    @POST
    @Path("/course/pay-by-subscription")
    @Produces("application/json")
    @Consumes("application/json")
    public Response payForCourseBySubscription(@Context HttpServletRequest request, ApiPaymentModel apiPaymentModel) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not");
            }
            if (apiPaymentModel == null) {
                return ApiUtils.composeFailureMessage("No data specified");
            }
            if (StringUtils.isBlank(apiPaymentModel.getMemberSubscriptionId())) {
                return ApiUtils.composeFailureMessage("memberSubscriptionId id is missing");
            }

            if (StringUtils.isBlank(apiPaymentModel.getCourseId())) {
                return ApiUtils.composeFailureMessage("courseId is missing");
            }
            Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(apiPaymentModel.getCourseId());
            if (course == null) {
                return ApiUtils.composeFailureMessage("Course not found");
            }

            MemberSubscriptionPlan memberSubscriptionPlan = ApplicationContextProvider.getBean(MemberSubscriptionPlanService.class).getInstanceByID(apiPaymentModel.getMemberSubscriptionId());
            if (memberSubscriptionPlan == null || !memberSubscriptionPlan.getStatus().equals(SubscriptionPlanStatus.ACTIVE)) {
                return ApiUtils.composeFailureMessage("Member Subscription not found");
            }

            if (!memberSubscriptionPlan.getStatus().equals(SubscriptionPlanStatus.ACTIVE)) {
                return ApiUtils.composeFailureMessage("Member Subscription expired/depleted");
            }

            CourseSubscription courseSubscription = ApplicationContextProvider.getBean(MemberSubscriptionPlanService.class).payBySubscription(course, memberSubscriptionPlan);
            //compose response
            result.put("courseSubscription", new JSONObject(courseSubscription)
                    .put("id", courseSubscription.getId())
                    .put("readStatus", courseSubscription.getReadStatus())
            );

            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "Subscription Successfull");

            return Response.status(200).entity("" + result).build();
        } catch (Exception ex) {
            Logger.getLogger(ApiPaymentService.class.getName()).log(Level.SEVERE, null, ex);
            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }

    @POST
    @Path("/subscriptions/initiate")
    @Produces("application/json")
    @Consumes("application/json")
    public Response initiateSubscriptionsPayment(@Context HttpServletRequest request, ApiPaymentModel apiPaymentModel) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not");
            }

            if (StringUtils.isBlank(apiPaymentModel.getPlanId())) {
                return ApiUtils.composeFailureMessage("Missing plan Id");
            }
            SubscriptionPlan subscriptionPlan = ApplicationContextProvider.getBean(SubscriptionPlanService.class).getInstanceByID(apiPaymentModel.getPlanId());
            if (subscriptionPlan == null) {
                return ApiUtils.composeFailureMessage("Subscription Plan not found");
            }

            SubscriptionPlanPayment payment = ApplicationContextProvider.getBean(SubscriptionPlanPaymentService.class).initiatePayment(subscriptionPlan, member); //Logger.getLogger("APiPaymentsService",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+new Gson().toJson(flutterReponse));
            //compose response
            result.put("payment", new JSONObject()
                    .put("link", payment.getLastRavePaymentLink())
                    .put("transactionId", payment.getTransactionId())
            );

            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, "CoursePayment initiated");

            return Response.status(200).entity("" + result).build();
        } catch (Exception ex) {
            Logger.getLogger(ApiPaymentService.class.getName()).log(Level.SEVERE, null, ex);
            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }

    @GET
    @Path("/subscriptions/plans")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getPaymentPlans(@Context HttpServletRequest request) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not");
            }
            List<SubscriptionPlan> plans = ApplicationContextProvider.getBean(SubscriptionPlanService.class).getInstances(
                    new Search()
                            .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                            .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE),
                    0, 0);

            JSONArray plansArray = new JSONArray();
            for (SubscriptionPlan plan : plans) {
                plansArray.put(new JSONObject()
                        .put("id", plan.getId())
                        .put("name", plan.getName())
                        .put("cost", String.format("%,f", plan.getCost()).replaceAll("0*$", ""))
                        .put("maximumNumberOfCourses", plan.getMaximumNumberOfCourses())
                        .put("allowedAcademyType", plan.getAllowedAcademyType())
                        .put("description", plan.getDescription())
                        .put("durationInMonths", plan.getDurationInMonths())
                        .put("contentRestrictionType", plan.getContentRestrictionType().getDisplayName()));

            }
            result.put("subcriptionPlans", plansArray);

            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_TOKEN);

            return Response.status(200).entity("" + result).build();
        } catch (Exception ex) {
            Logger.getLogger(ApiPaymentService.class.getName()).log(Level.SEVERE, null, ex);
            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }

    @GET
    @Path("/subscriptions/my-plans")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getMyPaymentPlans(@Context HttpServletRequest request) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not");
            }
            List<MemberSubscriptionPlan> plans = ApplicationContextProvider.getBean(MemberSubscriptionPlanService.class).getInstances(
                    new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                            .addFilterEqual("status", SubscriptionPlanStatus.ACTIVE),
                    0, 0);

            JSONArray plansArray = new JSONArray();
            for (MemberSubscriptionPlan plan : plans) {
                plansArray.put(new JSONObject()
                        .put("id", plan.getId())
                        .put("cost", String.valueOf(plan.getCost()))
                        .put("activatedOn", plan.getActivatedOn())
                        .put("status", plan.getStatus().getDisplayName())
                        .put("durationInMonths", plan.getDurationInMonths())
                        .put("subscriptionPlan", new JSONObject()
                                .put("id", plan.getSubscriptionPlan().getId())
                                .put("name", plan.getSubscriptionPlan().getName())
                        )
                        .put("member", new JSONObject()
                                .put("id", plan.getMember().getId())
                        )
                );

            }
            result.put("mySubcriptionPlans", plansArray);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESS_MESSAGE);

            return Response.status(200).entity("" + result).build();
        } catch (Exception ex) {
            Logger.getLogger(ApiPaymentService.class.getName()).log(Level.SEVERE, null, ex);
            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }

    public static String[] getCodes(String fullString) {
        String withoutHash = fullString;
        // String withoutHash = fullString.replace("#", "");
        return withoutHash.split("\\*");
    }

}
