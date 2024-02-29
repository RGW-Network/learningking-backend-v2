/**
 * 
 */
package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.controllers.UserDetailsContext;
import com.byaffe.learningking.services.GenericService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.googlecode.genericdao.search.Search;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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


	@Override
	public int countInstances(Search arg0) {
		// TODO Auto-generated method stub
		return super.count(arg0);
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

		arg0.setChangedById(UserDetailsContext.getLoggedInUser().getId());
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
	public abstract boolean isDeletable(T entity) throws OperationFailedException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.byaffe.systems.akinamama.utils.backend.core.services.GenericService#
	 * getInstanceByID(java.lang.String)
	 */
	@Override
	public T getInstanceByID(Long arg0) {
		// TODO Auto-generated method stub
		return super.getReference(arg0);
	}


	@Override
	public List<T> getInstances(Search arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return super.search(arg0.setFirstResult(arg1).setMaxResults(arg2));
	}
}
