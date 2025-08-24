package com.byaffe.learningking.dtos.auth;

import com.byaffe.learningking.shared.api.BaseDTO;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.constants.PermissionModule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionDTO extends BaseDTO {

    public String module;
    public List<PermissionLookup> permissions;

    void addPermission(PermissionLookup permission) {
        if (this.permissions == null) {
            permissions = new ArrayList<>();
        }
        permissions.add(permission);
    }

    public static List<PermissionLookup> getOrderedPermissions() {
        List<PermissionLookup> list = new ArrayList<>();
        for (PermissionConstant permissionConstant : PermissionConstant.values()) {
            list.add(new PermissionLookup(permissionConstant));

        }
        return list;
    }

    public static class PermissionLookup {
        public String id;
        public String name;
        public String module;

        public PermissionLookup(PermissionConstant permissionConstant) {
            this.id = permissionConstant.name();
            this.name = permissionConstant.getName();
            this.module=permissionConstant.getModule();
        }
    }


}
