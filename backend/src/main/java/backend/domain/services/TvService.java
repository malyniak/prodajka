package backend.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import backend.domain.dto.TvDto;
import backend.rdb.entities.TvEntity;
import backend.rdb.repositories.TvRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TvService {
    private final TvRepository tvRepository;

    public Flux<TvDto> getAll() {
        return tvRepository.findAll()
                .doOnNext(tvEntity -> System.out.println("Tv " + tvEntity))
                .doOnError(System.out::println)
                .map(this::toTv);
    }

    public Mono<TvDto> getById(String id) {
        return tvRepository.findById(id).map(this::toTv);
    }

    public Mono<TvEntity> create(TvDto tvDto) {
        return tvRepository.save(toTvEntity(tvDto))
                .doOnNext(__ -> System.out.println("Phone created"))
                .doOnError(__ -> System.out.println("Save error"));
    }

    public Mono<TvEntity> update(TvDto tvDto, String id) {
        return tvRepository.findById(id)
                .flatMap(tvEntity -> {
            if(tvDto.getProductId() != null) {
                tvEntity.setProductId(tvDto.getProductId());
            }
            if (tvDto.getDiagonal() != null) {
                tvEntity.setDiagonal(tvDto.getDiagonal());
            }
            if (tvDto.getFrequency() != null) {
                tvEntity.setFrequency(tvDto.getFrequency());
            }
            if (tvDto.getPlatform() != null) {
                tvEntity.setPlatform(tvDto.getPlatform());
            }
            if (tvDto.getResolvingPower() != null) {
                tvEntity.setResolvingPower(tvDto.getResolvingPower());
            }
            tvEntity.setNewEntity(false);
            return tvRepository.save(tvEntity);
        });
    }


    public TvDto toTv(TvEntity tvEntity) {
        return new TvDto(tvEntity.getId(), tvEntity.getProductId(), tvEntity.getDiagonal(), tvEntity.getResolvingPower(),
                tvEntity.getPlatform(), tvEntity.getFrequency());
    }

    public TvEntity toTvEntity(TvDto tvDto) {
        var tvEntity = new TvEntity();
        tvEntity.setId(UUID.randomUUID().toString());
        tvEntity.setProductId(tvDto.getProductId());
        tvEntity.setDiagonal(tvDto.getDiagonal());
        tvEntity.setFrequency(tvDto.getFrequency());
        tvEntity.setPlatform(tvDto.getPlatform());
        tvEntity.setResolvingPower(tvDto.getResolvingPower());
        tvEntity.setNewEntity(true);
        return tvEntity;
    }


    public Mono<Void> delete(String id) {
        return tvRepository.findById(id).doOnNext(System.out::println)
                .flatMap(tvRepository::delete);
    }
}
