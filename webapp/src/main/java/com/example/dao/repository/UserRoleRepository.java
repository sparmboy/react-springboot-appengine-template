package com.example.dao.repository;

import com.example.domain.UserRoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
    Optional<UserRoleEntity> findByName(String roleName);
}
