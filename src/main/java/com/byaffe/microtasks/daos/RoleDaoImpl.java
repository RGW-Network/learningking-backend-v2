package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.shared.dao.BaseDAOImpl;
import com.byaffe.microtasks.shared.models.Role;
import com.byaffe.microtasks.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link User}
 */
@Repository
public class RoleDaoImpl extends BaseDAOImpl<Role> implements RoleDao {
}

