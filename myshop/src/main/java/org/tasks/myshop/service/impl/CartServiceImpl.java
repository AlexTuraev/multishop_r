package org.tasks.myshop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.dao.repository.CartRepository;
import org.tasks.myshop.dto.CartDto;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.mapper.CartMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public Mono<CartEntity> getCartByItemIdAndCartId(Long itemId, Long cartId) {
        return cartRepository.findByItemIdAndCartId(itemId, cartId);
    }

    @Override
    public Mono<CartEntity> updateCountItem(Long itemId, Long cartId, int deltaCount) {
        Mono<CartEntity> monoCart = getCartByItemIdAndCartId(itemId, cartId)
                //.map(o -> o.orElse(new CartEntity(cartId, itemId, 0, null)));
                .defaultIfEmpty(new CartEntity(cartId, itemId, 0, null));

        return monoCart.map(cart -> {
            if (cart.getCountItem() == 0 && deltaCount < 0) {
                throw new RuntimeException("Попытка уменьшить отсутствующее значение");
            }

            cart.setCountItem(cart.getCountItem() + deltaCount);
            if (cart.getCountItem() > 0) {
                cartRepository.save(cart);
                return cart;
            }
            else {
                cartRepository.deleteByItemIdAndCartId(itemId, cartId);
                return cart;
            }
        });


    }

    @Override
    public Flux<CartEntity> getCartsByCartId(Long cartId) {
        return cartRepository.getCartModelByCartId(cartId);
    }

    @Override
    public Mono<Model> getModelByCartId(Model model, Long cartId) {
        Flux<CartEntity> fluxCart = getCartsByCartId(cartId);
        Mono<List<CartDto>> modelCartItems = fluxCart.collectList().map(cart->cart.stream().map(cartMapper::toDto).toList());

        Mono<BigDecimal> modeltotalSum = getTotalSum(fluxCart);

        return Mono.zip(modelCartItems, modeltotalSum).map(tuple->{
            model.addAttribute("cartItems", tuple.getT1());
            model.addAttribute("totalSum", tuple.getT2());
            return model;
        });
    }

    @Override
    public Mono<Integer> getCountItemOrZeroIfAbsent(Long itemId, Long cartId) {
        return getCartByItemIdAndCartId(itemId, cartId)
                .map(c -> c.getCountItem())
                .defaultIfEmpty(0);
    }

    @Override
    public Mono<Void> deleteAll(List<CartEntity> carts) {
        return cartRepository.deleteAll(carts).then();
    }

    @Override
    public Mono<BigDecimal> getTotalSum(Flux<CartEntity> fluxCarts) {
        return fluxCarts.collectList()
                .map(carts -> {
                    BigDecimal total = BigDecimal.ZERO;
                    for (CartEntity cart : carts) {
                        BigDecimal sumItem = cart.getItem().getPrice().multiply(BigDecimal.valueOf(cart.getCountItem()));
                        total = total.add(sumItem);
                    }
                    return total;
                });
    }

    @Override
    public BigDecimal getTotalSumList(List<CartEntity> carts) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartEntity cart : carts) {
            BigDecimal sumItem = cart.getItem().getPrice().multiply(BigDecimal.valueOf(cart.getCountItem()));
            total = total.add(sumItem);
        }
        return total;
    }

}
