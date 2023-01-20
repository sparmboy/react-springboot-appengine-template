package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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

    public final static String TABLE_NAME = "USER_ROLES";

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
        name = "ROLES_TO_PRIVILEGES",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<UserPrivilegeEntity> privileges;

    public UserRoleEntity(String name, Collection<UserPrivilegeEntity> privileges) {
        this.name = name;
        this.privileges = privileges;
    }
}
