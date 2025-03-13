package backend.domain.services;


import backend.domain.dto.BasketItemDto;
import backend.domain.model.Product;
import backend.rdb.entities.BasketItemEntity;
import backend.rdb.entities.UserBasketEntity;
import backend.rdb.repositories.BasketItemRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketItemService {

    private final BasketItemRepository basketItemRepository;


    public Flux<BasketItemDto> getBasketItem(String basketId) {
        return basketItemRepository.findByBasketId(basketId)
                .map(this::toBasketItemDto);
    }

    public Mono<Void> createBasketItem(String productId, String basketId) {
        BasketItemEntity basketItemEntity = new BasketItemEntity(UUID.randomUUID().toString(), basketId, productId, 1);
        basketItemEntity.setNewEntity(true);
       return basketItemRepository.save(basketItemEntity).then();
    }

    public Mono<Void> increaseProduct(String productId) {
        basketItemRepository.findByProductId(productId)
                .map(basketItemEntity -> {
                    basketItemEntity.setCount(basketItemEntity.getCount() + 1);
                   return basketItemRepository.save(basketItemEntity).then();
                });
        return Mono.empty();
    }

    public Mono<Void> deleteBasketItem(String productId, String basketId) {
        return getBasketItem(basketId)
                .doOnNext(basketItemDto -> System.out.println("Found item: " + basketItemDto))
                .filter(basketItemDto -> basketItemDto.getProductId().equals(productId))
                .doOnNext(basketItemDto -> System.out.println("Filtered item: " + basketItemDto))
                .next()
                .doOnNext(basketItemDto -> System.out.println("Deleting item: " + basketItemDto.getId()))
                .flatMap(basketItemDto -> basketItemRepository.deleteById(basketItemDto.getId()))
                .doOnSuccess(unused -> System.out.println("Delete operation completed"))
                .then();
    }

    public Mono<Void> changeCount(String productId, Integer newCount) {
       return basketItemRepository.findByProductId(productId)
                .flatMap(basketItemEntity -> {
                    basketItemEntity.setCount(newCount);
                    basketItemEntity.setNewEntity(false);
                   return basketItemRepository.save(basketItemEntity);
                }).then();
    }





//    public Mono<BasketItemEntity> createBasketItem(String basketId) {
//        new BasketItemEntity()
//        return basketItemRepository.save()
//    }

    private BasketItemDto toBasketItemDto(BasketItemEntity basketItemEntity) {
        BasketItemDto basketItemDto = new BasketItemDto();
        basketItemDto.setId(basketItemEntity.getId());
        basketItemDto.setProductId(basketItemEntity.getProductId());
        basketItemDto.setCount(basketItemEntity.getCount());
        return basketItemDto;
    }



}

