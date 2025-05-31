package org.tasks.myshop.service.facade.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClient;
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
import java.util.Map;

@Service
public class PurchaseFcdServiceImpl implements PurchaseFcdService {

    @Value("${app.payment.url}")
    private String PAYMENT_URL;

    @Value("${app.payment.uri}")
    private String PAYMENT_URI;

    private final OAuth2AuthorizedClientManager manager;

    private final CartService cartService;
    private final OrderService orderService;
    private final CartOrderMapper cartOrderMapper;
    private final CartMapper cartMapper;

    public PurchaseFcdServiceImpl(OAuth2AuthorizedClientManager manager, CartService cartService, OrderService orderService, CartOrderMapper cartOrderMapper, CartMapper cartMapper) {
        this.manager = manager;
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
                    OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                            .withClientRegistrationId("myshop")
                            .principal("system")
                            .build()
                    );
                    String accessToken = client.getAccessToken().getTokenValue();
                    RestClient restClient = RestClient.create(PAYMENT_URL);
                    Integer resBalance;
                    try {
                        resBalance = restClient.post()
                                .uri("/payment/userbalance")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                                .body(Map.of("amount", cartService.getTotalSumList(carts).intValue()))
                                .retrieve()
                                .toEntity(Integer.class)
                                .getBody();
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
