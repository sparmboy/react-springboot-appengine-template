package com.example.security;

import com.example.config.ApplicationConfig;
import com.example.domain.exceptions.OAuth2AuthenticationProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenProvider {
    
    private ApplicationConfig appProperties;

    public TokenProvider(ApplicationConfig appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();


        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private DefaultOidcUser mapPrincipal(Object principal) {
        if( principal instanceof DefaultOidcUser ) {
            return (DefaultOidcUser)principal;
        }else {
            throw new OAuth2AuthenticationProcessingException("Unidentified principal class " + principal.getClass());
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}",ex.getLocalizedMessage(),ex);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}",ex.getLocalizedMessage(),ex);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}",ex.getLocalizedMessage(),ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}",ex.getLocalizedMessage(),ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}",ex.getLocalizedMessage(),ex);
        }
        return false;
    }

}
