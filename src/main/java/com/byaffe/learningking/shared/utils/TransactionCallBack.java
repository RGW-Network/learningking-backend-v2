package com.byaffe.learningking.shared.utils;

@FunctionalInterface
public interface TransactionCallBack<T>{
    T execute() throws Exception;
}
