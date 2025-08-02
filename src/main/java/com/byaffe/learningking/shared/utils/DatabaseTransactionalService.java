package com.byaffe.learningking.shared.utils;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DatabaseTransactionalService {

    @Transactional
    public <T> T executeInTransaction(TransactionCallBack<T> callBack) {
        try {
            return callBack.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public <T> T executeInTransactionWithException(TransactionCallBack<T> callBack) throws Exception{
        return callBack.execute();
    }
}
