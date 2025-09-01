package com.byaffe.learningking.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception thrown for failed data validations
 * @author RayGdhrt
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {
        super("You Don't Have The Required Permissions");
    }


}
