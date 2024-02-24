package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.shared.dao.BaseDao;
import com.byaffe.microtasks.shared.models.Role;
import com.byaffe.microtasks.shared.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Data Access Object class for {@link User}
 */
public interface RoleDao extends BaseDao<Role> {;
}

