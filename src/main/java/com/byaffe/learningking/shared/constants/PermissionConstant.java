package com.byaffe.learningking.shared.constants;

import lombok.Getter;

@Getter
public enum PermissionConstant {
    Manage_Lookups("Manage Lookups"),
    Manage_Users("Manage Admin Users"),
    Manage_Categories("Manage Course Categories"),
    Manage_Courses("Manage Courses"),
    Manage_Students("Manage Students"),
    Manage_Instructors("Manage Instructors"),
    Manage_Events("Manage Events"),
    Manage_Articles("Manage Article"),
    Manage_Reviews("Manage Reviews"),
    Manage_Message_Templates("Manage Message Templates"),
    Manage_System_Settings("Manage System Settings"),
    Manage_Subscription_Plans("Manage Subscription Plans"),
    Manage_Organisations("Manage Organisations"),
    ;
    private String name;

    PermissionConstant(String name) {
        this.name = name;
    }


}
