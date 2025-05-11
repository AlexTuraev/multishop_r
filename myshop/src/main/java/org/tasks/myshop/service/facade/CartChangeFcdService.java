package org.tasks.myshop.service.facade;

import reactor.core.publisher.Mono;

public interface CartChangeFcdService {

    Mono<Integer> getDelta(Long cartId, Long itemId, String action);

    Mono<Void> updateItemInCart(Long cartId, Long itemId, String action);

}
