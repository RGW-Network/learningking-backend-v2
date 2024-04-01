package com.byaffe.learningking.shared.constants;

public enum PermissionConstant {
    ADD_USERS(1,"Create/Update Users",PermissionModule.USERS),
    ADD_ROLE(2,"Create/Update Roles",PermissionModule.USERS),
    ADD_SHOP_MANAGER(3,"Create/Update Shop Managers",PermissionModule.SHOP_MANAGERS),
    ADD_SHOP(4,"Create/Update Any Shop",PermissionModule.SHOPS),

    MANAGE_MY_SHOP(5,"Create/Update My Shop Details",PermissionModule.SHOPS),
    MANAGE_MY_SHOP_ATTENDANTS(6,"Create/Update My Shop Attendants",PermissionModule.SHOPS),
    MANAGE_MY_SHOP_STOCK(7,"Create/Update My Shop Details",PermissionModule.SHOPS),
    REGISTER_SHOP_SALES(8,"Register Shop Sales",PermissionModule.SHOPS),


    ;
    private String name;
    private long id;
    private PermissionModule permissionModule;

    PermissionConstant(long id,String name, PermissionModule permissionModule) {
        this.id=id;
        this.name = name;
        this.permissionModule = permissionModule;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public PermissionModule getPermissionModule() {
        return permissionModule;
    }

    public static PermissionConstant getById(long id){
        for(PermissionConstant permissionConstant:PermissionConstant.values()){
            if(permissionConstant.id==id){
                return permissionConstant;
            }
        }
        return null;
    }
}
