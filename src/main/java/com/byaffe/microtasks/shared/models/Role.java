/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.byaffe.microtasks.shared.models;

import com.byaffe.microtasks.shared.constants.PermissionConstant;
import com.byaffe.microtasks.shared.constants.SecurityConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Inheritance(strategy = InheritanceType.JOINED)
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false,columnDefinition = "TEXT")
    private String description;
    @ElementCollection( targetClass = PermissionConstant.class)
    @JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permissions", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<PermissionConstant> permissions;


    public Role(final String name, final String description, final Set<PermissionConstant> permissions) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;

    }

    public Role(final String name, final String description) {
        this.name = name;
        this.description = description;

    }


    public void addPermission(final PermissionConstant permission) {
        if(this.permissions==null){
            this.permissions= new HashSet<>();
        }
        this.getPermissions().add(permission);
    }

    public void removePermission(final PermissionConstant permission) {
        if (this.getPermissions().contains(permission)) {
            this.getPermissions().remove(permission);
        }
    }




    public boolean checkIfDefaultAdminRole() {
        return this.getName().equals(SecurityConstants.SUPER_ADMIN_ROLE);
    }

    @Override
    public String toString() {
        if (this.getName() != null && this.getName().trim().length() > 0) {
            return this.getName();
        }
        return super.toString();
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof Role && this.getId() != 0) ? this.getId() ==(((Role) other).getId()) : (other == this);
    }

    @Override
    public int hashCode() {
        return (this.getId() != 0) ? (int) (this.getClass().hashCode() + this.getId()) : super.hashCode();
    }

    public boolean hasPermission(final PermissionConstant perm) {
        if (this.permissions != null) {
            for (final PermissionConstant permission : this.permissions) {
                if (permission.equals(perm)) {
                    return true;
                }
            }
        }
        return false;
    }
}
