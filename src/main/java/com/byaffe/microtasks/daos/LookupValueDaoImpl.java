package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.models.LookupType;
import com.byaffe.microtasks.models.LookupValue;
import com.byaffe.microtasks.shared.dao.BaseDAOImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object class for {@link LookupValue}
 */
@Repository
public class LookupValueDaoImpl extends BaseDAOImpl<LookupValue> implements LookupValueDao {
}

