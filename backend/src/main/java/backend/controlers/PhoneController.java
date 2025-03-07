package backend.controlers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import backend.domain.dto.PhoneDto;
import backend.rdb.entities.PhoneEntity;
import backend.domain.services.PhoneService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/phone")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PhoneController {
    private final PhoneService phoneService;

    @GetMapping
    public Flux<PhoneDto> getAllPhones() {
       return phoneService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<PhoneDto> getById(@PathVariable String id) {
        return phoneService.getById(id);
    }

    @PutMapping
    public Mono<PhoneEntity> create(@RequestBody PhoneDto phoneDto) {
       return phoneService.create(phoneDto);
    }

    @PatchMapping(value = "/{id}")
    public Mono<PhoneEntity> update(@RequestBody PhoneDto phoneDto, @PathVariable String id) {
        return phoneService.update(phoneDto, id);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return phoneService.delete(id);
    }
}
