package com.example.controller;

import com.example.controllers.LoginControllerApiDelegate;
import com.example.dao.repository.UserRepository;
import com.example.domain.enums.AuthProvider;
import com.example.domain.UserEntity;
import com.example.domain.exceptions.BadRequestException;
import com.example.model.AuthResponse;
import com.example.model.LoginRequest;
import com.example.model.OauthUrl;
import com.example.model.SignupRequest;
import com.example.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginControllerApiDelegateImpl implements LoginControllerApiDelegate {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    private static String authorizationRequestBaseUri
            = "/api/oauth2/authorize";
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();

    @Value("${baseUrl}")
    private String baseUrl;


    @Override
    public ResponseEntity<List<OauthUrl>> getOauthUrls() {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId() + "?redirect_uri=" + baseUrl + "/oauth2" ));

        return ResponseEntity.ok(oauth2AuthenticationUrls.keySet().stream().map(k->new OauthUrl().key(k).href(oauth2AuthenticationUrls.get(k))).collect(Collectors.toList()) );
    }

    @Override
    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse().accessToken(token));
    }

    @Override
    public ResponseEntity<String> registerUser(SignupRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating userEntity's account
        UserEntity userEntity = new UserEntity();
        userEntity.setName(signUpRequest.getName());
        userEntity.setEmail(signUpRequest.getEmail());
        userEntity.setPassword(signUpRequest.getPassword());
        userEntity.setProvider(AuthProvider.local);

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        UserEntity result = userRepository.save(userEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/userEntity/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body( "UserEntity registered successfully");
    }
}
