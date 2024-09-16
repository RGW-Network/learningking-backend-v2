package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.courses.OrganisationStudent;
import com.byaffe.learningking.models.courses.OrganisationStudentGroup;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link User}
 */
@Repository
public class OrganisationStudentGroupDaoImpl extends BaseDAOImpl<OrganisationStudentGroup> implements OrganisationStudentGroupDao {
}

