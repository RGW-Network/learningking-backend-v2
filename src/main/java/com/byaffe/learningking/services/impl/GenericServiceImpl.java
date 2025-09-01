package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.services.GenericService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides for generic implementation of the {@link GenericService}.Concrete classes need to provide implementation of methods that are specific
 to that class or the associated entity.
 *
 *
 * @author Mzee Sr.
 * @param <T>
 *
 */
@Transactional
public abstract class GenericServiceImpl<T extends BaseEntity> extends BaseDAOImpl<T> implements GenericService<T> {

    @Autowired
    protected ModelMapper modelMapper;
    @Override
    public int countInstances(Search arg0) {
        // TODO Auto-generated method stub
        return super.count(arg0);
    }



    @Override
    public void deleteInstance(long id) {
        T instance=getInstanceByIDOrThrow(id);
        deleteInstance(instance);
    }
    @Override
    public T saveInstance(T arg0) {
       return save(arg0);
    }


    @Override
    public void deleteInstance(T arg0) throws OperationFailedException {
        if (!isDeletable(arg0))
            throw new OperationFailedException("Deletion is yet supported for this instance.");
        changeStatusToDeleted(arg0);
    }

    /**
     * Deactivates the instance by changing its status to deleted
     *
     * @param arg0
     */
    private void changeStatusToDeleted(T arg0) {

        arg0.setChangedById(SessionContext.getLoggedInUser().getId());
        arg0.setDateChanged(LocalDateTime.now());
        arg0.setRecordStatus(RecordStatus.DELETED);
        super.save(arg0);

    }

    @Override
    public void deleteInstances(Search search) throws OperationFailedException {
        if (isDeletable((T) super.searchUnique(new Search().setFirstResult(0).setMaxResults(1))))
            throw new OperationFailedException("Deletion is yet supported for this instance.");

        search.setFirstResult(0);
        search.setMaxResults(10);
        // Manage memory using recursion and step loading
        deleteRecursively(search);
    }

    private void deleteRecursively(Search search) throws OperationFailedException {
        List<T> instances = super.search(search);

        if (instances.isEmpty())
            return;

        for (T instance : instances)
            changeStatusToDeleted(instance);

        deleteRecursively(search);
    }

    /**
     * Must be implemented by all classes that extend this abstract class.
     *
     * @param entity
     * @return
     */
    public  boolean isDeletable(T entity){
        return true;
    }

    public abstract List<String> getStringFilterFields();
    public  Search composeSearchObject(String searchTerm) {
        List<String> fields = getStringFilterFields();
        if(fields==null){
            fields= new ArrayList<>();
        }
        return CustomSearchUtils.generateSearchTerms(searchTerm,fields);
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * org.byaffe.systems.akinamama.utils.backend.core.services.GenericService#
     * getInstanceByID(java.lang.String)
     */
    @Override
    public T getInstanceByIDOrThrow(Long arg0) {
        // TODO Auto-generated method stub
        return super.findById(arg0).orElseThrow(()->new ValidationFailedException("Record not found with id "+arg0));
    }


    @Override
    public T getInstanceByIDOrNull(Long arg0) {
        // TODO Auto-generated method stub
        return super.findById(arg0).orElse(null);
    }

    @Override
    public T getInstanceByID(Long arg0) {
        // TODO Auto-generated method stub
        return super.findById(arg0).orElse(null);
    }


    @Override
    public List<T> getInstances(Search arg0, int arg1, int arg2) {
        if(arg0==null){
            arg0=new Search();
        }
        // TODO Auto-generated method stub
        return super.search(arg0.setFirstResult(arg1).setMaxResults(arg2));
    }
}
