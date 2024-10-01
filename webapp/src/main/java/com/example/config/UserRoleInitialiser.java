package com.example.config;

import static com.example.constants.RoleAndPrivilegeConstants.PRIVILEGE_READ;
import static com.example.constants.RoleAndPrivilegeConstants.PRIVILEGE_WRITE;
import static com.example.constants.RoleAndPrivilegeConstants.ROLE_ADMIN;
import static com.example.constants.RoleAndPrivilegeConstants.ROLE_USER;

import com.example.dao.repository.UserPrivilegeRepository;
import com.example.dao.repository.UserRepository;
import com.example.dao.repository.UserRoleRepository;
import com.example.domain.UserEntity;
import com.example.domain.UserPermissionEntity;
import com.example.domain.UserRoleEntity;
import com.example.domain.exceptions.ResourceNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
@Transactional
public class UserRoleInitialiser implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    final UserRepository userRepository;
    final UserRoleRepository userRoleRepository;
    final UserPrivilegeRepository userPrivilegeRepository;
    final PasswordEncoder passwordEncoder;

    public UserRoleInitialiser(UserRepository userRepository, UserRoleRepository userRoleRepository, UserPrivilegeRepository userPrivilegeRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userPrivilegeRepository = userPrivilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }
        UserPermissionEntity readPrivilege = createPrivilegeIfNotFound(PRIVILEGE_READ);
        UserPermissionEntity writePrivilege = createPrivilegeIfNotFound(PRIVILEGE_WRITE);

        List<UserPermissionEntity> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound(ROLE_ADMIN, adminPrivileges);
        createRoleIfNotFound(ROLE_USER, Collections.singletonList(readPrivilege));

        UserRoleEntity adminRole = userRoleRepository.findByName(ROLE_ADMIN).orElseThrow(() -> new ResourceNotFoundException(UserRoleEntity.class.getName(), "name", ROLE_ADMIN));
        UserRoleEntity userRole = userRoleRepository.findByName(ROLE_USER).orElseThrow(() -> new ResourceNotFoundException(UserRoleEntity.class.getName(), "name", ROLE_USER));

        UserEntity adminUser = createAdmin(Arrays.asList(userRole,adminRole));
        UserEntity defaultUser = createUser(Collections.singletonList(userRole));

        if(userRepository.findByEmail(adminUser.getEmail()).isEmpty()) {
            userRepository.save(adminUser);
        }

        if(userRepository.findByEmail(defaultUser.getEmail()).isEmpty()) {
            userRepository.save(defaultUser);
        }


        alreadySetup = true;
    }

    private UserEntity createAdmin(List<UserRoleEntity> roles) {
        UserEntity user = new UserEntity();
        user.setName("Admin");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("admin@admin.com");
        user.setEmailVerified(true);
        user.setRoles(roles);
        return user;
    }

    private UserEntity createUser(List<UserRoleEntity> roles) {
        UserEntity user = new UserEntity();
        user.setName("User 1");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("user@user.com");
        user.setEmailVerified(true);
        user.setRoles(roles);
        return user;
    }

    @Transactional
    UserPermissionEntity createPrivilegeIfNotFound(String name) {
        return userPrivilegeRepository.findByName(name).orElseGet(()-> userPrivilegeRepository.save(new UserPermissionEntity(name)));
    }

    @Transactional
    UserRoleEntity createRoleIfNotFound(String name, Collection<UserPermissionEntity> privileges) {
        return userRoleRepository.findByName(name).orElseGet(()->userRoleRepository.save( new UserRoleEntity(name,privileges) ) );
    }

}
