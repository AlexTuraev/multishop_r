package org.tasks.myshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.tasks.myshop.dto.InnerOrder;
import org.tasks.myshop.service.OrderService;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // READY
    @GetMapping
    public Mono<Rendering> getOrders(Model model) {
        Mono<Map<Long, InnerOrder>> monoOrders = orderService.findAll();
        return monoOrders.map(orders -> {
            model.addAttribute("orders", orders);
            Rendering r = Rendering.view("item")
                    .model(model.asMap())
                    .build();
            return r;
        });
    }

    // READY
    @GetMapping("/{id}")
    public Mono<Rendering> getOrder(Model model, @PathVariable("id") Long orderId) {
        Mono<Model> monoModel = orderService.getModelOrdersById(model, orderId);
        return monoModel.map(model1 -> {
            Rendering r = Rendering.view("order")
                    .model(model1.asMap())
                    .build();
            return r;
        });
    }

}
