package com.example.security;

import com.example.domain.UserEntity;
import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@Builder
public class UserPrincipal implements OAuth2User, UserDetails, OidcUser {
    private String id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private boolean enabled;


    public static UserPrincipal create(UserEntity userEntity,Collection<? extends GrantedAuthority> authorities) {
        return UserPrincipal.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(authorities)
                .enabled(Boolean.TRUE.equals(userEntity.getEmailVerified()))
                .build();
    }

    public static UserPrincipal create(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities,Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(userEntity,authorities);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
