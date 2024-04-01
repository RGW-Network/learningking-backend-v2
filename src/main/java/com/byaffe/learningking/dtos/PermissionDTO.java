package com.byaffe.learningking.dtos;

import com.byaffe.learningking.shared.api.BaseDTO;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.constants.PermissionModule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionDTO  extends BaseDTO {

    public String module;
    public List<PermissionLookup> permissions;
    void addPermission(PermissionLookup permission){
        if(this.permissions==null){
            permissions= new ArrayList<>();
        }
        permissions.add(permission);
    }

   public static List<PermissionDTO> getOrderedPermissions(){
        List<PermissionDTO>  list= new ArrayList<>();
        for(PermissionModule permissionModule:PermissionModule.values()){
            PermissionDTO permissionDTO= new PermissionDTO();
            permissionDTO.module=permissionModule.getDisplayName();
            for(PermissionConstant permissionConstant: PermissionConstant.values()){
                if(permissionConstant.getPermissionModule()==permissionModule) {
                    permissionDTO.addPermission(new PermissionLookup(permissionConstant));
                }
            }
            list.add(permissionDTO);
        }
        return list;
    }
  public   static class PermissionLookup{
        public long id;
        public String name;
        public String moduleName;

        public PermissionLookup(PermissionConstant permissionConstant) {
            this.id = permissionConstant.getId();
            this.name = permissionConstant.getName();
            this.moduleName=permissionConstant.getPermissionModule().getDisplayName();
        }
    }



}
