package backend.logic;

import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtil jwtUtil;
    private final UserService userService;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(token);

        return userService.findByEmail(username)
                .<Authentication>handle((userEntity, sink) -> {
                    if (!jwtUtil.validateToken(token, userEntity.getEmail())) {
                        sink.next(authentication);
                    } else {
                        sink.error(new AuthenticationException("Invalid JWT token") {
                            @Override
                            public String getMessage() {
                                return super.getMessage();
                            }
                        });

                    }
                });
    }

    public ServerAuthenticationConverter authenticationConverter() {
        return exchange -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                return Mono.just(SecurityContextHolder.getContext().getAuthentication());
            }
            return Mono.empty();
        };
    }
}
