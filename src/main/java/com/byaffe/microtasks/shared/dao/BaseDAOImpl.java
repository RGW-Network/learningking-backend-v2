package com.byaffe.microtasks.shared.dao;

import com.byaffe.microtasks.shared.models.BaseEntity;
import com.byaffe.microtasks.shared.models.User;
import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.MetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import com.byaffe.microtasks.controllers.UserDetailsContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class BaseDAOImpl<T extends BaseEntity> extends GenericDAOImpl<T, Long> implements BaseDao<T> {

    @Autowired
    public EntityManager entityManager;

    @Autowired
    public JpaAnnotationMetadataUtil mdu;

    public BaseDAOImpl() {
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    protected JPASearchProcessor getSearchProcessor() {
        return new JPASearchProcessor(mdu);
    }
    @Override
    protected MetadataUtil getMetadataUtil() {
        return new JpaAnnotationMetadataUtil();
    }
    @Override
    public T save(T entity) {
        User user=UserDetailsContext.getLoggedInUser();

        if(user!=null) {
             entity.addAuditTrail(user);
        }

        T object= super.save(entity);
        if(StringUtils.isBlank(object.getSerialNumber())){
            object.setSerialNumber(object.generateSerialNumber());
            object=super.save(object);
        }
        return object;
    }

    @Transactional
    @Override
    public T saveAndFlush(T entity, User loggedInUser) {
        if(loggedInUser!=null) {
            entity.addAuditTrail(loggedInUser);
        }
        T result = super.save(entity);
        super.flush();
        return result;
    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.ofNullable(super.find(id));
    }

    @Override
    public T merge(T entity, User loggedInUser) {
        entity.addAuditTrail(loggedInUser);
        return super.merge(entity);
    }

    @Override
    public T mergeAndFlush(T entity, User loggedInUser) {
        entity.addAuditTrail(loggedInUser);
        T result = super.merge(entity);
        super.flush();
        return result;
    }
}
