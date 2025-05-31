package org.tasks.myshop.service;

import org.springframework.ui.Model;
import org.tasks.myshop.dao.model.OrderEntity;
import org.tasks.myshop.dto.InnerOrder;
import org.tasks.myshop.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Mono<Long> getNextOrderId();

    Flux<OrderEntity> saveAll(List<OrderEntity> orders);
    Mono<Map<Long, InnerOrder>> findAll();

    Flux<OrderDto> getOrdersById(Long orderId);

    Mono<Model> getModelOrdersById(Model model, Long orderId);
}
