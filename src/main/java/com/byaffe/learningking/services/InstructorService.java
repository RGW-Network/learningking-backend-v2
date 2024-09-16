package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.instructor.InstructorRequestDTO;
import com.byaffe.learningking.models.courses.CourseInstructor;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.User;
import com.googlecode.genericdao.search.Search;

import java.util.List;

/**
 * Responsible for CRUD operations on {@link CourseInstructor}
 *
 * @author Ray Gdhrt
 *
 */
public interface InstructorService extends GenericService<CourseInstructor> {

    /**
     * Adds a member to the database.
     *
     * @param CourseInstructor
     * @return
     * @throws ValidationFailedException if the following attributes are blank:
     * name, phoneNumber
     */
    CourseInstructor quickSave(CourseInstructor CourseInstructor) throws ValidationFailedException;
    CourseInstructor doRegister(InstructorRequestDTO dto) throws ValidationFailedException;


    /**
     *
     * @param email
     * @return
     */
     CourseInstructor sendOTP(String email);
    /**
     * Gets a list of members that match the specified search criteria
     *
     * @param offset
     * @param limit
     * @return
     */
    List<CourseInstructor> getCourseInstructors(Search search, int offset, int limit);

    /**
     * Counts a list of members that match the specified search criteria
     *
     * @param search
     * @return
     */
    int countCourseInstructors(Search search);

    /**
     * Gets a member that matches the specified identifier
     *
     * @param memberId
     * @return
     */
    CourseInstructor getCourseInstructorById(String memberId);

    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param CourseInstructor
     * @throws ValidationFailedException
     */
    void delete(CourseInstructor CourseInstructor) throws ValidationFailedException;
    
    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param CourseInstructor
     * @throws ValidationFailedException
     */
    void block(CourseInstructor CourseInstructor, String blockNotes) throws ValidationFailedException,OperationFailedException;

     void unblock(CourseInstructor CourseInstructor, String unblockNotes) throws ValidationFailedException, OperationFailedException;
    /**
     * Gets a member that matches the specified identifier
     *
     * @param phoneNumber
     * @return
     */
    CourseInstructor getCourseInstructorByPhoneNumber(String phoneNumber);
    
       CourseInstructor activateCourseInstructorAccount(String username, String code) throws Exception ;

    /**
     * Gets a member that matches the specified identifier
     *
     * @param user
     * @return
     */
    CourseInstructor getCourseInstructorByUserAccount(User user);

 
    CourseInstructor getCourseInstructorByUsername(String email) ;
    /**
     *
     * @param email
     * @return
     */
    CourseInstructor getCourseInstructorByEmail(String email);
    }
