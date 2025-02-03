package backend.rdb.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import backend.rdb.entities.LaptopEntity;

@Repository
public interface LaptopRepository extends ReactiveCrudRepository<LaptopEntity, String> {
}
