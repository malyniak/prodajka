package backend.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import backend.domain.dto.LaptopDto;
import backend.rdb.entities.LaptopEntity;
import backend.rdb.repositories.LaptopRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LaptopService {
    private final LaptopRepository laptopRepository;

    public Flux<LaptopDto> getAll() {
        return laptopRepository.findAll()
                .doOnNext(laptopEntity -> System.out.println("Laptop " + laptopEntity))
                .doOnError(System.out::println)
                .map(this::toLaptop);
    }

    public Mono<LaptopDto> getById(String id) {
        return laptopRepository.findById(id).map(this::toLaptop);
    }

    public Mono<LaptopEntity> create(LaptopDto laptopDto) {
        return laptopRepository.save(toLaptopEntity(laptopDto))
                .doOnNext(__ -> System.out.println("Laptop created"))
                .doOnError(__ -> System.out.println("Save error"));
    }

    public Mono<LaptopEntity> update(LaptopDto laptopDto, String id) {
        return laptopRepository.findById(id)
                .flatMap(laptopEntity -> {
                    if (laptopDto.getProductId() != null) {
                        laptopEntity.setProductId(laptopDto.getProductId());
                    }
                    if (laptopDto.getCamera() != null) {
                        laptopEntity.setCamera(laptopDto.getCamera());
                    }
                    if (laptopDto.getFrequencies() != null) {
                        laptopEntity.setFrequencies(laptopDto.getFrequencies());
                    }
                    if (laptopDto.getMemory() != null) {
                        laptopEntity.setMemory(laptopDto.getMemory());
                    }
                    if (laptopDto.getVersionOS() != null) {
                        laptopEntity.setVersionOS(laptopDto.getVersionOS());
                    }
                    if (laptopDto.getDisplay() != null) {
                        laptopEntity.setDisplay(laptopDto.getDisplay());
                    }
                    laptopEntity.setNewEntity(false);
                    return laptopRepository.save(laptopEntity);
                });
    }


    public LaptopDto toLaptop(LaptopEntity laptopEntity) {
        return new LaptopDto(laptopEntity.getId(), laptopEntity.getProductId(), laptopEntity.getDisplay(), laptopEntity.getFrequencies(),
                laptopEntity.getCamera(), laptopEntity.getVersionOS(), laptopEntity.getMemory());
    }

    public LaptopEntity toLaptopEntity(LaptopDto laptopDto) {
        var laptopEntity = new LaptopEntity();
        laptopEntity.setId(UUID.randomUUID().toString());
        laptopEntity.setProductId(laptopDto.getProductId());
        laptopEntity.setCamera(laptopDto.getCamera());
        laptopEntity.setFrequencies(laptopDto.getFrequencies());
        laptopEntity.setMemory(laptopDto.getMemory());
        laptopEntity.setVersionOS(laptopDto.getVersionOS());
        laptopEntity.setDisplay(laptopEntity.getDisplay());
        laptopEntity.setNewEntity(true);
        return laptopEntity;
    }


    public Mono<Void> delete(String id) {
        return laptopRepository.findById(id).doOnNext(System.out::println)
                .flatMap(laptopRepository::delete);
    }
}


