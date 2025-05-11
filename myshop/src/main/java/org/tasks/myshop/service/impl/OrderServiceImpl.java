package org.tasks.myshop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.tasks.myshop.dao.model.OrderEntity;
import org.tasks.myshop.dao.repository.OrderRepository;
import org.tasks.myshop.dto.InnerOrder;
import org.tasks.myshop.dto.OrderDto;
import org.tasks.myshop.service.OrderService;
import org.tasks.myshop.service.mapper.OrderMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Mono<Long> getNextOrderId() {
        return orderRepository.getNextOrderId();
    }

    @Override
    public Flux<OrderEntity> saveAll(List<OrderEntity> orders) {
        return orderRepository.saveAll(orders);
    }

    @Override
    public Mono<Map<Long, InnerOrder>> findAll() {
//        List<OrderDto> dtos = orderRepository.findAll()
//                .stream().map(orderMapper::toDto).toList();
//
//        Map<Long, List<OrderDto>> mapOrders = dtos.stream().collect(Collectors.groupingBy(OrderDto::getOrderId));
//
//        Map<Long, InnerOrder> newMap = mapOrders.entrySet().stream()
//                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), new InnerOrder(e.getValue(), getTotalSum(e.getValue()))))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//        return newMap;
        return null;
    }

    @Override
    public Flux<OrderDto> getOrdersById(Long orderId) {
        return orderRepository.findAllWhereId(orderId)
                .map(orderMapper::toDto);
    }

    @Override
    public Mono<Model> getModelOrdersById(Model model, Long orderId) {
        return getOrdersById(orderId)
                .collectList()
                .map(orderDtos -> {
                    model.addAttribute("afterPurchase", false);
                    model.addAttribute("orderId", orderId);
                    model.addAttribute("orders", orderDtos);
                    model.addAttribute("totalSum", getTotalSum(orderDtos));

                    return model;
                });
    }

    public BigDecimal getTotalSum(List<OrderDto> orders) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDto orderDto : orders) {
            BigDecimal sumItem = orderDto.getItem().getPrice().multiply(BigDecimal.valueOf(orderDto.getCountItem()));
            total = total.add(sumItem);
        }
        return total;
    }

}
