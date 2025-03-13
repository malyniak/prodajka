package backend.controlers;

import backend.domain.dto.UserBasketDto;
import backend.domain.services.BasketItemService;
import backend.domain.services.BasketService;
import backend.rdb.entities.UserBasketEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/basket")
public class BasketController {
    private final BasketService basketService;
    private final BasketItemService basketItemService;

    @GetMapping("/admin")
    public Mono<UserBasketDto> getUserBasket(@RequestParam String userId) {
        return basketService.getUserBasketDto();
        // todo
    }

    @GetMapping
    public Mono<UserBasketDto> getUserBasket() {
        return basketService.getUserBasketDto();
    }

    @PostMapping
    public Mono<UserBasketEntity> create() {
        return basketService.createUserBasket();
    }

    @PostMapping("/item")
    public Mono<Void> addBasketItem(@RequestParam String productId, @RequestParam String basketId) {
       return basketItemService.createBasketItem(productId, basketId);
    }

    @DeleteMapping
    public Mono<Void> deleteBasketItem(@RequestParam String productId, @RequestParam String basketId) {
        return basketItemService.deleteBasketItem(productId, basketId);
    }

    @DeleteMapping("/clear")
    public Mono<Void> clearBasket(@RequestParam String basketId) {
        return basketService.clearBasket();
    }

    @PatchMapping
    public Mono<Void> changeCount(@RequestParam String productId, Integer count) {
        return basketItemService.changeCount(productId, count);
    }
}
