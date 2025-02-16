package backend.logic;

import backend.rdb.entities.enums.UserRole;
import backend.services.UserService;
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
                ? authentication.getCredentials().toString() : null;
        String refreshToken = authentication instanceof JwtAuthenticationToken
                ? ((JwtAuthenticationToken) authentication).getRefreshToken()
                : null;
        String username = jwtUtil.getUsernameFromToken(accessToken);
        if (username == null || !jwtUtil.validateToken(accessToken)) {
            username = refreshToken != null ? jwtUtil.getUsernameFromToken(refreshToken) : null;
            String newAccessToken = jwtUtil.generateAccessToken(username);
            return userService.findByEmail(username)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
                    .map(user -> {
                        return new JwtAuthenticationToken(newAccessToken, refreshToken, List.of(UserRole.USER));
                    });
        }
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token"));
        }
        return userService.findByEmail(username)
                .doOnSuccess(user -> System.out.println("User found: " + user.getEmail()))
                .map(user -> {
                    return new JwtAuthenticationToken(accessToken, refreshToken, List.of(UserRole.USER));
                });
    }


}
