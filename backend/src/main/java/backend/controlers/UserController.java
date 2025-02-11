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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

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
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                    }
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    }

}
