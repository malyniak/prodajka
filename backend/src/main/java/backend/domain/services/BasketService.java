package backend.domain.services;

import backend.domain.dto.BasketItemDto;
import backend.domain.dto.UserBasketDto;
import backend.domain.model.Basket;
import backend.domain.model.Product;
import backend.rdb.entities.UserBasketEntity;
import backend.rdb.repositories.BasketItemRepository;
import backend.rdb.repositories.UserBasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final UserBasketRepository userBasketRepository;
    private final BasketItemRepository basketItemRepository;
    private final BasketItemService basketItemService;
    private final UserService userService;
    private final ProductService productService;


    public Mono<UserBasketDto> getUserBasketDto() {
        return userService.getCurrentUser()
                .flatMap(user -> userBasketRepository.findByUserId(user.getId())
                        .flatMap(userBasketEntity -> basketItemService.getBasketItem(userBasketEntity.getId())
                                .collectList()
                                .map(basketItemList -> toUserBasketDto(userBasketEntity, basketItemList.isEmpty() ? List.of() : basketItemList))
                        )
                );
    }

    public Mono<Basket> getBasket() {
        return userService.getCurrentUser()
                .flatMap(user -> userBasketRepository.findByUserId(user.getId())
                        .flatMap(userBasketEntity ->
                                basketItemService.getBasketItem(userBasketEntity.getId())
                                        .collectList()
                                        .flatMap(this::toProductsMap)
                                        .map(productMap ->
                                                Basket.builder()
                                                        .id(userBasketEntity.getId())
                                                        .userId(userBasketEntity.getUserId())
                                                        .products(productMap)
                                                        .build()
                                        )
                        )
                );
    }

    public Mono<UserBasketEntity> createUserBasket() {
        return userService.getCurrentUser()
                .flatMap(user -> {
                    UserBasketEntity userBasketEntity = new UserBasketEntity(UUID.randomUUID().toString(), user.getId());
                    userBasketEntity.setNewEntity(true);
                    return userBasketRepository.save(userBasketEntity);
                });
    }
    public Mono<Void> clearBasket() {
        return userService.getCurrentUser()
                .flatMap(user -> getBasket()
                        .flatMap(basket -> basketItemRepository.findByBasketId(basket.getId())
                                .flatMap(basketItemRepository::delete)
                                .then()
                        )
                );
    }
    private UserBasketDto toUserBasketDto(UserBasketEntity userBasketEntity, List<BasketItemDto> basketItemDtoList) {
        UserBasketDto userBasketDto = new UserBasketDto();
        userBasketDto.setId(userBasketEntity.getId());
        userBasketDto.setBasketItems(basketItemDtoList);

        return userBasketDto;
    }

    private Mono<Map<Product, Integer>> toProductsMap(List<BasketItemDto> basketItemDtoList) {
        return Flux.fromIterable(basketItemDtoList)
                .flatMap(basketItemDto -> productService.getById(basketItemDto.getProductId())
                        .map(productDto -> Map.entry(productService.toProduct(productDto), basketItemDto.getCount())))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }
}