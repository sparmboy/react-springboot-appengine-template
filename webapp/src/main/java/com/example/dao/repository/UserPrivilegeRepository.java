package com.example.dao.repository;

import com.example.domain.UserPrivilegeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilegeEntity, String> {
    Optional<UserPrivilegeEntity> findByName(String name);
}
