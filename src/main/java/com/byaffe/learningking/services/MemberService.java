package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.User;
import com.googlecode.genericdao.search.Search;

import java.util.List;

/**
 * Responsible for CRUD operations on {@link Student}
 *
 * @author Ray Gdhrt
 *
 */
public interface MemberService extends GenericService<Student> {

    /**
     * Adds a member to the database.
     *
     * @param student
     * @return
     * @throws ValidationFailedException if the following attributes are blank:
     * name, phoneNumber
     */
    Student quickSave(Student student) throws ValidationFailedException;

    /**
     * Adds a member to the database outside the spring security context.
     *
     * @param student
     * @return
     * @throws ValidationFailedException
     */
    Student saveOutsideContext(Student student) throws ValidationFailedException;

   
    /**
     * Gets a list of members that match the specified search criteria
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Student> getMembers(Search search, int offset, int limit);

    /**
     * Counts a list of members that match the specified search criteria
     *
     * @param search
     * @return
     */
    int countMembers(Search search);

    /**
     * Gets a member that matches the specified identifier
     *
     * @param memberId
     * @return
     */
    Student getMemberById(String memberId);

    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param student
     * @throws ValidationFailedException
     */
    void delete(Student student) throws ValidationFailedException;
    
    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param student
     * @throws ValidationFailedException
     */
    void block(Student student, String blockNotes) throws ValidationFailedException,OperationFailedException;

     void unblock(Student student, String unblockNotes) throws ValidationFailedException, OperationFailedException;
    /**
     * Gets a member that matches the specified identifier
     *
     * @param phoneNumber
     * @return
     */
    Student getMemberByPhoneNumber(String phoneNumber);
    
       Student activateMemberAccount(String username, String code) throws Exception ;

    /**
     * Gets a member that matches the specified identifier
     *
     * @param user
     * @return
     */
    Student getMemberByUserAccount(User user);

 
    Student getMemberByUsername(String email) ;
    /**
     *
     * @param email
     * @return
     */
    Student getMemberByEmail(String email);
    
     Student doRegister(String firstName, String lastName, String username, String password) throws ValidationFailedException ;

    
    /**
     * 
     * @param username
     * @param password
     * @return
     * @throws ValidationFailedException 
     */
     Student doLogin(String username, String password) throws ValidationFailedException ;
}
