package com.example.utils;

import com.example.domain.UserPermissionEntity;
import com.example.domain.UserRoleEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserRoleUtils {

    public static Collection<? extends GrantedAuthority> getAuthorities(Collection<UserRoleEntity> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private static List<String> getPrivileges(Collection<UserRoleEntity> roles) {

        List<String> privileges = new ArrayList<>();
        List<UserPermissionEntity> collection = new ArrayList<>();
        for (UserRoleEntity role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPermissions());
        }
        for (UserPermissionEntity item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private static List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
