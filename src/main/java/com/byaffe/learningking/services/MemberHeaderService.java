package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;

/**
 * Responsible for CRUD operations on {@link Student}
 *
 * @author Ray Gdhrt
 *
 */

public interface MemberHeaderService  {

    
       Student activateMemberAccount(String username, String code) throws Exception ;

    
}
