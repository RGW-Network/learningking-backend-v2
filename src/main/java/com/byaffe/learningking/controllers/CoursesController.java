package com.byaffe.learningking.controllers;

import com.byaffe.learningking.controllers.constants.ApiUtils;
import com.byaffe.learningking.dtos.articles.ArticlesFilterDTO;
import com.byaffe.learningking.dtos.courses.*;
import com.byaffe.learningking.dtos.instructor.*;
import com.byaffe.learningking.dtos.courses.CourseResponseDTO;
import com.byaffe.learningking.dtos.courses.CourseTopicResponseDTO;
import com.byaffe.learningking.dtos.courses.LessonResponseDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.services.impl.CourseServiceImpl;
import com.byaffe.learningking.services.impl.InstructorServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.UserDetailsContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
public class CoursesController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CourseSubscriptionService subscriptionService;
    @GetMapping("")
    public ResponseEntity<ResponseList<CourseResponseDTO>> getCourses(@Valid ArticlesFilterDTO queryParamModel) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(queryParamModel.getSearchTerm())
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
        List<CourseResponseDTO> courses = new ArrayList<>();
        for (Course course : ApplicationContextProvider.getBean(CourseService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {

            CourseResponseDTO dto = modelMapper.map(course, CourseResponseDTO.class);

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
            if (UserDetailsContext.getLoggedInStudent() != null) {
                CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(UserDetailsContext.getLoggedInStudent(), course);

                dto.setEnrolled(subscription != null);
            }
            dto.setNumberOfLessons(lessonsCount);
            dto.setAverageRating((rattings / 5));
            dto.setRatingsCount(ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course));

            courses.add(dto);
        }
        return ResponseEntity.ok().body(new ResponseList<>(courses, (int) 0, queryParamModel.getOffset(), queryParamModel.getLimit()));

    }


    @GetMapping("/by-categories")
    public ResponseEntity<ResponseList<ByTopicResponseDTO>> getCoursesByCategories(@RequestParam ArticlesFilterDTO queryParamModel) throws JSONException {
        List<ByTopicResponseDTO> records = new ArrayList<>();
        for (Category devTopic : ApplicationContextProvider.getBean(CategoryService.class).getInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0)) {
            List<Course> coursesModels = ApplicationContextProvider.getBean(CourseService.class).getInstances(new Search()
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                    .addFilterEqual("category", devTopic), queryParamModel.getOffset(), queryParamModel.getLimit());

            ByTopicResponseDTO courseByTopicResponseDTO = modelMapper.map(devTopic, ByTopicResponseDTO.class);
            List<CourseResponseDTO> dtos = new ArrayList<>();
            for (Course course : coursesModels) {
                CourseResponseDTO dto = (CourseResponseDTO) course;
                int lessonsCount = ApplicationContextProvider.getBean(CourseLessonService.class)
                        .countInstances(new Search().addFilterEqual("course", course).addFilterEqual("recordStatus", RecordStatus.ACTIVE));
                double rattings = 0;
                try {
                    rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(UserDetailsContext.getLoggedInStudent(), course);
                dto.setEnrolled(subscription != null);
                dto.setNumberOfLessons(lessonsCount);
                dto.setAverageRating((rattings / 5));
                dto.setRatingsCount(ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course));
                dtos.add(dto);
            }
            courseByTopicResponseDTO.setCourses(dtos);
            records.add(courseByTopicResponseDTO);
        }
        return ResponseEntity.ok().body(new ResponseList<>(records, (int) 0, queryParamModel.getOffset(), queryParamModel.getLimit()));

    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CourseDetailsResponseDTO>> getCourseDetails(@PathVariable("id") Long id) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        CourseService courseService = ApplicationContextProvider.getBean(CourseService.class);
        Course course = courseService.getInstanceByID(id);
        CourseDetailsResponseDTO responseDTO = new CourseDetailsResponseDTO();
        CourseResponseDTO courseObj = modelMapper.map(course, CourseResponseDTO.class);
        List<CourseLesson> lessons = ApplicationContextProvider.getBean(CourseLessonService.class).getInstances(new Search()
                .addFilterEqual("course", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortAsc("position"), 0, 0);
        CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, course);
        log.info("Lessons got: {}", lessons.stream().map(r -> r.id).toArray());
        for (CourseLesson lesson : lessons) {
            LessonResponseDTO lessonDto = modelMapper.map(lesson, LessonResponseDTO.class);
            if (subscription != null) {
                lessonDto.setProgress(ApplicationContextProvider.getBean(CourseLessonService.class).getProgress(subscription.getCurrentLecture()));
            }
            lessonDto.setIsPreview(lesson.getPosition() == 1);

            List<CourseTopic> topics = ApplicationContextProvider.getBean(CourseTopicService.class).getInstances(new Search()
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addFilterEqual("courseLesson", lesson)
                    .addSortAsc("position"), 0, 0);

            for (CourseTopic topic : topics) {
                CourseTopicResponseDTO topicJSONObject = modelMapper.map(topic, CourseTopicResponseDTO.class);
                List<CourseLecture> lectures = ApplicationContextProvider.getBean(CourseLectureService.class).getInstances(new Search()
                        .addFilterEqual("courseTopic", topic)
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .addSortAsc("position"), 0, 0);
                topicJSONObject.setLectures(lectures.stream().map(r -> modelMapper.map(r, LectureResponseDTO.class)).collect(Collectors.toList()));
                lessonDto.getTopics().add(topicJSONObject);
            }
            courseObj.getLessons().add(lessonDto);
        }

        double rattings = 1;
        try {
            rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
        } catch (Exception e) {
            e.printStackTrace();
        }

        courseObj.setEnrolled(subscription != null);
        courseObj.setAverageRating(rattings / 5);
        if (subscription != null) {
            courseObj.setEnrolled(true);
            courseObj.setProgress(subscription.getProgress());
        }
        log.info("Lessons got: {}", lessons.stream().map(r -> r.id).toArray());
        //     .put("ratingsCount", ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course))
        courseObj.setTestimonials(course.getTestimonials());
        //courseObj.setLessons(lessonsArray);
        courseObj.setNumberOfLessons(lessons.size());
        responseDTO.setCourse(courseObj);
        responseDTO.setSubscription(subscription);
        return ResponseEntity.ok().body(new ResponseObject<>(responseDTO));


    }


    @GetMapping("/lessons/{id}")
    public ResponseEntity<ResponseObject<LessonResponseDTO>> getLessons(@PathVariable("id") Long id) throws JSONException {
        LessonResponseDTO result = new LessonResponseDTO();
        Student member = new Student();

        CourseLesson lesson = ApplicationContextProvider.getBean(CourseLessonService.class).getInstanceByID(id);
        if (lesson == null) {
            throw new ValidationFailedException("Lesson not found");
        }
        result = modelMapper.map(lesson, LessonResponseDTO.class);
        CourseTopicService courseSubTopicService = ApplicationContextProvider.getBean(CourseTopicService.class);
        List<CourseTopic> topics = courseSubTopicService.getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("courseLesson", lesson)
                .addSortAsc("position"), 0, 0);
        CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, lesson.getCourse());

        for (CourseTopic topic : topics) {
            CourseTopicResponseDTO topicJSONObject = modelMapper.map(topic, CourseTopicResponseDTO.class);
            if (subscription != null) {
                topicJSONObject.setProgress(courseSubTopicService.getProgress(subscription.getCurrentLecture()));
            }
            List<CourseLecture> lectures = ApplicationContextProvider.getBean(CourseLectureService.class).getInstances(new Search()
                    .addFilterEqual("courseTopic", topic)
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addSortAsc("position"), 0, 0);
            topicJSONObject.setLectures(lectures.stream().map(r -> modelMapper.map(r, LectureResponseDTO.class)).collect(Collectors.toList()));


            result.getTopics().add(topicJSONObject);
        }
        result.setIsPreview(lesson.getPosition() == 1);
        if (subscription != null) {

            result.setProgress(ApplicationContextProvider.getBean(CourseLessonService.class).getProgress(subscription.getCurrentLecture()));
        }
        return ResponseEntity.ok().body(new ResponseObject<>(result));
    }


    @GetMapping("/topic/{id}")
    public ResponseEntity<ResponseObject<CourseTopicResponseDTO>> getTopicById(@PathVariable("id") Long id) throws JSONException {
        CourseTopicResponseDTO result = new CourseTopicResponseDTO();
        Student member = new Student();

        CourseTopic topic = ApplicationContextProvider.getBean(CourseTopicService.class).getInstanceByID(id);
        if (topic == null) {
            throw new ValidationFailedException("Topic not found");
        }
        result = modelMapper.map(topic, CourseTopicResponseDTO.class);
        List<CourseLecture> subTopics = ApplicationContextProvider.getBean(CourseLectureService.class
        ).getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("courseTopic", topic)
                .addSortAsc("position"), 0, 0);
        CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, topic.getCourseLesson().getCourse());

        for (CourseLecture subTopic : subTopics) {
            LectureResponseDTO jSONObject = modelMapper.map(subTopic, LectureResponseDTO.class);
            result.getLectures().add(jSONObject);
        }
        if (subscription != null) {
            result.setProgress(ApplicationContextProvider.getBean(CourseTopicService.class).getProgress(subscription.getCurrentLecture()));
        }
        return ResponseEntity.ok().body(new ResponseObject<>(result));

    }


    @PostMapping("/enroll/{id}")
    public ResponseEntity<ResponseObject<CourseEnrollment>> enroll(@PathVariable("id") Long id) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        assert member != null;
        CourseEnrollment courseEnrollment = subscriptionService.enrolForFreeCourse(member.getId(), id);
        return ResponseEntity.ok().body(new ResponseObject<>(courseEnrollment));

    }


    @PostMapping("/rating")
    public ResponseEntity<ResponseObject<CourseRating>> rateCourse(@RequestBody CourseRatingDTO courseRatingDTO) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        if(member==null){
            throw new ValidationFailedException("Student Not Found");
        }
        CourseService courseService = ApplicationContextProvider.getBean(CourseService.class);
        Course course = courseService.getInstanceByID(courseRatingDTO.getCourseId());
        CourseRating courseRating = new CourseRating();
        courseRating.setCourse(course);
        courseRating.setStudent(member);
        courseRating.setReviewText(courseRatingDTO.getRatingText());
        courseRating.setStarsCount(courseRatingDTO.getStars());
        courseRating = ApplicationContextProvider.getBean(CourseRatingService.class).saveInstance(courseRating);
        return ResponseEntity.ok().body(new ResponseObject<>(courseRating));
    }


    @GetMapping("/rating/{courseId}")
    public ResponseEntity<ResponseList<CourseRatingResponseDTO>> getRatings(@PathVariable("courseId") Long courseId) throws JSONException {

        List<CourseRating> courseRatings = ApplicationContextProvider.getBean(CourseRatingService.class)
                .getInstances(new Search().
                                addFilterEqual("course.id", courseId).
                                addFilterEqual("recordStatus", RecordStatus.ACTIVE).
                                addFilterEqual("publicationStatus", PublicationStatus.ACTIVE),
                        0, 0);

        List<CourseRatingResponseDTO> ratings = new ArrayList<>();
        for (CourseRating courseRating : courseRatings) {
            CourseRatingResponseDTO dto = new CourseRatingResponseDTO();
            dto.setStars(courseRating.getStarsCount());
            dto.setDateCreated(ApiUtils.ENGLISH_DATE_FORMAT.format(courseRating.getDateCreated()));
            dto.setMemberFullName(courseRating.getStudent().getFullName());
            dto.setRatingText(courseRating.getReviewText());
            ratings.add(dto);

        }
        return ResponseEntity.ok().body(new ResponseList<>(ratings, 0, 0, 0));

    }


    @PostMapping("/lectures/complete/{id}")
    public ResponseEntity<CourseEnrollment> completeSubTopic(@PathVariable("id") Long id) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        if(member==null){
            throw new ValidationFailedException("Student Not Found");
        }
        CourseLecture topic = ApplicationContextProvider.getBean(CourseLectureService.class).getInstanceByID(id);
        if (topic == null) {
            throw new ValidationFailedException("Topic  Not Found");
        }
        CourseEnrollment courseEnrollment = ApplicationContextProvider.getBean(CourseSubscriptionService.class).completeSubTopic(member, topic);

        return ResponseEntity.ok().body(courseEnrollment);
    }


    @GetMapping("/mycourses")
    public ResponseEntity<List<CourseResponseDTO>> getStudentCourses(@RequestParam ArticlesFilterDTO queryParamModel) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(queryParamModel.getSearchTerm())
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (queryParamModel.getCategoryId() != null) {
            search.addFilterEqual("course.category.id", queryParamModel.getCategoryId());
        }
        if (queryParamModel.getAuthorId() != null) {
            search.addFilterEqual("course.instructor.id", queryParamModel.getAuthorId());
        }

        if (queryParamModel.getFeatured() != null) {
            search.addFilterEqual("course.isFeatured", queryParamModel.getFeatured());
        }

        if (queryParamModel.getSortBy() != null) {
            search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
        }
        List<CourseResponseDTO> courses = new ArrayList<>();
        for (CourseEnrollment course : ApplicationContextProvider.getBean(CourseSubscriptionService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
            CourseResponseDTO dto = (CourseResponseDTO) course.getCourse();
            int lessonsCount = ApplicationContextProvider.getBean(CourseLessonService.class)
                    .countInstances(new Search()
                            .addFilterEqual("course", course.getCourse())
                            .addFilterEqual("recordStatus", RecordStatus.ACTIVE));
            double rattings = 0;
            try {
                rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course.getCourse()) / 5;
            } catch (Exception e) {
                e.printStackTrace();
            }
            CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(UserDetailsContext.getLoggedInStudent(), course.getCourse());
            dto.setEnrolled(subscription != null);
            dto.setNumberOfLessons(lessonsCount);
            dto.setAverageRating((rattings / 5));
            dto.setRatingsCount(ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course.getCourse()));
            courses.add(dto);
        }
        return ResponseEntity.ok().body((courses));


    }

    @GetMapping("/instructors")
    public ResponseEntity<ResponseList<InstructorResponseDTO>> getInstructors(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit
    ) {
        Search search = InstructorServiceImpl.generateSearchObjectForCourses(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        long count = ApplicationContextProvider.getBean(InstructorService.class).countInstances(search);
        log.info("Instructors Count: {}", count);
        List<CourseInstructor> courses = ApplicationContextProvider.getBean(InstructorService.class).getInstances(search, offset, limit);
        return ResponseEntity.ok().body(new ResponseList<>(courses.stream().map(r -> modelMapper.map(r, InstructorResponseDTO.class)).collect(Collectors.toList()), count, offset, limit));
    }


    @GetMapping("/categories")
    public ResponseEntity<JSONObject> getTopics(@RequestParam ArticlesFilterDTO queryParamModel) throws JSONException {
        JSONObject result = new JSONObject();

        Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (StringUtils.isNotBlank(queryParamModel.getSortBy())) {
            search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
        }
        if (StringUtils.isNotBlank(queryParamModel.getType())) {
            CourseAcademyType academyType = CourseAcademyType.valueOf(queryParamModel.getType());
            search.addFilterEqual("academy", academyType);
        }
        JSONArray topics = new JSONArray();
        for (Category topic : ApplicationContextProvider.getBean(CategoryService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
            int count = ApplicationContextProvider.getBean(CourseService.class).countInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterEqual("category", topic));
            topics.put(
                    new JSONObject()
                            .put("id", topic.getId())
                            .put("name", topic.getName())
                            .put("colorCode", topic.getColorCode())
                            .put("imageUrl", topic.getImageUrl())
                            .put("coursesCount", count)
            );
        }
        //topics= ApiUtils.sortJsonArray(topics, "seriesCount",false);

        result.put("courseCategories", topics);
        return ResponseEntity.ok().body((result));
    }

}
