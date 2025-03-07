package backend.domain.logic;

import backend.rdb.entities.enums.UserRole;
import backend.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
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
        String accessToken = authentication.getCredentials() != null
                ? authentication.getCredentials().toString()
                : null;

        if (accessToken == null || !jwtUtil.validateToken(accessToken)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid access token"));
        }

        String username = jwtUtil.getUsernameFromToken(accessToken);

        return userService.findByEmail(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")))
                .map(user -> new JwtAuthenticationToken(accessToken, List.of(UserRole.USER)));
    }
}
