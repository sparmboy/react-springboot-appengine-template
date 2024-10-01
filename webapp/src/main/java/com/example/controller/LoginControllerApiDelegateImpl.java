package com.example.controller;

import static com.example.constants.RoleAndPrivilegeConstants.ROLE_USER;

import com.example.controllers.LoginControllerApiDelegate;
import com.example.dao.repository.UserRepository;
import com.example.dao.repository.UserRoleRepository;
import com.example.domain.UserEntity;
import com.example.domain.UserRoleEntity;
import com.example.domain.enums.AuthProvider;
import com.example.domain.exceptions.BadRequestException;
import com.example.domain.exceptions.ResourceNotFoundException;
import com.example.domain.mappers.UserMapper;
import com.example.model.AuthResponse;
import com.example.model.LoginRequest;
import com.example.model.OauthUrl;
import com.example.model.SignupRequest;
import com.example.security.TokenProvider;
import com.example.security.UserPrincipal;
import com.example.services.EmailService;
import com.example.utils.PropertyDecryptionUtil;
import com.example.utils.PropertyEncryptionUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Slf4j
public class LoginControllerApiDelegateImpl implements LoginControllerApiDelegate {

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final UserRoleRepository userRoleRepository;

    private final EmailService emailClient;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private static String authorizationRequestBaseUri
        = "/api/oauth2/authorize";
    Map<String, String> oauth2AuthenticationUrls
        = new HashMap<>();

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${baseUrl}")
    private String baseUrl;

    public LoginControllerApiDelegateImpl(ClientRegistrationRepository clientRegistrationRepository,
                                          AuthenticationManager authenticationManager,
                                          UserRepository userRepository,
                                          PasswordEncoder passwordEncoder,
                                          TokenProvider tokenProvider,
                                          UserRoleRepository userRoleRepository,
                                          EmailService emailClient) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userRoleRepository = userRoleRepository;
        this.emailClient = emailClient;
    }

    @Override
    public ResponseEntity<List<OauthUrl>> getOauthUrls() {
        Iterable<ClientRegistration> clientRegistrations;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
            .as(Iterable.class);
        if (type != ResolvableType.NONE &&
            ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
            clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                    authorizationRequestBaseUri + "/" + registration.getRegistrationId() + "?redirect_uri=" + baseUrl + "/oauth2" ));
        }


        return ResponseEntity.ok(oauth2AuthenticationUrls.keySet().stream().map(k -> new OauthUrl().key(k).href(oauth2AuthenticationUrls.get(k))).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {
        log.debug("Attempting to login {}",loginRequest);
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();
        UserEntity userEntity = userRepository.findById(principal.getId()).orElseThrow(()->new ResourceNotFoundException(UserEntity.class.getName(),"id",principal.getId()));

        return ResponseEntity.ok(new AuthResponse()
            .accessToken(token)
            .user(userMapper.map(userEntity))
        );
    }

    @Override
    public ResponseEntity<String> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }
        UserRoleEntity userRole = userRoleRepository.findByName(ROLE_USER).orElseThrow(() -> new ResourceNotFoundException(UserRoleEntity.class, "name", ROLE_USER));

        // Creating userEntity's account
        UserEntity userEntity = new UserEntity();
        //userEntity.setName(signUpRequest.getName());
        userEntity.setEmail(signUpRequest.getEmail());
        userEntity.setPassword(signUpRequest.getPassword());
        userEntity.setProvider(AuthProvider.local);
        userEntity.setRoles(Collections.singletonList(userRole));
        userEntity.setEmailVerified(false);

        try {
            URL verifyEmailUrl = generateVerifyEmailUrl(signUpRequest.getEmail());
            log.info("Generated verification email: {}", verifyEmailUrl);

            emailClient.sendVerificationEmail(signUpRequest,verifyEmailUrl.toString());

            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

            UserEntity result = userRepository.save(userEntity);

            URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/userEntity/me")
                .buildAndExpand(result.getId()).toUri();

            return ResponseEntity.created(location)
                .body("UserEntity registered successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> verifyEmail(Optional<String> q) {
        if (q.isPresent()) {
            String email = PropertyDecryptionUtil.decrypt(new String(Base64.getDecoder().decode(q.get().getBytes(StandardCharsets.UTF_8))), tokenSecret);
            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(UserEntity.class,"email",email));
            user.setEmailVerified(true);
            user = userRepository.save(user);
            log.info("User {} successfully verified email.",user.getId());
            try {
                return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(new URI("/signin")).build();
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private URL generateVerifyEmailUrl(String email) throws MalformedURLException {
        String q = Base64.getEncoder().encodeToString(PropertyEncryptionUtil.encrypt(email, tokenSecret).getBytes(StandardCharsets.UTF_8));
        return new URL(baseUrl + "/api/v1/auth/verify?q=" + q);
    }

    @Override
    public ResponseEntity<Void> logOut() {
        HttpServletRequest request =
            ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
        return ResponseEntity.ok().build();
    }
}
