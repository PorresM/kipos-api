package fr.mporres.kiposapi.enums;

import java.util.Arrays;
import java.util.List;

public enum UserRole {

    ADMIN(Arrays.asList(
        Permission.USER_CREATE,
        Permission.USER_DELETE,
        Permission.USER_GET,
        Permission.USER_GET_SELF
    )),
    USER(Arrays.asList(
        Permission.USER_GET_SELF
    ));

    private List<Permission> permissionList;

    UserRole(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

}
