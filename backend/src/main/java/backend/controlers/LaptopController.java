package backend.controlers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import backend.dto.LaptopDto;
import backend.rdb.entities.LaptopEntity;
import backend.services.LaptopService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/laptop")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class LaptopController {
    private final LaptopService laptopService;
    @GetMapping
    public Flux<LaptopDto> getAllPhones() {
        return laptopService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<LaptopDto> getById(@PathVariable String id) {
        return laptopService.getById(id);
    }

    @PutMapping
    public Mono<LaptopEntity> create(@RequestBody LaptopDto laptopDto) {
        return laptopService.create(laptopDto);
    }

    @PatchMapping(value = "/{id}")
    public Mono<LaptopEntity> update(@RequestBody LaptopDto laptopDto, @PathVariable String id) {
        return laptopService.update(laptopDto, id);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return laptopService.delete(id);
    }
}
