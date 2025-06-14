package com.byaffe.learningking.dtos.auth;

import com.byaffe.learningking.shared.api.BaseDTO;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.models.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleRequestDTO  {
private Long id;
    private String name;
    private String description;
    private Set<PermissionConstant> permissions= new HashSet<>();

}
