package backend.controlers;

import backend.dto.RegisterUserDto;
import backend.rdb.entities.UserEntity;
import backend.rdb.repositories.UserRepository;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public Mono<UserEntity> registerUser(@RequestBody RegisterUserDto user) {
        user.setPassword(user.getPassword());
         return userService.create(user);
    }
}
