package com.example.config;

import static com.example.config.WebSocketConfig.TOPIC_MY_EVENT;
import static com.example.config.WebSocketConfig.TOPIC_PREFIX;
import static com.example.config.WebSocketConfig.WEBSOCKETS_ENDPOINT;
import static org.springframework.security.config.Customizer.withDefaults;

import com.example.security.CustomUserDetailsService;
import com.example.security.RestAuthenticationEntryPoint;
import com.example.security.TokenAuthenticationFilter;
import com.example.security.oauth2.CustomOAuth2OidcUserService;
import com.example.security.oauth2.CustomOAuth2UserService;
import com.example.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.security.oauth2.OAuth2AuthenticationSuccessHandler;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    public static final List<String> SWAGGER_ROUTES = Arrays.asList(
        "/swagger-ui.html",
        "/webjars/springfox-swagger-ui", "/swagger-resources", "/null/swagger-resources", "/v2/api-docs", "/actuator");

    public static final List<String> UNSECURED_PATHS = Arrays.asList("/home",
        "/login",
        "/signin",
        "/signup",
        "/signup-success",
        "/oauth2",
        "/api/user/**",
        "/api/auth/**",
        "/api/v1/messages/sms/status",
        "/",
        "/*.json",
        "/*.js",
        "/**/*.png",
        "/**/*.gif",
        "/**/*.svg",
        "/**/*.jpg",
        "/**/*.html",
        "/**/*.css",
        "/**/*.js",
        "/error",
        "/favicon.ico",
        "/static/**/*",
        "/static/*"
    );

    private final static List<String> SECURED_PATHS = Arrays.asList(
        "/",
        "/error",
        "/favicon.ico",
        "/static/**/*",
        "/static/*",
        "/oauth2",
        "/api/oauth2",
        "/api/oauth2/**",
        "/home",
        "/login",
        "/signin",
        "/*.json",
        "/*.js",
        "/**/*.png",
        "/**/*.gif",
        "/**/*.svg",
        "/**/*.jpg",
        "/**/*.html",
        "/**/*.css",
        "/**/*.js",
        "/app/v2/api-docs",
        "/api/v1/auth/**",
        "/api/v1/orders/**",
        WEBSOCKETS_ENDPOINT + "/**",
        TOPIC_PREFIX,
        TOPIC_MY_EVENT
    );


    public static final List<String> ANON_PATHS = Stream.concat(
        SWAGGER_ROUTES.stream().map(r -> r + "/**"),
        UNSECURED_PATHS.stream()
    ).collect(Collectors.toList());


    private final CustomUserDetailsService customUserDetailsService;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomOAuth2OidcUserService customOAuth2OidcUserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) throws Exception {
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
        CustomUserDetailsService userDetailsService,
        PasswordEncoder encoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Value("${spring.security.oauth2.client.registration.apple.keyId}")
    private String appleKeyId;

    @Value("${spring.security.oauth2.client.registration.apple.teamId}")
    private String appleTeamId;

    @Value("${spring.security.oauth2.client.registration.apple.clientId}")
    private String clientId;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(withDefaults())
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new RestAuthenticationEntryPoint()))

            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(ANON_PATHS.stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new))
                .permitAll()
            )

            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(SECURED_PATHS.stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new))
                .permitAll()
                .anyRequest()
                .authenticated()

            )
            .oauth2Login(oauth2 -> oauth2.authorizationEndpoint(customizer -> customizer
                    .baseUri("/api/oauth2/authorize")
                    .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
                    .baseUri("/api/oauth2/callback/*")
                )
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOAuth2OidcUserService)

                )
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
            );


        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
