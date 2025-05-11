package org.tasks.myshop.service.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.MyshopService;
import org.tasks.myshop.service.facade.CartChangeFcdService;
import reactor.core.publisher.Mono;

@Service
public class CartChangeFcdServiceImpl implements CartChangeFcdService {

    private final CartService cartService;
    private final MyshopService myshopService;

    public CartChangeFcdServiceImpl(CartService cartService, MyshopService myshopService) {
        this.cartService = cartService;
        this.myshopService = myshopService;
    }

    @Override
    @Transactional
    public Mono<Void> updateItemInCart(Long cartId, Long itemId, String action) {
        Mono<Integer> monoDelta = getDelta(cartId, itemId, action);
        return monoDelta.doOnNext(delta -> Mono.zip(cartService.updateCountItem(itemId, cartId, delta), myshopService.updateCountItem(itemId, -delta))).then();
    }

    @Override
    public Mono<Integer> getDelta(Long cartId, Long itemId, String action) {
        return cartService.getCartByItemIdAndCartId(itemId, cartId)
//                .map(o->o.orElse(new CartEntity(cartId, itemId, 0, null)))
                .defaultIfEmpty(new CartEntity(cartId, itemId, 0, null))
                .map(c -> {
                    return switch (action) {
                        case "plus" -> 1;
                        case "minus" -> -1;
                        case "delete" -> -c.getCountItem();
                        default -> throw new IllegalArgumentException("Invalid action:  " + action);
                    };
                })
                ;


    }


}
