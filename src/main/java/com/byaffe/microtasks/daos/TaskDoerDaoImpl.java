package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.models.TaskCreator;
import com.byaffe.microtasks.models.TaskDoer;
import com.byaffe.microtasks.shared.dao.BaseDAOImpl;
import com.byaffe.microtasks.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link User}
 */
@Repository
public class TaskDoerDaoImpl extends BaseDAOImpl<TaskDoer> implements TaskDoerDao{

}

