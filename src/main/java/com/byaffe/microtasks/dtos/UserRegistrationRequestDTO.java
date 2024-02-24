package com.byaffe.microtasks.dtos;

import com.byaffe.microtasks.models.TaskCreator;
import com.byaffe.microtasks.models.TaskDoer;
import com.byaffe.microtasks.shared.api.BaseDTO;
import com.byaffe.microtasks.shared.constants.PermissionConstant;
import com.byaffe.microtasks.shared.constants.SecurityConstants;
import com.byaffe.microtasks.shared.models.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserRegistrationRequestDTO {
    public String emailAddress;
    public String lastName;
    public String firstName;
    public String password;
    public String confirmPassword;
    public Long countryId;



}
