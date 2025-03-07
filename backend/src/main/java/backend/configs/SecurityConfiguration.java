package backend.configs;


import backend.domain.dto.RegisterUserDto;
import backend.domain.logic.JwtAuthenticationFilter;
import backend.domain.logic.JwtAuthenticationManager;

import backend.domain.logic.JwtUtil;
import backend.domain.services.RegisterService;
import backend.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec
                            .pathMatchers("/login", "/registration/**", "/refresh", "/oauth2/**").permitAll()
                            .anyExchange().authenticated();
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .oauth2Login(oauth2 -> oauth2.authenticationSuccessHandler(

                        successHandler(jwtUtil)
                ))
                .build();
    }

    private CorsConfigurationSource corsConfiguration() {
        CorsConfiguration apiConfiguration = new CorsConfiguration();
        apiConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        apiConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        apiConfiguration.setAllowedHeaders(List.of("*"));
        apiConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", apiConfiguration);
        return source;
    }

    @Bean
    public ServerAuthenticationSuccessHandler successHandler(JwtUtil jwtUtil) {
        return (webFilterExchange, authentication) -> {
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");


            if (email == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не знайдено"));
            }

            return userService.findByEmail(email)
                    .switchIfEmpty(userService.create(RegisterUserDto.builder().email(email).build()))
                    .flatMap((__) -> {

                        String accessToken = jwtUtil.generateAccessToken(email);
                        String refreshToken = jwtUtil.generateRefreshToken(email);

                        response.getHeaders().add("Authorization", "Bearer " + accessToken);
                        response.getHeaders().add("Refresh-Token", refreshToken);
                        response.setStatusCode(HttpStatus.FOUND);
                        response.getHeaders().setCacheControl("no-cache");
                        response.getHeaders().setLocation(URI.create("http://localhost:4200/login-callback?accessToken=" +
                                accessToken + "&refreshToken=" + refreshToken));

                        return response.setComplete();
                    });
        };
    }
}