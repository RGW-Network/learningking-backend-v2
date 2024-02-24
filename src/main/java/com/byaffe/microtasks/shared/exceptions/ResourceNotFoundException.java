package com.byaffe.microtasks.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author RayGdhrt
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String reourceName;
    private String fieldName;
    private Object fieldValue;
    
    

    public ResourceNotFoundException(String reourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : %s", reourceName, fieldName, fieldValue));
        this.reourceName = reourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getReourceName() {
        return reourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

}
