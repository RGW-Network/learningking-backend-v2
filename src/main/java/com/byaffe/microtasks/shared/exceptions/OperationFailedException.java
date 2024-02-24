package com.byaffe.microtasks.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author RayGdhrt
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class OperationFailedException extends RuntimeException {

    private String message;
   
    public OperationFailedException(String message) {
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
