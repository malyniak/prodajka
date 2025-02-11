package backend.logic;

import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtil jwtUtil;
    private final UserService userService;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getPrincipal() != null
                ? authentication.getPrincipal().toString() : null;
        String username = jwtUtil.getUsernameFromToken(token);
        if (token == null || !jwtUtil.validateToken(token)) {

            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            });
        }
        return userService.findByEmail(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
                .map(user -> new UsernamePasswordAuthenticationToken(username, null, List.of()));
    }
}
