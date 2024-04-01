/**
 * 
 */
package com.byaffe.learningking.services;

import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;

import java.util.List;

/**
 * Generic interface from which most services are derived. It defines CRUD
 * methods for all entities that inherit/implement this interface.
 * 
 * @author Mzee Sr.
 *
 */
public interface GenericService<T> {

	/**
	 * Returns the instance of the object represented by the specified identifier.
	 * 
	 * @return
	 */
	T getInstanceByID(Long id);

	/**
	 * Adds a copy <T> to the database after running validation rules as specified
	 * in the use case documents. If the instance already exists, it is updated
	 * after running the validation rules.
	 * 
	 * @param instance
	 * @return a copy of the saved instance.
	 * @throws ValidationFailedException
	 * @throws OperationFailedException
	 */
	T saveInstance(T instance) throws ValidationFailedException, OperationFailedException;

	/**
	 * Queries for a list of object instances that match the specified search
	 * criteria, from the specified offset and limit.
	 * 
	 * @param search
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<T> getInstances(Search search, int offset, int limit);

	/**
	 * Queries for the number of object instances that match the specified search
	 * criteria.
	 * 
	 * @param search
	 * @return
	 */
	int countInstances(Search search);

	/**
	 * Deactivates the specified instance. Deactivated records can neither be used
	 * to create new records nor can they appear in lists of queried data from the
	 * database.
	 * 
	 * However, records already saved that reference the deactivated record still
	 * maintain that relationship. As a result, a deactivated record will appear in
	 * its relationships but only for viewing purposes.
	 * 
	 * This obviously means that we do not permanently delete records from the DB,
	 * but change record statuses.
	 * 
	 * @param instance
	 * @throws OperationFailedException
	 */
	void deleteInstance(T instance) throws OperationFailedException;

	/**
	 * Used for bulk deletion. Check the description of deleteInstance(T instance)
	 * for a detailed description of how deletion happens and the constraints.
	 * 
	 * @param search
	 * @throws OperationFailedException
	 */
	void deleteInstances(Search search) throws OperationFailedException;
}
