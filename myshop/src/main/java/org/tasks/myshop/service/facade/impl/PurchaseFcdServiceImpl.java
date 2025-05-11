package org.tasks.myshop.service.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.tasks.myshop.dao.model.OrderEntity;
import org.tasks.myshop.dto.OrderDto;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.OrderService;
import org.tasks.myshop.service.facade.PurchaseFcdService;
import org.tasks.myshop.service.mapper.CartMapper;
import org.tasks.myshop.service.mapper.CartOrderMapper;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PurchaseFcdServiceImpl implements PurchaseFcdService {

    private final CartService cartService;
    private final OrderService orderService;
    private final CartOrderMapper cartOrderMapper;
    private final CartMapper cartMapper;

    public PurchaseFcdServiceImpl(CartService cartService, OrderService orderService, CartOrderMapper cartOrderMapper, CartMapper cartMapper) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.cartOrderMapper = cartOrderMapper;
        this.cartMapper = cartMapper;
    }

    @Override
    @Transactional
    public Mono<Model> purchase(Model model, Long cartId) {
//        List<CartEntity> carts = cartService.getCartsByCartId(cartId);
//        Long nextOrderId = orderService.getNextOrderId();
//        List<OrderEntity> orders = carts.stream()
//                .map(cartOrderMapper::cartToOrderEntity)
//                .map(orderEntity -> orderEntity.orderId(nextOrderId))
//                .toList();


        Long nextOrderId = orderService.getNextOrderId().block().longValue();
        return cartService.getCartsByCartId(cartId)
                .collectList()
                .doOnNext(cartService::deleteAll)                                                         // cartService.deleteAll(carts);
                .doOnNext(carts -> {
                    List<OrderEntity> orders = carts.stream()
                            .map(cartOrderMapper::cartToOrderEntity)
                            .map(orderEntity -> orderEntity.orderId(nextOrderId))
                            .toList();
                    orderService.saveAll(orders);                                                         // List<OrderEntity> list = orderService.saveAll(orders);
        })
                .map(carts -> {
                    List<OrderDto> orderDtos = carts.stream()
                                    .map(cartMapper::toDto)
                                    .map(cartOrderMapper::cartToOrderDto)
                                    .toList();
                    model.addAttribute("afterPurchase", true);
                    model.addAttribute("orderId", nextOrderId);
                    model.addAttribute("orders", orderDtos);
//                    model.addAttribute("totalSum", cartService.getTotalSum(carts));
                    return model;
                });

        /*List<OrderDto> orderDtos = carts.stream()
                .map(cartMapper::toDto)
                .map(cartOrderMapper::cartToOrderDto)
                .toList();*/

//        model.addAttribute("afterPurchase", true);
//        model.addAttribute("orderId", nextOrderId);
//        model.addAttribute("orders", orderDtos);
//        model.addAttribute("totalSum", cartService.getTotalSum(carts));
//        return model;
    }



}
