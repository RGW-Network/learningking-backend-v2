package com.byaffe.learningking.shared.dao;

import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.User;
import com.googlecode.genericdao.dao.jpa.GenericDAO;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends BaseEntity> extends GenericDAO<T, Long> {

    /**
     * This method is used to save changes to the database and wait for the transaction to complete.
     *
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     * This method is used to save changes and immediately flush the changes to the database
     *
     * @param entity
     * @param loggedInUser
     * @return
     */
    T saveAndFlush(T entity, User loggedInUser);

    Optional<T> findById(long id);

    /**
     * @param entity
     * @param loggedInUser
     * @return
     */
    T merge(T entity, User loggedInUser);

    /**
     * @param entity
     * @param loggedInUser
     * @return
     */
    T mergeAndFlush(T entity, User loggedInUser);

    public List<T> searchByPropertyEqual(String string, Object o);

    public T searchUniqueByPropertyEqual(String string, Object o);

    public List<T> searchByPropertyEqual(String string, Object o, RecordStatus rs);

    public List<T> searchByRecordStatus(RecordStatus rs);

    public T searchUniqueByPropertyEqual(String string, Object o, RecordStatus rs);
}
