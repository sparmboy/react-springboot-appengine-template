package com.example.domain;

import com.example.domain.enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.Optional;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name="UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(name="IMAGE_URL")
    private String imageUrl;

    @Column(nullable = false,name = "EMAIL_VERIFIED")
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name="PROVIDER_ID")
    private String providerId;

    @ManyToMany
    @JoinTable(
        name = "USER_ROLES",
        joinColumns = @JoinColumn(
            name = "USER_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(
            name = "ROLE_ID", referencedColumnName = "ID"))
    private Collection<UserRoleEntity> roles;
}
