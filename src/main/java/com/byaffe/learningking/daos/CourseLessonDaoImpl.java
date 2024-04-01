package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link User}
 */
@Repository
public class CourseLessonDaoImpl extends BaseDAOImpl<CourseLesson> implements CourseLessonDao {
}

