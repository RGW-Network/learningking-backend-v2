package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.courses.OrganisationGroup;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link User}
 */
@Repository
public class OrganisationGroupDaoImpl extends BaseDAOImpl<OrganisationGroup> implements OrganisationGroupDao {
}

