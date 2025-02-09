package backend.services;

import backend.dto.RegisterUserDto;
import backend.rdb.entities.UserEntity;
import backend.rdb.entities.enums.UserRole;
import backend.rdb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Flux<RegisterUserDto> findAll() {
       return userRepository.findAll().map(this::toUserDto);
    }

    public Mono<UserEntity> create(RegisterUserDto userDto) {

       return userRepository.save(toUserEntity(userDto));
    }

    public Mono<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }



    private UserEntity toUserEntity(RegisterUserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID().toString());
        userEntity.setFirstname(userDto.getFirstname());
        if(userDto.getLastname() != null) {
            userEntity.setLastname(userDto.getLastname());
        }
        userEntity.setEmail(userDto.getEmail());
        userEntity.setUserRole(UserRole.USER);
        userEntity.setPhoneNumber(String.valueOf(userDto.getPhoneNumber()));
        userEntity.setNewEntity(true);
        userEntity.setPassword(userDto.getPassword());
        return userEntity;
    }


    private RegisterUserDto toUserDto(UserEntity userEntity) {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setEmail(userEntity.getEmail());
        userDto.setPhoneNumber(userEntity.getPhoneNumber());
        userDto.setFirstname(userEntity.getFirstname());
        if(userEntity.getLastname() != null) {
            userDto.setLastname(userEntity.getLastname());
        }
        return userDto;
    }
}
