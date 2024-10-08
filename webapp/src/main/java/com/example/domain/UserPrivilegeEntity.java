package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = UserPrivilegeEntity.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class UserPrivilegeEntity extends AbstractBaseEntity{

    public final static String TABLE_NAME = "USER_PRIVILEGES";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name="UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private String name;

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private Collection<UserRoleEntity> roles;

    public UserPrivilegeEntity(String name) {
        this.name = name;
    }
}
