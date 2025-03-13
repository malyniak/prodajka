package backend.rdb.repositories;

import backend.rdb.entities.UserBasketEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserBasketRepository extends ReactiveCrudRepository<UserBasketEntity, String> {
    // @Query("select * from baskets b where b.user_id = :userId")
    Mono<UserBasketEntity> findByUserId(String userId);

}
