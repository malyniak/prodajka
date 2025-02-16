package backend.logic;

import backend.rdb.entities.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String refreshHeader = exchange.getRequest().getHeaders().getFirst("Refresh-Token");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String accessToken = authHeader.substring(7);
        String refreshToken = (refreshHeader != null && refreshHeader.startsWith("Bearer ")) ? refreshHeader.substring(7) : null;

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(accessToken, refreshToken, List.of(UserRole.USER));
        return authenticationManager.authenticate(authentication)
                .doOnSuccess(auth -> System.out.println("Authentication success: " + auth))
                .doOnError(error -> System.out.println("Authentication error: " + error.getMessage()))
                .flatMap(auth -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
    }

}