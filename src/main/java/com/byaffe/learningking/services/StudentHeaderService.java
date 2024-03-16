package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;

/**
 * Responsible for CRUD operations on {@link Student}
 *
 * @author Ray Gdhrt
 *
 */

public interface StudentHeaderService  {

    
       Student activateStudentAccount(String username, String code) throws Exception ;

    
}
