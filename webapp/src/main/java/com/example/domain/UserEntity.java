package com.example.domain;

import com.example.domain.enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Collection;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = UserEntity.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
public class UserEntity {

    public final static String TABLE_NAME = "USERS";

    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @ManyToMany
    @JoinTable(
        name = "USER_TO_ROLES",
        joinColumns = @JoinColumn(
            name = "USER_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(
            name = "ROLE_ID", referencedColumnName = "ID"))
    private Collection<UserRoleEntity> roles;
}
