package com.byaffe.learningking.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("/v1/certifications")
public class ApiCertificationsService {

//    @GET
//    @Path("/")
//    @Produces("application/json")
//    @Consumes("application/json")
//    public Response getCertifications(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
//        JSONObject result = new JSONObject();
//
//        try {
//            JSONArray certifications = new JSONArray();
//
//            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
//            Search search = CertificationServiceImpl.generateSearchTermsForCertifications(queryParamModel.getSearchTerm(), null)
//                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
//                    .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);
//            if (queryParamModel.getCategoryId() != null) {
//                search.addFilterEqual("category.id", queryParamModel.getCategoryId());
//            }
//
//            if (queryParamModel.getSortBy() != null) {
//                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
//            }
//
//            for (Certification certification : ApplicationContextProvider.getBean(CertificationService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
//                certifications.put(new JSONObject(certification)
//                        .put("id", certification.getId())
//                        .put("coverImageUrl", certification.getCoverImageUrl())
//                        .put("title", certification.getTitle())
//                        .put("description", certification.getDescription())
//                );
//            }
//            result.put("certifications", certifications);
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
//            return ApiUtils.buidResponse(200, result);
//        } catch (Exception e) {
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
//            return ApiUtils.buidResponse(500, result);
//        }
//
//    }
//
//    @GET
//    @Path("/by-academies")
//    @Produces("application/json")
//    @Consumes("application/json")
//    public Response getCertificationsByCategories(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
//        JSONObject result = new JSONObject();
//
//        try {
//            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
//
//            JSONArray certificationsArray = new JSONArray();
//            JSONArray data = new JSONArray();
//            SortField sortField = new SortField("dateCreated", "dateCreated", true);
//
//            for (CourseAcademyType devTopic : CourseAcademyType.values()) {
//                List<Certification> certifications = ApplicationContextProvider.getBean(CertificationService.class).getInstances(new Search()
//                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
//                        .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
//                        .addFilterEqual("academy", devTopic), queryParamModel.getOffset(), queryParamModel.getLimit());
//
//                for (Certification certification : certifications) {
//
//                    certificationsArray.put(new JSONObject()
//                            .put("id", certification.getId())
//                            .put("academy", certification.getAcademy())
//                            .put("title", certification.getTitle())
//                            .put("coverImageUrl", certification.getCoverImageUrl())
//                            .put("description", certification.getDescription())
//                            .put("duration", 1)
//                    );
//
//                }
//                if (certificationsArray.length() < 1) {
//                    continue;
//                }
//                data.put(new JSONObject()
//                        .put("id", devTopic.ordinal())
//                        .put("name", devTopic.getDisplayName())
//                        .put("certifications", certificationsArray));
//                certificationsArray = new JSONArray();
//
//            }
//
//            result.put("records", data);
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
//            return ApiUtils.buidResponse(200, result);
//        } catch (JSONException e) {
//
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
//            return ApiUtils.buidResponse(500, result);
//
//        }
//
//    }
//
//    @GET
//    @Path("/{id}")
//    @Produces("application/json")
//    @Consumes("application/json")
//    public Response getCertificationDetails(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
//        JSONObject result = new JSONObject();
//        Student member = (Student) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
//        if (member == null) {
//            return ApiUtils.composeFailureMessage("User not found", 401);
//        } else {
//            try {
//                JSONObject certificationObj = new JSONObject();
//                JSONObject subscriptionObject = new JSONObject();
//                JSONArray lessonsArray = new JSONArray();
//
//                Certification certification = ApplicationContextProvider.getBean(CertificationService.class).getInstanceByID(id);
//                if (certification == null) {
//                    return ApiUtils.composeFailureMessage("Certification Not Found", ApiConstants.BAD_REQUEST_CODE);
//                }
//
//                List<CertificationCourse> lessons = ApplicationContextProvider.getBean(CertificationService.class
//                ).getCertificationCourses(certification);
//
//                for (CertificationCourse lesson : lessons) {
//                    JSONObject jSONObject = new JSONObject(lesson);
//                    jSONObject.put("id", lesson.getId());
//                     jSONObject.put("courseId", lesson.getCourse().getId());
//                    lessonsArray.put(jSONObject);
//                }
//
//                certificationObj = new JSONObject(certification);
//                certificationObj.put("id", certification.getId());
//
//                CertificationSubscription subscription = ApplicationContextProvider.getBean(CertificationSubscriptionService.class).getSubscription(member, certification);
//                if (subscription != null) {
//                    subscriptionObject = new JSONObject();
//                    subscriptionObject.put("id", subscription.getId())
//                            .put("completedCourses", subscription.getCompletedCourses())
//                            .put("readStatus", subscription.getReadStatus());
//                }
//
//                result.put("subscription", subscriptionObject);
//                result.put("certification", certificationObj);
//                result.put("courses", lessonsArray);
//                result.put("numberOfCourses", lessons.size());
//                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
//                result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
//
//            } catch (Exception e) {
//                result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
//                result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
//            }
//
//        }
//        return ApiUtils.buidResponse(200, result);
//    }
//
//    @POST
//    @Path("/enroll/{id}")
//    @Produces("application/json")
//    @Consumes("application/json")
//    public Response enrollForCertification(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
//        JSONObject result = new JSONObject();
//        Student member = (Student) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
//
//        if (member == null) {
//            return ApiUtils.composeFailureMessage("User not found", 401);
//        }
//        try {
//            CertificationService courseService = ApplicationContextProvider.getBean(CertificationService.class);
//            Certification courseSerie = courseService.getInstanceByID(id);
//            if (courseSerie == null) {
//                return ApiUtils.composeFailureMessage("Certification Not Found", ApiConstants.BAD_REQUEST_CODE);
//            }
//
//            CertificationSubscription certificationSubscription = ApplicationContextProvider.getBean(CertificationSubscriptionService.class).createSubscription(member, courseSerie);
//
//            JSONObject subscriptionObj = new JSONObject();
//            subscriptionObj.put("id", certificationSubscription.getId())
//                    .put("completedCourses", certificationSubscription.getCompletedCourses())
//                    .put("readStatus", certificationSubscription.getReadStatus());
//
//            result.put("subscription", subscriptionObj);
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
//            return ApiUtils.buidResponse(200, result);
//
//        } catch (JSONException | ValidationFailedException e) {
//            return ApiUtils.composeFailureMessage(e.getMessage());
//        }
//
//    }
//
//    @GET
//    @Path("/mycertifications")
//    @Produces("application/json")
//    @Consumes("application/json")
//    public Response getStudentCertification(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
//        JSONObject result = new JSONObject();
//        JSONArray courses = new JSONArray();
//        Student member = (Student) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
//        try {
//            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
//            Search search = CourseSubscriptionServiceImpl.generateSearchTermsForSubscriptions(queryParamModel.getSearchTerm(), member, null, null)
//                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
//
//            if (queryParamModel.getSortBy() != null) {
//                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
//            }
//
//            List<CertificationSubscription> subscriptions = ApplicationContextProvider.getBean(CertificationSubscriptionService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit());
//            for (CertificationSubscription subscription : subscriptions) {
//                Certification certification = subscription.getCertification();
//                courses.put(new JSONObject(certification)
//                        .put("id", certification.getId())
//                        .put("academy", certification.getAcademy())
//                        .put("coverImageUrl", certification.getCoverImageUrl())
//                        .put("title", certification.getTitle())
//                        .put("description", certification.getDescription())
//                        .put("subscription", new JSONObject(certification)
//                                .put("id", subscription.getId())
//                                .put("completedCourses", subscription.getCompletedCourses())
//                                .put("readStatus", subscription.getReadStatus())
//                        ));
//
//            }
//            result.put("courses", courses);
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
//            return ApiUtils.buidResponse(200, result);
//        } catch (Exception e) {
//
//            return ApiUtils.composeFailureMessage(e.getMessage());
//        }
//
//    }

}
