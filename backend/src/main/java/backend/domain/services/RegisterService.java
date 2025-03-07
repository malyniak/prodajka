package backend.domain.services;

import backend.domain.dto.RegisterUserDto;
import backend.rdb.entities.UserEntity;
import backend.rdb.entities.enums.UserRole;
import backend.rdb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final static String CONFIRMATION_LINK = "http://localhost:8080/registration/confirm?confirmationToken=";


    public Mono<Void> register(RegisterUserDto registerUserDto) {
        return userRepository.findByEmail(registerUserDto.getEmail())
                .flatMap(user -> {

                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    String confirmationToken = UUID.randomUUID().toString();
                    UserEntity user = toUserEntity(registerUserDto, confirmationToken);
                    return userRepository.save(user)
                            .doOnSuccess(entity -> {
                                emailService.sendConfirmationEmail(entity.getEmail(), CONFIRMATION_LINK + confirmationToken);
                            });
                })).then();

    }

    private UserEntity toUserEntity(RegisterUserDto registerUserDto, String confirmationToken) {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setFirstname(registerUserDto.getFirstname());
        user.setUserRole(UserRole.USER);
        user.setLastname(registerUserDto.getLastname());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        user.setEnabled(false);
        user.setNewEntity(true);
        user.setConfirmToken(confirmationToken);
        return user;

    }

}