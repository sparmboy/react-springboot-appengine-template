package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = UserRoleEntity.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class UserRoleEntity extends AbstractBaseEntity{

    public final static String TABLE_NAME = "USER_ROLES";

    @Id
    @UuidGenerator
    private String id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<UserEntity> users;

    @ManyToMany
    @JoinTable(
        name = "ROLES_TO_PRIVILEGES",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<UserPrivilegeEntity> privileges;

    public UserRoleEntity(String name, Collection<UserPrivilegeEntity> privileges) {
        this.name = name;
        this.privileges = privileges;
    }
}
