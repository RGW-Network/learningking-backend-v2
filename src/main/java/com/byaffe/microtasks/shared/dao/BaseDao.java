package com.byaffe.microtasks.shared.dao;

import com.byaffe.microtasks.shared.models.BaseEntity;
import com.byaffe.microtasks.shared.models.User;
import com.googlecode.genericdao.dao.jpa.GenericDAO;

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
}
