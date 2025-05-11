package org.tasks.myshop.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import org.tasks.myshop.dto.ItemDto;
import org.tasks.myshop.exception.SortException;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.MyshopService;
import org.tasks.myshop.service.facade.CartChangeFcdService;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/myshop")
public class MyshopController {

    private final MyshopService myshopService;
    private final CartService cartService;
    private final CartChangeFcdService cartChangeFcdService;

    public MyshopController(MyshopService myshopService, CartService cartService, CartChangeFcdService cartChangeFcdService) {
        this.myshopService = myshopService;
        this.cartService = cartService;
        this.cartChangeFcdService = cartChangeFcdService;
    }

    // READY-R
    @GetMapping
    public Mono<Rendering> getItems(
            Model model,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) throws SortException {
        Mono<Model> monoModel = myshopService.getItemsModel(model, search, pageSize, pageNumber, sort);
        return monoModel.map(model1 -> Rendering.view("myshop")
                    .model(model1.asMap())
                    .build());
    }

    // READY-R
    @GetMapping("/item/{id}")
    public Mono<String> getItem(Model model, @PathVariable("id") Long id) {
        Mono<ItemDto> monoItemDto = myshopService.getItemById(id);
        Mono<Integer> monoCountItem = cartService.getCountItemOrZeroIfAbsent(id, 1L);
        return Mono.zip(monoItemDto, monoCountItem)
                .doOnNext(tuple -> {
                    model.addAttribute("item", tuple.getT1());
                    model.addAttribute("countItem", tuple.getT2());
                })
                .thenReturn("item");
    }

    // READY
    @PostMapping("/item/{id}/cart")
    public Mono<Rendering> changeItemCart(
            Model model,
            @PathVariable("id") Long id,
            @RequestParam("action") @Min(-1) @Max(1) int delta) {
        Mono<Model> monoModel = myshopService.changeItemCart(model, id, 1L, delta);

        return monoModel.map(model1 -> {
            Rendering r = Rendering.view("item")
                    .model(model1.asMap())
                    .build();
            return r;
        });
    }

    // READY
    @PostMapping("/item/{id}/changecart")
    public Mono<String> changeItemCartFromMain(
            Model model,
            @PathVariable("id") Long id,
            @RequestParam("action") String action) {
        Long cartId = 1L; // эмуляция 1-й корзины, в дальнейшем можно брать номер из авторизации user (напр, id user'а)
        return cartChangeFcdService.updateItemInCart(cartId,  id, action)
                .map(r -> "redirect:/myshop");
    }

}
