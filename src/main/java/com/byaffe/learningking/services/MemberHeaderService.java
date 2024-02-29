package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Member;

/**
 * Responsible for CRUD operations on {@link Member}
 *
 * @author Ray Gdhrt
 *
 */

public interface MemberHeaderService  {

    
       Member activateMemberAccount(String username, String code) throws Exception ;

    
}
