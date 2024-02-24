package com.byaffe.microtasks.shared.security;

import lombok.Data;

import java.util.List;

@Data
public class UserAccount {
    private String username;
    private String password;
    private List<Permission>  permissions;

    public class Permission{
        String name;
    }
}
