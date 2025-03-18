package doancuoiki.db_cnpm.QuanLyNhaSach.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.config.Customizer;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResLoginDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.SecurityUtil;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguaration {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;
    // private final OAuth2AuthenticationSuccessHandler
    // oAuth2AuthenticationSuccessHandler;

    // private final CorsConfigurationSource corsConfigurationSource;

    @Value("${nqoctai.jwt.base64-secret}")
    private String jwtKey;

    public SecurityConfiguaration(
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.oauth2UserService = oauth2UserService;
        // this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] whiteList = {
                "/",
                "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/register",
                "/test", "/storage/**", "/api/v1/files", "/api/v1/categories",
                "/api/v1/account", "/api/v1/book/**", "/api/v1/books/**",
                "/api/v1/payment/**", "/api/v1/auth/login/oauth2/github",
                "/login/oauth2/**", "/oauth2/**" // Add these paths to whitelist
        };

        http
                .csrf(c -> c.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers(whiteList).permitAll()
                                .anyRequest().authenticated())
                .formLogin(f -> f.disable())
                // .oauth2Login(oauth2 -> oauth2
                // .authorizationEndpoint(auth -> auth.baseUri("/oauth2/authorize"))
                // .redirectionEndpoint(redirect -> redirect.baseUri("/login/oauth2/code/*"))
                // .userInfoEndpoint(userInfo -> userInfo
                // .userService(oauth2UserService))
                // .successHandler(oAuth2AuthenticationSuccessHandler)
                // // .defaultSuccessUrl("http://localhost:3000/oauth2/redirect", true)
                // .failureHandler((request, response, exception) -> {
                // // Handle OAuth2 login failure
                // System.err.println("OAuth2 login failed: " + exception.getMessage());
                // response.sendRedirect("/login?error=oauth2_failure");
                // }))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }

}
