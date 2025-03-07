package backend.rdb.repositories;

import backend.rdb.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, String> {
    @Query("select * from users where email = :email")
    Mono<UserEntity> findByEmail(String email);

    @Query("select * from users where confirm_token = :confirmationToken")
    Mono<UserEntity> findByConfirmToken(String confirmationToken);

}
