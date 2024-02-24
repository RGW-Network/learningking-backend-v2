package com.byaffe.microtasks.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception thrown for failed data validations
 * @author RayGdhrt
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ValidationFailedException extends RuntimeException {

    private String message;

    public ValidationFailedException(String message) {
        super(message);
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
