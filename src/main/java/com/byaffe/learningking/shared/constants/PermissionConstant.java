package com.byaffe.learningking.shared.constants;

import lombok.Getter;

@Getter
public enum PermissionConstant {
    
    // ========================================
    // SYSTEM ADMINISTRATION PERMISSIONS
    // ========================================
    
    // User Management
    Manage_Users("Manage Admin Users", "SYSTEM"),
    
    // System Configuration
    Manage_System_Settings("Manage System Settings", "SYSTEM"),
    Manage_Message_Templates("Manage Message Templates", "SYSTEM"),
    Manage_Lookups("Manage Lookups", "SYSTEM"),
    
    // ========================================
    // CONTENT MANAGEMENT PERMISSIONS
    // ========================================
    
    // Course Management
    Manage_Categories("Manage Course Categories", "CONTENT"),
    Manage_Subscription_Plans("Manage Subscription Plans", "CONTENT"),
    
    // Content Creation
    Course_Create("Create Courses", "CONTENT"),
    Article_Create("Create Articles", "CONTENT"),
    Event_Create("Create Events", "CONTENT"),
    
    // ========================================
    // USER MANAGEMENT PERMISSIONS
    // ========================================
    
    // Student Management
    Manage_Students("Manage Students", "USER_MANAGEMENT"),
    
    // Instructor Management
    Manage_Instructors("Manage Instructors", "USER_MANAGEMENT"),
    
    // Organization Management
    Manage_Organisations("Manage Organisations", "USER_MANAGEMENT"),
    
    // ========================================
    // CONTENT MODERATION PERMISSIONS
    // ========================================
    
    // Review Management
    Manage_Reviews("Manage Reviews", "MODERATION"),
    
    // ========================================
    // RECORD-LEVEL COURSE PERMISSIONS
    // ========================================
    
    // Own Course Permissions
    Course_View_Own("View Own Courses", "COURSE"),
    Course_Edit_Own("Edit Own Courses", "COURSE"),
    Course_Delete_Own("Delete Own Courses", "COURSE"),
    Course_Publish_Own("Publish Own Courses", "COURSE"),
    
    // All Course Permissions
    Course_View_All("View All Courses", "COURSE"),
    Course_Edit_All("Edit All Courses", "COURSE"),
    Course_Delete_All("Delete All Courses", "COURSE"),
    Course_Publish_All("Publish All Courses", "COURSE"),
    
    // ========================================
    // RECORD-LEVEL ARTICLE PERMISSIONS
    // ========================================
    
    // Own Article Permissions
    Article_View_Own("View Own Articles", "ARTICLE"),
    Article_Edit_Own("Edit Own Articles", "ARTICLE"),
    Article_Delete_Own("Delete Own Articles", "ARTICLE"),
    Article_Publish_Own("Publish Own Articles", "ARTICLE"),
    
    // All Article Permissions
    Article_View_All("View All Articles", "ARTICLE"),
    Article_Edit_All("Edit All Articles", "ARTICLE"),
    Article_Delete_All("Delete All Articles", "ARTICLE"),
    Article_Publish_All("Publish All Articles", "ARTICLE"),
    
    // ========================================
    // RECORD-LEVEL EVENT PERMISSIONS
    // ========================================
    
    // Own Event Permissions
    Event_View_Own("View Own Events", "EVENT"),
    Event_Edit_Own("Edit Own Events", "EVENT"),
    Event_Delete_Own("Delete Own Events", "EVENT"),
    Event_Publish_Own("Publish Own Events", "EVENT"),
    
    // All Event Permissions
    Event_View_All("View All Events", "EVENT"),
    Event_Edit_All("Edit All Events", "EVENT"),
    Event_Delete_All("Delete All Events", "EVENT"),
    Event_Publish_All("Publish All Events", "EVENT");

    private final String name;
    private final String module;

    PermissionConstant(String name, String module) {
        this.name = name;
        this.module = module;
    }

    // ========================================
    // PERMISSION CATEGORY UTILITY METHODS
    // ========================================
    
    /**
     * Check if this permission is a system administration permission
     */
    public boolean isSystemAdminPermission() {
        return this.name().startsWith("Manage_") && 
               (this.name().equals("Manage_Users") || 
                this.name().equals("Manage_System_Settings") || 
                this.name().equals("Manage_Message_Templates") || 
                this.name().equals("Manage_Lookups"));
    }
    
    /**
     * Check if this permission is a content management permission
     */
    public boolean isContentManagementPermission() {
        return this.name().equals("Manage_Categories") || 
               this.name().equals("Manage_Subscription_Plans") ||
               this.name().endsWith("_Create");
    }
    
    /**
     * Check if this permission is a user management permission
     */
    public boolean isUserManagementPermission() {
        return this.name().equals("Manage_Students") || 
               this.name().equals("Manage_Instructors") || 
               this.name().equals("Manage_Organisations");
    }
    
    /**
     * Check if this permission is a content moderation permission
     */
    public boolean isContentModerationPermission() {
        return this.name().equals("Manage_Reviews");
    }
    
    /**
     * Check if this permission is a record-level permission
     */
    public boolean isRecordLevel() {
        return this.name().contains("_Own") || this.name().contains("_All");
    }
    
    /**
     * Check if this permission is a creation permission
     */
    public boolean isCreationPermission() {
        return this.name().endsWith("_Create");
    }

    /**
     * Get the base permission name (without _Own or _All suffix)
     */
    public String getBasePermission() {
        if (this.name().endsWith("_Own") || this.name().endsWith("_All")) {
            return this.name().substring(0, this.name().lastIndexOf("_"));
        }
        return this.name();
    }

    /**
     * Check if this is an "Own" permission
     */
    public boolean isOwnPermission() {
        return this.name().endsWith("_Own");
    }

    /**
     * Check if this is an "All" permission
     */
    public boolean isAllPermission() {
        return this.name().endsWith("_All");
    }

    /**
     * Get the resource type from permission name
     */
    public String getResourceType() {
        if (this.name().contains("_")) {
            return this.name().split("_")[0];
        }
        return this.name();
    }
    
    /**
     * Get the action type from permission name
     */
    public String getActionType() {
        if (this.name().contains("_")) {
            String[] parts = this.name().split("_");
            if (parts.length >= 2) {
                return parts[1]; // View, Edit, Delete, Publish, Create, Manage
            }
        }
        return this.name();
    }
    
    /**
     * Get the scope type from permission name
     */
    public String getScopeType() {
        if (this.name().endsWith("_Own")) {
            return "Own";
        } else if (this.name().endsWith("_All")) {
            return "All";
        } else if (this.name().startsWith("Manage_")) {
            return "System";
        } else if (this.name().endsWith("_Create")) {
            return "Create";
        }
        return "General";
    }
    
    /**
     * Check if permission belongs to a specific module
     */
    public boolean belongsToModule(String module) {
        return this.module.equals(module);
    }
    
    /**
     * Get all permissions for a specific resource type
     */
    public static PermissionConstant[] getPermissionsForResource(String resourceType) {
        return java.util.Arrays.stream(values())
                .filter(p -> p.getResourceType().equals(resourceType))
                .toArray(PermissionConstant[]::new);
    }
    
    /**
     * Get all permissions for a specific action type
     */
    public static PermissionConstant[] getPermissionsForAction(String actionType) {
        return java.util.Arrays.stream(values())
                .filter(p -> p.getActionType().equals(actionType))
                .toArray(PermissionConstant[]::new);
    }
    
    /**
     * Get all permissions for a specific scope type
     */
    public static PermissionConstant[] getPermissionsForScope(String scopeType) {
        return java.util.Arrays.stream(values())
                .filter(p -> p.getScopeType().equals(scopeType))
                .toArray(PermissionConstant[]::new);
    }
    
    /**
     * Get all permissions for a specific module
     */
    public static PermissionConstant[] getPermissionsForModule(String module) {
        return java.util.Arrays.stream(values())
                .filter(p -> p.belongsToModule(module))
                .toArray(PermissionConstant[]::new);
    }
    
    /**
     * Get all system-level permissions
     */
    public static PermissionConstant[] getSystemPermissions() {
        return java.util.Arrays.stream(values())
                .filter(p -> !p.isRecordLevel() && !p.isCreationPermission())
                .toArray(PermissionConstant[]::new);
    }
    
    /**
     * Get all record-level permissions
     */
    public static PermissionConstant[] getRecordLevelPermissions() {
        return java.util.Arrays.stream(values())
                .filter(PermissionConstant::isRecordLevel)
                .toArray(PermissionConstant[]::new);
    }
    
    /**
     * Get all creation permissions
     */
    public static PermissionConstant[] getCreationPermissions() {
        return java.util.Arrays.stream(values())
                .filter(PermissionConstant::isCreationPermission)
                .toArray(PermissionConstant[]::new);
    }
    
    /**
     * Get all available modules
     */
    public static String[] getAvailableModules() {
        return java.util.Arrays.stream(values())
                .map(PermissionConstant::getModule)
                .distinct()
                .toArray(String[]::new);
    }
    
    /**
     * Get module display name
     */
    public String getModuleDisplayName() {
        switch (this.module) {
            case "SYSTEM": return "System Administration";
            case "CONTENT": return "Content Management";
            case "USER_MANAGEMENT": return "User Management";
            case "MODERATION": return "Content Moderation";
            case "COURSE": return "Course Management";
            case "ARTICLE": return "Article Management";
            case "EVENT": return "Event Management";
            default: return this.module;
        }
    }
}
