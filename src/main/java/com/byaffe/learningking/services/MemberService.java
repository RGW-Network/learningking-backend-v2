package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.User;
import com.googlecode.genericdao.search.Search;

import java.util.List;

/**
 * Responsible for CRUD operations on {@link Member}
 *
 * @author Ray Gdhrt
 *
 */
public interface MemberService extends GenericService<Member> {

    /**
     * Adds a member to the database.
     *
     * @param member
     * @return
     * @throws ValidationFailedException if the following attributes are blank:
     * name, phoneNumber
     */
    Member quickSave(Member member) throws ValidationFailedException;

    /**
     * Adds a member to the database outside the spring security context.
     *
     * @param member
     * @return
     * @throws ValidationFailedException
     */
    Member saveOutsideContext(Member member) throws ValidationFailedException;

   
    /**
     * Gets a list of members that match the specified search criteria
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Member> getMembers(Search search, int offset, int limit);

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
    Member getMemberById(String memberId);

    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param member
     * @throws ValidationFailedException
     */
    void delete(Member member) throws ValidationFailedException;
    
    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param member
     * @throws ValidationFailedException
     */
    void block(Member member,String blockNotes) throws ValidationFailedException,OperationFailedException;

     void unblock(Member member, String unblockNotes) throws ValidationFailedException, OperationFailedException;
    /**
     * Gets a member that matches the specified identifier
     *
     * @param phoneNumber
     * @return
     */
    Member getMemberByPhoneNumber(String phoneNumber);
    
       Member activateMemberAccount(String username, String code) throws Exception ;

    /**
     * Gets a member that matches the specified identifier
     *
     * @param user
     * @return
     */
    Member getMemberByUserAccount(User user);

 
    Member getMemberByUsername(String email) ;
    /**
     *
     * @param email
     * @return
     */
    Member getMemberByEmail(String email);
    
     Member doRegister(String firstName, String lastName, String username, String password) throws ValidationFailedException ;

    
    /**
     * 
     * @param username
     * @param password
     * @return
     * @throws ValidationFailedException 
     */
     Member doLogin(String username, String password) throws ValidationFailedException ;
}
