package org.tasks.myshop.service;

import org.springframework.ui.Model;
import org.tasks.myshop.dao.model.CartEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    Mono<CartEntity> getCartByItemIdAndCartId(Long itemId, Long cartId);

    Mono<CartEntity> updateCountItem(Long itemId, Long cartId, int deltaCount);

    Flux<CartEntity> getCartsByCartId(Long cartId);

    Mono<Model> getModelByCartId(Model model, Long cartId);

    Mono<Integer> getCountItemOrZeroIfAbsent(Long itemId, Long cartId);

    Mono<Void> deleteAll(List<CartEntity> carts);

    Mono<BigDecimal> getTotalSum(Flux<CartEntity> carts);

    BigDecimal getTotalSumList(List<CartEntity> carts);

}
