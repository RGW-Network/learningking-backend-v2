package com.byaffe.microtasks.shared.constants;

public enum PermissionModule {
    LOOKUPS("Lookups"),
    USERS("System Users"),
    SHOP_MANAGERS("Shop Managers"),
    SHOPS("Shops"),
    TRANSACTIONS("Transactions");

    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    PermissionModule(String displayName) {
        this.displayName = displayName;
    }
}
