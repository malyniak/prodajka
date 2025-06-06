package backend.rdb.repositories;

import backend.rdb.entities.BasketItemEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BasketItemRepository extends R2dbcRepository<BasketItemEntity, String> {
   // @Query("select * from basket_elements b where b.basket_id = :basketId")
    Flux<BasketItemEntity> findByBasketId(String basketId);

    @Query("SELECT * FROM basket_elements WHERE product_id = :productId")
    Mono<BasketItemEntity> findBy(@Param("productId") String productId);
}
