package backend.rdb.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import backend.rdb.entities.PhoneEntity;
import reactor.core.publisher.Flux;

public interface PhoneRepository extends ReactiveCrudRepository<PhoneEntity, String> {

    @Query("select * from phones as ph left join products as pr " +
            "on pr.id = ph.product_id order by pr.price asc ")
    Flux<PhoneEntity> sortByPriceAsc();

    @Query("select * from phones as ph left join products as pr " +
            "on pr.id = ph.product_id order by pr.price asc ")
    Flux<PhoneEntity> sortByPriceDesc();
}
