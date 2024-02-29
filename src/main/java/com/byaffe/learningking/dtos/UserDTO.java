package com.byaffe.learningking.dtos;

import com.byaffe.learningking.shared.api.BaseDTO;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.models.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO extends BaseDTO {
    public long id;
    public String username;
    public List<RoleDTO> roles;
    public long[] roleIds;
    public String emailAddress;
    public String lastName;
    public String firstName;
    public String gender;
    public long genderId;
    public String phoneNumber;
    public String initialPassword;
    public String countryName;
    public long countryId;
    public boolean isSuperAdmin;
    public Double walletBalance;
    public List<PermissionConstant> permissions;
    public Double balance;
    public static UserDTO fromModel(User model, Double walletBalance) {
        UserDTO dto = new UserDTO();
        //Attributes to be set here
        dto.username = model.getUsername();
        dto.emailAddress = model.getEmailAddress();
        dto.firstName = model.getFirstName();
        dto.phoneNumber = model.getPhoneNumber();
        dto.lastName = model.getLastName();
        dto.roles = model.getRoles().stream().map(r->RoleDTO.fromRole(r)).collect(Collectors.toList());
        if(walletBalance!=null) {
            dto.balance = model.getBalance();
        }
        dto.isSuperAdmin = (model.hasAdministrativePrivileges());

        if(walletBalance!=null){
            dto.setWalletBalance(walletBalance);
        }



        dto.setId(model.getId());
        dto.setRecordStatus(model.getRecordStatus().name());
        dto.setCreatedById(model.getCreatedById());
        dto.setCreatedByUsername(model.getCreatedByUsername());
        dto.setChangedById(model.getChangedById());
        dto.setChangedByUserName(model.getChangedByUsername());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateChanged(model.getDateChanged());
        return dto;
    }
    public static UserDTO fromModel(User model) {
        UserDTO dto = new UserDTO();
        //Attributes to be set here
        dto.username = model.getUsername();
        dto.emailAddress = model.getEmailAddress();
        dto.firstName = model.getFirstName();
        dto.phoneNumber = model.getPhoneNumber();
        dto.lastName = model.getLastName();
        dto.roles = model.getRoles().stream().map(r->RoleDTO.fromRole(r)).collect(Collectors.toList());
        dto.isSuperAdmin = (model.hasAdministrativePrivileges());
dto.setBalance(model.getBalance());
dto.setWalletBalance(model.getBalance());


        dto.setId(model.getId());
        dto.setRecordStatus(model.getRecordStatus().name());
        dto.setCreatedById(model.getCreatedById());
        dto.setCreatedByUsername(model.getCreatedByUsername());
        dto.setChangedById(model.getChangedById());
        dto.setChangedByUserName(model.getChangedByUsername());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateChanged(model.getDateChanged());
        dto.setChangedByFullName(model.getChangedByFullName());
        dto.setCreatedByFullName(model.getCreatedByFullName());
        return dto;
    }

class ShopDetails{
        String shopName;
        Long shopId;
}
}
