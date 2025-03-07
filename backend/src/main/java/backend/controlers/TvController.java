package backend.controlers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import backend.domain.dto.TvDto;
import backend.rdb.entities.TvEntity;
import backend.domain.services.TvService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tv")
@RequiredArgsConstructor
public class TvController {
    private final TvService tvService;

    @GetMapping
    public Flux<TvDto> getAllTvs() {
        return tvService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<TvDto> getById(@PathVariable String id) {
        return tvService.getById(id);
    }

    @PutMapping
    public Mono<TvEntity> create(@RequestBody TvDto tvDto) {
        return tvService.create(tvDto);
    }

    @PatchMapping(value = "/{id}")
    public Mono<TvEntity> update(@RequestBody TvDto tvDto, @PathVariable String id) {
        return tvService.update(tvDto, id);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return tvService.delete(id);
    }
}
