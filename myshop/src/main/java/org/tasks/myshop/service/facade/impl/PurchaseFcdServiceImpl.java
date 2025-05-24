package org.tasks.myshop.service.facade.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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

    @Value("${app.payment.url}")
    private String PAYMENT_URL;

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
        Long nextOrderId = orderService.getNextOrderId().block().longValue();

        return cartService.getCartsByCartId(cartId)
                .collectList()
                .doOnNext(carts -> {
                    WebClient webClient = WebClient.create(PAYMENT_URL);
                    Mono<Integer> resBalance;
                    try {
                        resBalance = webClient.post()
                                .bodyValue(new InnerPaymentUserbalancePostRequest(cartService.getTotalSumList(carts).intValue()))
                                .retrieve()
                                .bodyToMono(Integer.class);
                    }
                    catch (WebClientResponseException.BadRequest e) {
                        e.printStackTrace();
                        throw e;
                    }
                })
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

    static class InnerPaymentUserbalancePostRequest{
        @JsonProperty
        Integer amount;

        public InnerPaymentUserbalancePostRequest(Integer amount) {
            this.amount = amount;
        }
    };

}
