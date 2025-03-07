package backend.controlers;

import backend.domain.dto.RegisterUserDto;
import backend.domain.services.RegisterService;
import backend.rdb.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegisterController {
    private final RegisterService registerService;
    private final UserRepository userRepository;

    @GetMapping("/confirm")
    public Mono<ResponseEntity<String>> confirmRegistration(@RequestParam String confirmationToken) {
        return userRepository.findByConfirmToken(confirmationToken)
                .flatMap(user -> {
                    user.setEnabled(true);
                    return userRepository.save(user);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid confirmation token")))
                .then(Mono.just(ResponseEntity.ok("Registration confirmed successfully")));
    }
    @PostMapping
    public Mono<Void> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
       return registerService.register(registerUserDto)
               .then();
    }
}