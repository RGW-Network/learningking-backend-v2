package com.byaffe.microtasks.dtos;

import com.byaffe.microtasks.shared.api.BaseDTO;
import com.byaffe.microtasks.shared.constants.PermissionConstant;
import com.byaffe.microtasks.shared.models.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleDTO extends BaseDTO {
private long id;
    private String name;
    private String description;
    private long[] permissionIds;
    private Set<PermissionDTO.PermissionLookup> permissions;


    public void addPermission(PermissionDTO.PermissionLookup permissionLookup){
        if(this.permissions==null){
            this.permissions= new HashSet<>();
        }
        this.permissions.add(permissionLookup);

    }

    public static RoleDTO fromRole(Role dbModel){
        RoleDTO dto= new RoleDTO();
        dto.setName(dbModel.getName());
        dto.setDescription(dbModel.getDescription());

        for(PermissionConstant permissionConstant:dbModel.getPermissions()){
            dto.addPermission(new PermissionDTO.PermissionLookup(permissionConstant));
        }

        dto.setId(dbModel.getId());
        dto.setRecordStatus(dbModel.getRecordStatus().name());
        dto.setCreatedById(dbModel.getCreatedById());
        dto.setCreatedByUsername(dbModel.getCreatedByUsername());
        dto.setChangedById(dbModel.getChangedById());
        dto.setChangedByUserName(dbModel.getChangedByUsername());
        dto.setDateCreated(dbModel.getDateCreated());
        dto.setDateChanged(dbModel.getDateChanged());
        dto.setChangedByFullName(dbModel.getChangedByFullName());
        dto.setCreatedByFullName(dbModel.getCreatedByFullName());

        return dto;
    }


}
