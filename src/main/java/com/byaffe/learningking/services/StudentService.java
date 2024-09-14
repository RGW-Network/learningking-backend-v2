package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.student.StudentProfileUpdateRequestDTO;
import com.byaffe.learningking.dtos.auth.UserRegistrationRequestDTO;
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
public interface StudentService extends GenericService<Student> {

    /**
     * Adds a member to the database.
     *
     * @param student
     * @return
     * @throws ValidationFailedException if the following attributes are blank:
     * name, phoneNumber
     */
    Student quickSave(Student student) throws ValidationFailedException;
    Student saveStudent(UserRegistrationRequestDTO dto) throws ValidationFailedException;
    Student updateProfile(StudentProfileUpdateRequestDTO dto) throws ValidationFailedException;


    /**
     *
     * @param email
     * @return
     */
     Student sendOTP(String email);
    /**
     * Gets a list of members that match the specified search criteria
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Student> getStudents(Search search, int offset, int limit);

    /**
     * Counts a list of members that match the specified search criteria
     *
     * @param search
     * @return
     */
    int countStudents(Search search);

    /**
     * Gets a member that matches the specified identifier
     *
     * @param memberId
     * @return
     */
    Student getStudentById(String memberId);

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
    Student getStudentByPhoneNumber(String phoneNumber);
    
       Student activateStudentAccount(String username, String code) throws Exception ;

    /**
     * Gets a member that matches the specified identifier
     *
     * @param user
     * @return
     */
    Student getStudentByUserAccount(User user);

 
    Student getStudentByUsername(String email) ;
    /**
     *
     * @param email
     * @return
     */
    Student getStudentByEmail(String email);
    
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
