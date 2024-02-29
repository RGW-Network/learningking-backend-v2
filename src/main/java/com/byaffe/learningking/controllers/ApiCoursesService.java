package com.byaffe.learningking.controllers;

import org.byaffe.systems.api.models.BaseQueryParamModel;
import com.googlecode.genericdao.search.Search;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.byaffe.systems.api.models.CourseRatingDTO;
import org.byaffe.systems.core.services.CourseCategoryService;
import org.byaffe.systems.core.services.CourseLessonService;
import org.byaffe.systems.core.services.CourseRatingService;
import org.byaffe.systems.core.services.CourseService;
import org.byaffe.systems.core.services.CourseSubTopicService;
import org.byaffe.systems.core.services.CourseSubscriptionService;
import org.byaffe.systems.core.services.CourseTopicService;
import org.byaffe.systems.core.services.impl.CourseServiceImpl;
import org.byaffe.systems.core.services.impl.CourseSubscriptionServiceImpl;
import org.byaffe.systems.core.utilities.AppUtils;
import org.byaffe.systems.models.Member;
import org.byaffe.systems.models.courses.Course;
import org.byaffe.systems.models.courses.CourseAcademyType;
import org.byaffe.systems.models.courses.CourseCategory;
import org.byaffe.systems.models.courses.CourseLesson;
import org.byaffe.systems.models.courses.CourseRating;
import org.byaffe.systems.models.courses.CourseSubTopic;
import org.byaffe.systems.models.courses.CourseSubscription;
import org.byaffe.systems.models.courses.CourseTopic;
import org.byaffe.systems.models.courses.ExternalResource;
import org.byaffe.systems.models.courses.PublicationStatus;
import org.byaffe.systems.models.courses.Testimonial;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.byaffe.systems.api.constants.ApiUtils;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.model.RecordStatus;
com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import org.sers.webutils.server.core.utils.DateUtils;

/**
 *
 * @author Ray Gdhrt
 */
@Path("/v1/courses")
public class ApiCoursesService {

    @GET
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getCourses(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            JSONArray courses = new JSONArray();
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User not found", 401);
            }
            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
            Search search = CourseServiceImpl.genereateSearchObjectForCourses(queryParamModel.getSearchTerm(), null, null)
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);
            if (queryParamModel.getCategoryId() != null) {
                search.addFilterEqual("category.id", queryParamModel.getCategoryId());
            }
            if (queryParamModel.getAuthorId() != null) {
                search.addFilterEqual("instructor.id", queryParamModel.getAuthorId());
            }

            if (queryParamModel.getFeatured() != null) {
                search.addFilterEqual("isFeatured", queryParamModel.getFeatured());
            }

            if (queryParamModel.getSortBy() != null) {
                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
            }

            for (Course course : ApplicationContextProvider.getBean(CourseService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
                JSONArray testimonialArray = new JSONArray();
                for (Testimonial testimonial : course.getTestimonials()) {
                    testimonialArray.put(new JSONObject(testimonial));
                }

                int lessonsCount = ApplicationContextProvider.getBean(CourseLessonService.class)
                        .countInstances(new Search()
                                .addFilterEqual("course", course)
                                .addFilterEqual("recordStatus", RecordStatus.ACTIVE));
                double rattings = 0;

                try {
                    rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CourseSubscription subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, course);

                courses.put(new JSONObject(course)
                        .put("id", course.getId())
                        .put("category", new JSONObject()
                                .put("name", course.getCategory().getName())
                                .put("academy", course.getCategory().getAcademy())
                                .put("imageUrl", course.getCategory().getImageUrl())
                                .put("id", course.getCategory().getId())
                        )
                        .put("enrolled", subscription != null)
                        .put("testimonials", testimonialArray)
                        .put("coverImageUrl", course.getCoverImageUrl())
                        .put("title", course.getTitle())
                        .put("numberOfLessons", lessonsCount)
                        .put("averageRating", (rattings / 5))
                        .put("ratingsCount", ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course))
                        .put("description", course.getDescription())
                        .put("ownershipType", course.getOwnershipType().toString())
                        .put("publicationStatus", course.getPublicationStatus())
                        .put("whatYouWillLearn", course.getWhatYouWillLearn().toArray())
                );
            }
            result.put("courses", courses);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            return ApiUtils.buidResponse(500, result);
        }

    }

    @GET
    @Path("/by-categories")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getCoursesByCategories(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);

            if (member == null) {
                return ApiUtils.composeFailureMessage("User not found", 401);
            }
            JSONArray coursesArray = new JSONArray();
            JSONArray data = new JSONArray();
            SortField sortField = new SortField("dateCreated", "dateCreated", true);

            for (CourseCategory devTopic : ApplicationContextProvider.getBean(CourseCategoryService.class).getInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0)) {
                List<Course> courses = ApplicationContextProvider.getBean(CourseService.class).getInstances(new Search()
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                        .addFilterEqual("category", devTopic), queryParamModel.getOffset(), queryParamModel.getLimit());

                for (Course course : courses) {
                    int lessonsCount = ApplicationContextProvider.getBean(CourseLessonService.class)
                            .countInstances(new Search()
                                    .addFilterEqual("course", course)
                                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE));

                    double rattings = 0;

                    try {
                        rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CourseSubscription subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, course);

                    coursesArray.put(new JSONObject(course)
                            .put("id", course.getId())
                            .put("category", new JSONObject()
                                    .put("name", course.getCategory().getName())
                                    .put("academy", course.getCategory().getAcademy())
                                    .put("imageUrl", course.getCategory().getImageUrl())
                                    .put("id", course.getCategory().getId())
                            )
                            .put("averageRating", rattings / 5)
                            .put("ratingsCount", ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course))
                            .put("enrolled", subscription != null)
                            .put("title", course.getTitle())
                            .put("numberOfLessons", lessonsCount)
                            .put("coverImageUrl", course.getCoverImageUrl())
                            .put("description", course.getDescription())
                            .put("ownershipType", course.getOwnershipType().toString())
                            .put("publicationStatus", course.getPublicationStatus())
                            .put("whatYouWillLearn", course.getWhatYouWillLearn().toArray())
                    );

                }
                if (!courses.isEmpty()) {
                    data.put(new JSONObject()
                            .put("id", devTopic.getId())
                            .put("name", devTopic.getName())
                            .put("imageUrl", devTopic.getName())
                            .put("courses", coursesArray));
                    coursesArray = new JSONArray();
                }
            }

            result.put("topicData", data);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (JSONException e) {

            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            return ApiUtils.buidResponse(500, result);

        }

    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getCourseDetails(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        } else {
            try {
                JSONObject courseObj = new JSONObject();
                JSONObject subscriptionObject = new JSONObject();
                JSONArray lessonsArray = new JSONArray();
                CourseService courseService = ApplicationContextProvider.getBean(CourseService.class);
                Course course = courseService.getInstanceByID(id);
                if (course == null) {
                    return ApiUtils.composeFailureMessage("Course Not Found", ApiConstants.BAD_REQUEST_CODE);
                }

                List<CourseLesson> lessons = ApplicationContextProvider.getBean(CourseLessonService.class
                ).getInstances(new Search()
                        .addFilterEqual("course", course)
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0);
                CourseSubscription subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, course);

                for (CourseLesson lesson : lessons) {
                    JSONObject jSONObject = new JSONObject(lesson);
                    jSONObject.put("id", lesson.getId());
                    jSONObject.put("progress", ApplicationContextProvider.getBean(CourseLessonService.class
                    ).getProgress(subscription.getCurrentSubTopic()));
                    jSONObject.put("isPreview", lesson.getPosition() == 1);
                    lessonsArray.put(jSONObject);
                }

                courseObj = new JSONObject(course);
                JSONArray testimonialArray = new JSONArray();
                for (Testimonial testimonial : course.getTestimonials()) {
                    testimonialArray.put(new JSONObject(testimonial));
                }
                double rattings = 1;
                try {
                //    rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                courseObj.put("id", course.getId())
                        .put("category", new JSONObject()
                                .put("name", course.getCategory().getName())
                                .put("academy", course.getCategory().getAcademy())
                                .put("imageUrl", course.getCategory().getImageUrl())
                                .put("id", course.getCategory().getId())
                        )
                        .put("enrolled", subscription != null)
                        .put("averageRating", rattings / 5)
                        .put("progress", courseService.getProgress(subscription.getCurrentSubTopic()))
                   //     .put("ratingsCount", ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course))
                        .put("testimonials", testimonialArray)
                        .put("ownershipType", course.getOwnershipType().toString())
                        .put("publicationStatus", course.getPublicationStatus())
                        .put("whatYouWillLearn", course.getWhatYouWillLearn().toArray());

                if (subscription != null) {
                    subscriptionObject = new JSONObject(subscription);
                    subscriptionObject.put("id", subscription.getId());
                }

                result.put("subscription", subscriptionObject);
                result.put("course", courseObj);
                result.put("lessons", lessonsArray);
                result.put("numberOfLessons", lessons.size());
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
                return ApiUtils.buidResponse(200, result);
            } catch (Exception e) {
                return ApiUtils.composeFailureMessage(e.getMessage());
            }

        }

    }

    @GET
    @Path("/lessons/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getLessonById(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        } else {
            try {
                JSONObject lessonObj = new JSONObject();
                JSONArray topicsArray = new JSONArray();

                CourseLesson lesson = ApplicationContextProvider.getBean(CourseLessonService.class).getInstanceByID(id);
                if (lesson == null) {
                    return ApiUtils.composeFailureMessage("Lesson Not Found", ApiConstants.BAD_REQUEST_CODE);
                }

                CourseTopicService courseSubTopicService = ApplicationContextProvider.getBean(CourseTopicService.class
                );
                List<CourseTopic> topics = courseSubTopicService.getInstances(new Search()
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .addFilterEqual("courseLesson", lesson), 0, 0);
                CourseSubscription subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, lesson.getCourse());

                for (CourseTopic topic : topics) {
                    JSONObject topicJSONObject = new JSONObject(topic);
                    topicJSONObject.put("id", topic.getId());
                    topicJSONObject.put("progress", courseSubTopicService.getProgress(subscription.getCurrentSubTopic()));

                    topicsArray.put(topicJSONObject);
                }
                lessonObj = new JSONObject(lesson);
                lessonObj.put("id", lesson.getId());
                lessonObj.put("isPreview", lesson.getPosition() == 1);
                lessonObj.put("progress", ApplicationContextProvider.getBean(CourseLessonService.class).getProgress(subscription.getCurrentSubTopic()));

                result.put("lesson", lessonObj);
                result.put("topics", topicsArray);
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);

            } catch (JSONException e) {
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            }

        }
        return ApiUtils.buidResponse(200, result);
    }

    @GET
    @Path("/topic/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getTopicById(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        } else {
            try {
                JSONObject topicObj = new JSONObject();
                JSONArray subTopicsArray = new JSONArray();
                JSONArray resourcesArray = new JSONArray();

                CourseTopic topic = ApplicationContextProvider.getBean(CourseTopicService.class).getInstanceByID(id);
                if (topic == null) {
                    return ApiUtils.composeFailureMessage("Topic Not Found", ApiConstants.BAD_REQUEST_CODE);
                }
                List<CourseSubTopic> subTopics = ApplicationContextProvider.getBean(CourseSubTopicService.class
                ).getInstances(new Search()
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .addFilterEqual("courseTopic", topic), 0, 0);
                CourseSubscription subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, topic.getCourseLesson().getCourse());

                for (CourseSubTopic subTopic : subTopics) {
                    JSONObject jSONObject = new JSONObject(subTopic);
                    jSONObject.put("id", subTopic.getId());
                    subTopicsArray.put(jSONObject);
                }

                for (ExternalResource resource : topic.getExternalLinks()) {
                    JSONObject jSONObject = new JSONObject(resource);
                    jSONObject.put("id", resource.getId());
                    resourcesArray.put(jSONObject);
                }
                topicObj = new JSONObject(topic);
                topicObj.put("id", topic.getId());
                topicObj.put("progress", ApplicationContextProvider.getBean(CourseTopicService.class).getProgress(subscription.getCurrentSubTopic()));

                result.put("topic", topicObj);
                result.put("subTopics", subTopicsArray);
                result.put("externalResources", resourcesArray);
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);

            } catch (JSONException e) {
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            }

        }
        return ApiUtils.buidResponse(200, result);
    }

    @POST
    @Path("/enroll/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response enrollForCourse(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);

        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        }
        try {
            JSONObject subscriptionObj = new JSONObject();
            JSONObject courseObj = new JSONObject();

            CourseService courseService = ApplicationContextProvider.getBean(CourseService.class);
            Course courseSerie = courseService.getInstanceByID(id);
            if (courseSerie == null) {
                return ApiUtils.composeFailureMessage("Course Not Found", ApiConstants.BAD_REQUEST_CODE);
            }

            CourseSubscription courseSubscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).createSubscription(member, courseSerie);

            courseObj = new JSONObject(courseSerie);
            courseObj.put("id", courseSerie.getId());

            subscriptionObj = new JSONObject(courseSubscription);
            subscriptionObj.put("id", courseSubscription.getId());

            result.put("course", courseObj);
            result.put("subscription", subscriptionObj);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);

        } catch (JSONException | ValidationFailedException e) {
            return ApiUtils.composeFailureMessage(e.getMessage());
        }

    }

    @POST
    @Path("/rating")
    @Produces("application/json")
    @Consumes("application/json")
    public Response rateCourse(@Context HttpServletRequest request, CourseRatingDTO courseRatingDTO) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);

        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        }
        if (courseRatingDTO == null) {
            return ApiUtils.composeFailureMessage("No data specified");
        }
        if (StringUtils.isBlank(courseRatingDTO.getCourseId())) {
            return ApiUtils.composeFailureMessage("Missing course id");
        }

        if (courseRatingDTO.getStars() < 1) {
            return ApiUtils.composeFailureMessage("Invalid start count");
        }
        try {
            JSONObject subscriptionObj = new JSONObject();
            JSONObject courseObj = new JSONObject();

            CourseService courseService = ApplicationContextProvider.getBean(CourseService.class);
            Course course = courseService.getInstanceByID(courseRatingDTO.getCourseId());
            if (course == null) {
                return ApiUtils.composeFailureMessage("Course with id Not Found");
            }

            CourseRating courseRating = new CourseRating();
            courseRating.setCourse(course);
            courseRating.setMember(member);
            courseRating.setReviewText(courseRatingDTO.getRatingText());
            courseRating.setStarsCount(courseRatingDTO.getStars());
            courseRating = ApplicationContextProvider.getBean(CourseRatingService.class).saveInstance(courseRating);

            result.put("courseRating", new JSONObject(courseRating));
            result.put("subscription", subscriptionObj);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);

        } catch (Exception e) {
            return ApiUtils.composeFailureMessage(e.getMessage());
        }

    }

    @GET
    @Path("/rating/{courseId}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getCourseRatings(@Context HttpServletRequest request, @PathParam("courseId") String courseId) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);

        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        }

        if (StringUtils.isBlank(courseId)) {
            return ApiUtils.composeFailureMessage("Missing course id");
        }

        try {
            JSONArray ratingsJSONArray = new JSONArray();
            JSONObject courseObj = new JSONObject();
            CourseService courseService = ApplicationContextProvider.getBean(CourseService.class);
            Course course = courseService.getInstanceByID(courseId);
            if (course == null) {
                return ApiUtils.composeFailureMessage("Course with id Not Found");
            }

            List<CourseRating> courseRatings = ApplicationContextProvider.getBean(CourseRatingService.class)
                    .getInstances(new Search().
                            addFilterEqual("course", course).
                            addFilterEqual("recordStatus", RecordStatus.ACTIVE).
                            addFilterEqual("publicationStatus", PublicationStatus.ACTIVE),
                            0, 0);

            for (CourseRating courseRating : courseRatings) {
                ratingsJSONArray.put(new JSONObject()
                        .put("stars", courseRating.getStarsCount())
                        .put("dateCreated", ApiUtils.ENGLISH_DATE_FORMAT.format(courseRating.getDateCreated()))
                        .put("memberFullName", courseRating.getMember().composeFullName())
                        .put("ratingText", courseRating.getReviewText())
                );

            }

            result.put("ratings", ratingsJSONArray);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);

        } catch (Exception e) {
            return ApiUtils.composeFailureMessage(e.getMessage());
        }

    }

    @POST
    @Path("/subtopics/complete/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response completeSubTopic(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        } else {
            try {
                JSONObject memberPlanObj = new JSONObject();
                CourseSubTopic topic = ApplicationContextProvider.getBean(CourseSubTopicService.class).getInstanceByID(id);
                if (topic == null) {
                    return ApiUtils.composeFailureMessage("Topic  Not Found", ApiConstants.BAD_REQUEST_CODE);
                }
                CourseSubscription courseSubscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).completeSubTopic(member, topic);

                memberPlanObj = new JSONObject(courseSubscription);
                result.put("subscription", memberPlanObj);
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);

            } catch (JSONException | ValidationFailedException e) {
                return ApiUtils.composeFailureMessage(e.getMessage(), ApiConstants.BAD_REQUEST_CODE);

            }

        }
        return ApiUtils.buidResponse(200, result);
    }

    @GET
    @Path("/mycourses")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getMemberCourses(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();
        JSONArray courses = new JSONArray();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        try {
            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
            Search search = CourseSubscriptionServiceImpl.generateSearchTermsForSubscriptions(queryParamModel.getSearchTerm(), member, null, null)
                    .addFilterEqual("course.publicationStatus", PublicationStatus.ACTIVE);

            if (queryParamModel.getSortBy() != null) {
                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
            }

            List<CourseSubscription> subscriptions = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit());
            for (CourseSubscription subscription : subscriptions) {
                courses.put(new JSONObject(subscription));
                courses.put(new JSONObject(subscription.getCourse())
                        .put("id", subscription.getCourse().getId())
                        .put("category", new JSONObject()
                                .put("name", subscription.getCourse().getCategory().getName())
                                .put("academy", subscription.getCourse().getCategory().getAcademy())
                                .put("imageUrl", subscription.getCourse().getCategory().getImageUrl())
                                .put("id", subscription.getCourse().getCategory().getId())
                        )
                        .put("coverImageUrl", subscription.getCourse().getCoverImageUrl())
                        .put("title", subscription.getCourse().getTitle())
                        .put("duration", subscription.getCourse().getNumberOfTopics())
                        .put("description", subscription.getCourse().getDescription())
                        .put("ownershipType", subscription.getCourse().getOwnershipType().toString())
                        .put("publicationStatus", subscription.getCourse().getPublicationStatus())
                        .put("whatYouWillLearn", subscription.getCourse().getWhatYouWillLearn().toArray())
                );
            }
            result.put("courses", courses);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (Exception e) {

            return ApiUtils.composeFailureMessage(e.getMessage());
        }

    }

    @GET
    @Path("/categories")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getTopics(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
            Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
            if (StringUtils.isNotBlank(queryParamModel.getSortBy())) {
                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
            }

            if (StringUtils.isNotBlank(queryParamModel.getType())) {
                CourseAcademyType academyType = CourseAcademyType.valueOf(queryParamModel.getType());
                search.addFilterEqual("academy", academyType);
            }
            JSONArray topics = new JSONArray();
            for (CourseCategory topic : ApplicationContextProvider.getBean(CourseCategoryService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
                int count = ApplicationContextProvider.getBean(CourseService.class).countInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterEqual("category", topic));
                topics.put(
                        new JSONObject()
                                .put("id", topic.getId())
                                .put("academy", topic.getAcademy())
                                .put("name", topic.getName())
                                .put("colorCode", topic.getColorCode())
                                .put("imageUrl", topic.getImageUrl())
                                .put("coursesCount", count)
                );
            }
            //topics= ApiUtils.sortJsonArray(topics, "seriesCount",false);

            result.put("courseCategories", topics);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (JSONException e) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            return ApiUtils.buidResponse(500, result);
        }

    }

}
