package com.byaffe.microtasks.daos;

import com.byaffe.microtasks.models.TaskExecution;
import com.byaffe.microtasks.models.WithdrawRequest;
import com.byaffe.microtasks.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link WithdrawRequest}
 */
@Repository
public class WithdrawRequestDaoImpl extends BaseDAOImpl<WithdrawRequest> implements WithdrawRequestDao{

}

