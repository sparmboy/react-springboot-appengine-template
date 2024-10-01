package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = UserRoleEntity.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class UserRoleEntity extends AbstractBaseEntity{

    public final static String TABLE_NAME = "ROLE";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<UserEntity> users;

    @ManyToMany
    @JoinTable(
        name = "ROLE_PERMISSIONS",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Collection<UserPermissionEntity> permissions;

    public UserRoleEntity(String name, Collection<UserPermissionEntity> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}
