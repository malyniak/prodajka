package backend.rdb.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import backend.rdb.entities.TvEntity;

public interface TvRepository extends ReactiveCrudRepository<TvEntity, String> {
}
