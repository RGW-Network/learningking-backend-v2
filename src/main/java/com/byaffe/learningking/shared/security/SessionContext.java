package com.byaffe.learningking.shared.security;

import com.byaffe.learningking.config.SessionDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.exceptions.PermissionDeniedException;
import com.byaffe.learningking.shared.models.User;

/**
 * This class is used to store the current request's bearer token. We have used the
 * InheritableThreadLocal variable. This enables the child threads created from
 * the main thread in our application to use the bearer token of the Parent Thread.
 */
public class SessionContext {

    private SessionContext() {
        // Add a private constructor to hide the implicit public one.
    }

    private static ThreadLocal<SessionDTO> bearerToken = new InheritableThreadLocal<>();

    public static void setLoggedInUser(User id) {
        SessionDTO dto = bearerToken.get();
        if (dto == null) {
            dto = new SessionDTO();
        }
        dto.setLoggedInUser(id);
        bearerToken.set(dto);
    }

    public static User getLoggedInUser() {

        SessionDTO sessionDTO = bearerToken.get();
        if (sessionDTO != null) {
            return sessionDTO.getLoggedInUser();
        }
        return null;
    }

    public static boolean isSuperAdmin() {
        User user = getLoggedInUser();
        if (user != null) return user.hasAdministrativePrivileges();
        return false;
    }

    public static void setLoggedInStudent(Student id) {
        SessionDTO dto = bearerToken.get();
        if (dto == null) {
            dto = new SessionDTO();
        }
        dto.setLoggedInStudent(id);
        bearerToken.set(dto);
    }

    public static Student getLoggedInStudent() {
        SessionDTO sessionDTO = bearerToken.get();
        if (sessionDTO != null) {
            return sessionDTO.getLoggedInStudent();
        }
        return null;
    }


    /**
     * Checks if logged-in user is a super admin. If No, a PermissionDeniedException will be thrown
     */
    public static void superAdminProtection() {
        if (!isSuperAdmin()) {
            throw new PermissionDeniedException();
        }
    }

    /**
     * Checks if logged-in user has a given permission. If permission is not found return true else returns false
     *
     * @return
     */
    public static boolean hasPermission(PermissionConstant permissionConstant) {
        User user = getLoggedInUser();
        if (user == null || !user.hasPermission(permissionConstant)) return false;

        return true;
    }

    /**
     * Checks if logged-in user has a given permission. If permission is not found a PermissionDeniedException will be thrown
     *
     * @param permissionConstant
     */
    public static void permissionProtection(PermissionConstant permissionConstant) {
        if (!hasPermission(permissionConstant)) {
            throw new PermissionDeniedException();
        }
    }
    public static void clear() {
        bearerToken.remove();
    }
}
