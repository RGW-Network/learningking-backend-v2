package com.byaffe.learningking.shared.security;

import com.byaffe.learningking.config.SessionDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.User;

/**
 * This class is used to store the current request's bearer token. We have used the
 * InheritableThreadLocal variable. This enables the child threads created from
 * the main thread in our application to use the bearer token of the Parent Thread.
 *
 */
public class UserDetailsContext {

    private UserDetailsContext() {
        // Add a private constructor to hide the implicit public one.
    }

    private static ThreadLocal<SessionDTO> bearerToken = new InheritableThreadLocal<>();

    public static void setLoggedInUser(User id) {
        SessionDTO  dto= bearerToken.get();
        if(dto==null){
            dto= new SessionDTO();
        }
        dto.setLoggedInUser(id);
        bearerToken.set(dto);
    }
    public static User getLoggedInUser() {

        SessionDTO sessionDTO=bearerToken.get();
        if(sessionDTO!=null){
            return sessionDTO.getLoggedInUser();
        }
        return null;
    }

    public static void setLoggedInMember(Student id) {
        SessionDTO  dto= bearerToken.get();
        if(dto==null){
            dto= new SessionDTO();
        }
        dto.setLoggedInMember(id);
        bearerToken.set(dto);
    }
    public static Student getLoggedInMember() {
        SessionDTO sessionDTO=bearerToken.get();
        if(sessionDTO!=null){
            return sessionDTO.getLoggedInMember();
        }
        return null;
    }

    public static void clear() {
        bearerToken.remove();
    }
}
