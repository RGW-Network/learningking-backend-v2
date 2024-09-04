package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.courses.Organisation;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.models.Role;
import com.byaffe.learningking.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link User}
 */
@Repository
public class OrganisationDaoImpl extends BaseDAOImpl<Organisation> implements OrganisationDao {
}

