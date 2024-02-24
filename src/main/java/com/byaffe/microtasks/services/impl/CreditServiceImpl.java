package com.byaffe.microtasks.services.impl;

import com.byaffe.microtasks.controllers.UserDetailsContext;
import com.byaffe.microtasks.daos.TaskDao;
import com.byaffe.microtasks.daos.TaskExecutionDao;
import com.byaffe.microtasks.daos.UserDao;
import com.byaffe.microtasks.daos.WithdrawRequestDao;
import com.byaffe.microtasks.dtos.TaskRequestDTO;
import com.byaffe.microtasks.models.*;
import com.byaffe.microtasks.services.CreditService;
import com.byaffe.microtasks.services.LookupValueService;
import com.byaffe.microtasks.services.TaskService;
import com.byaffe.microtasks.shared.constants.RecordStatus;
import com.byaffe.microtasks.shared.exceptions.ResourceNotFoundException;
import com.byaffe.microtasks.shared.exceptions.ValidationFailedException;
import com.byaffe.microtasks.shared.models.User;
import com.byaffe.microtasks.shared.utils.CustomSearchUtils;
import com.byaffe.microtasks.shared.utils.Validate;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CreditServiceImpl implements CreditService {
    @Autowired
    TaskExecutionDao taskExecutionDao;

    @Autowired
    UserDao userDao;
    @Autowired
    WithdrawRequestDao withdrawRequestDao;


    @Override
    public void updateUserCredit(Long id)  {
        User user= userDao.find(id);

        Double totalEarnings= taskExecutionDao.searchUnique(new Search()
                .addFilterEqual("status",TaskExecutionStatus.APPROVED)
                .addFilterEqual("createdById",id)
                .addField("amountPaid", Field.OP_SUM));
        if(totalEarnings==null){
            totalEarnings=0.0;
        }
        Double totalWithdraws= withdrawRequestDao.searchUnique(new Search()
                .addFilterEqual("status",WithdrawRequestStatus.DISBURSED)
                .addFilterEqual("createdById",id)
                .addField("amountRequested", Field.OP_SUM));
        if(totalWithdraws==null){
            totalWithdraws=0.0;
        }
        user.setBalance(totalEarnings-totalWithdraws);
        userDao.save(user);
    }
}
