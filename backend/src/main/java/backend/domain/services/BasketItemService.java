package backend.domain.services;


import backend.domain.dto.BasketProductDto;
import backend.domain.dto.ProductDto;
import backend.rdb.entities.BasketItemEntity;
import backend.rdb.repositories.BasketItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketItemService {
    private final ProductService productService;
    private final BasketItemRepository basketItemRepository;


    public Flux<BasketProductDto> getBasketItem(String basketId) {
        return basketItemRepository.findByBasketId(basketId)
                .flatMap(basketItemEntity -> {
                    return productService.getById(basketItemEntity.getProductId())
                            .map(productDto -> toBasketItemDto(basketItemEntity, productDto));
                });

    }

    public Mono<Void> createBasketItem(String productId, String basketId) {
        BasketItemEntity basketItemEntity = new BasketItemEntity(UUID.randomUUID().toString(), basketId, productId, 1);
        basketItemEntity.setNewEntity(true);
        return basketItemRepository.save(basketItemEntity).then();
    }

    public Mono<Void> increaseProduct(String productId) {
        basketItemRepository.findBy(productId)
                .map(basketItemEntity -> {
                    basketItemEntity.setCount(basketItemEntity.getCount() + 1);
                    return basketItemRepository.save(basketItemEntity).then();
                });
        return Mono.empty();
    }

    public Mono<Void> deleteBasketItem(String productId, String basketId) {
        return getBasketItem(basketId)
                .filter(basketItemDto -> basketItemDto.getProductId().equals(productId))
                .next()
                .flatMap(basketItemDto -> basketItemRepository.deleteById(basketItemDto.getId()))
                .then();
    }

    public Mono<Void> changeCount(String productId, Integer newCount) {
        return basketItemRepository.findBy(productId)
                .flatMap(basketItemEntity -> {
                    basketItemEntity.setCount(newCount);
                    basketItemEntity.setNewEntity(false);
                    return basketItemRepository.save(basketItemEntity);
                }).then();
    }

    private BasketProductDto toBasketItemDto(BasketItemEntity basketItemEntity, ProductDto productDto) {
        BasketProductDto basketItemDto = new BasketProductDto();
        basketItemDto.setId(basketItemEntity.getId());
        basketItemDto.setProductId(basketItemEntity.getProductId());
        basketItemDto.setFullName(productDto.getFullName());
        basketItemDto.setPrice(productDto.getPrice());
        basketItemDto.setImageUrl(productDto.getImageUrl());
        basketItemDto.setCount(basketItemEntity.getCount());
        return basketItemDto;
    }


}

