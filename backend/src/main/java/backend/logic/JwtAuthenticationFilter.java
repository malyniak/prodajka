package backend.logic;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, JwtAuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String refreshToken = exchange.getRequest().getHeaders().getFirst("Refresh-Token");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
                return refreshAccessToken(refreshToken, exchange, chain);
            } else {

                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tokens are invalid"));
            }
        }
        String username = jwtUtil.getUsernameFromToken(token);
        Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);

        return authenticationManager.authenticate(authentication)
                .flatMap(auth -> {
                    SecurityContext context = new SecurityContextImpl(auth);
                    return securityContextRepository.save(exchange, context)
                            .then(chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context))));
                });
    }


    private Mono<Void> refreshAccessToken(String refreshToken, ServerWebExchange exchange, WebFilterChain chain) {
        String email = jwtUtil.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(email);
        exchange.getResponse().getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newAccessToken, newAccessToken);
        return authenticationManager.authenticate(authentication)
                .doOnTerminate(() -> {
                    securityContextRepository.save(exchange, new SecurityContextImpl(authentication));
                })
                .flatMap(auth -> chain.filter(exchange));
    }
}
