package backend.rdb.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import backend.rdb.entities.ProductEntity;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, String> {
    @Query("select p from products as p where image_url = :url")
    Mono<ProductEntity> findByImageUrl(String url);
}
