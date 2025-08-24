package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.courses.OrganisationGroup;
import com.byaffe.learningking.models.courses.OrganisationGroupStudent;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link User}
 */
@Repository
public class OrganisationGroupStudentDaoImpl extends BaseDAOImpl<OrganisationGroupStudent> implements OrganisationGroupStudentDao {
}

