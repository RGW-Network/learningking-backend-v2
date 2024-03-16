package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.models.Country;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link LookupValue}
 */
@Repository
public class CourseLectureDaoImpl extends BaseDAOImpl<CourseLecture> implements CourseLectureDao {

}

