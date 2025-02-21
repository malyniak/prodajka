package backend.controlers;

import backend.dto.RegisterUserDto;
import backend.logic.AuthRequest;
import backend.logic.AuthResponse;
import backend.logic.JwtUtil;
import backend.rdb.entities.UserEntity;
import backend.services.UserService;
import backend.util.PasswordEncoderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public Mono<UserEntity> registerUser(@RequestBody RegisterUserDto user) {
        return userService.create(user);
    }

    @GetMapping("/users")
    public Flux<RegisterUserDto> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return userService.findByEmail(authRequest.getEmail())
                .flatMap(userEntity -> {
                    if (PasswordEncoderImpl.passwordEncoder().matches(authRequest.getPassword(), userEntity.getPassword())) {
                        String accessToken = jwtUtil.generateAccessToken(userEntity.getEmail());
                        String refreshToken = jwtUtil.generateRefreshToken(userEntity.getEmail());
                        return Mono.just(new AuthResponse(accessToken, refreshToken));
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid email or password"));
                    }
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid email or password")));
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<AuthResponse>> refresh(@RequestHeader("Authorization") String refreshToken) {
        String refresh = refreshToken.substring(7);
        if (!jwtUtil.validateToken(refresh)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        }

        String username = jwtUtil.getUsernameFromToken(refresh);

        return userService.findByEmail(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")))
                .map(user -> {
                    String newAccessToken = jwtUtil.generateAccessToken(username);
                    return ResponseEntity.ok(new AuthResponse(newAccessToken, refresh));
                });
    }
}
