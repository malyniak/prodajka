package backend.domain.services;

import backend.domain.dto.RegisterUserDto;
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


    public Mono<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Mono<UserEntity> create(RegisterUserDto registerUserDto) {
       return userRepository.save(toUserEntity(registerUserDto));
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
      return RegisterUserDto.builder()
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname() != null ? userEntity.getLastname() : null)
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .password(userEntity.getPassword())
                .build();
    }

}
