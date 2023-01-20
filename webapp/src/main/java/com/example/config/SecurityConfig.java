package com.example.config;

import static com.example.config.WebSocketConfig.TOPIC_MY_EVENT;
import static com.example.config.WebSocketConfig.TOPIC_PREFIX;
import static com.example.config.WebSocketConfig.WEBSOCKETS_ENDPOINT;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final List<String> SWAGGER_ROUTES = Arrays.asList(
        "/swagger-ui.html",
        "/webjars/springfox-swagger-ui", "/swagger-resources", "/null/swagger-resources", "/v2/api-docs","/actuator");

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
        WEBSOCKETS_ENDPOINT +"/**",
        TOPIC_PREFIX,
        TOPIC_MY_EVENT
    );


    public static final List<String> ANON_PATHS = Stream.concat(
        SWAGGER_ROUTES.stream().map(r -> r + "/**"),
        UNSECURED_PATHS.stream()
    ).collect(Collectors.toList());


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomOAuth2OidcUserService customOAuth2OidcUserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Value("${spring.security.oauth2.client.registration.apple.keyId}")
    private String appleKeyId;

    @Value("${spring.security.oauth2.client.registration.apple.teamId}")
    private String appleTeamId;

    @Value("${spring.security.oauth2.client.registration.apple.clientId}")
    private String clientId;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .httpBasic()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers(ANON_PATHS.toArray(new String[] {}))
            .anonymous()
            .and().authorizeRequests()
            .antMatchers(SECURED_PATHS.toArray(new String[] {}))
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/api/oauth2/authorize")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
            .redirectionEndpoint()
            .baseUri("/api/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .oidcUserService(customOAuth2OidcUserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler);

        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}