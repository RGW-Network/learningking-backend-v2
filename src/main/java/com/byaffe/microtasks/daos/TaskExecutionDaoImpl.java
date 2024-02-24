package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.models.Task;
import com.byaffe.microtasks.models.TaskExecution;
import com.byaffe.microtasks.shared.dao.BaseDAOImpl;
import com.byaffe.microtasks.shared.models.User;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link TaskExecution}
 */
@Repository
public class TaskExecutionDaoImpl extends BaseDAOImpl<TaskExecution> implements TaskExecutionDao{

}

