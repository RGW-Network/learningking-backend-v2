package com.byaffe.learningking.shared.constants;

public enum PermissionModule {
    Manage_Lookups("Manage Lookups"),
    Manage_Users("Manage Admin Users"),
    Manage_Categories("Manage Course Categories"),
    Manage_Courses("Manage Courses"),
    Manage_Students("Manage Students"),
    Manage_Instructors("Manage Instructors"),
    Manage_Events("Manage Events"),
    Manage_Organisations("Manage Organisations"),
    ;

    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    PermissionModule(String displayName) {
        this.displayName = displayName;
    }
}
