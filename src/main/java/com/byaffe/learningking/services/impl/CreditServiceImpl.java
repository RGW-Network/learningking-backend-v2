package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.TaskExecutionDao;
import com.byaffe.learningking.daos.UserDao;
import com.byaffe.learningking.daos.WithdrawRequestDao;
import com.byaffe.learningking.models.*;
import com.byaffe.learningking.services.CreditService;
import com.byaffe.learningking.shared.models.User;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
