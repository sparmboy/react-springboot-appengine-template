package com.example.dao.repository;

import com.example.domain.UserPermissionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPermissionEntity, String> {
    Optional<UserPermissionEntity> findByName(String name);
}
