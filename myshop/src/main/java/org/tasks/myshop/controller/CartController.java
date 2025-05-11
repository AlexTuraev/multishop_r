package org.tasks.myshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.facade.CartChangeFcdService;
import org.tasks.myshop.service.facade.PurchaseFcdService;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final PurchaseFcdService purchaseFcdService;
    private final CartChangeFcdService  cartChangeFcdService;

    public CartController(CartService cartService, PurchaseFcdService purchaseFcdService, CartChangeFcdService cartChangeFcdService) {
        this.cartService = cartService;
        this.purchaseFcdService = purchaseFcdService;
        this.cartChangeFcdService = cartChangeFcdService;
    }

    // READY
    @GetMapping("/{id}")
    public Mono<Rendering> getCartByCartId(@PathVariable("id") Long cartId, Model model) {
        Mono<Model> monoModel = cartService.getModelByCartId(model, cartId);

        return monoModel.map(model1 -> {
            Rendering r = Rendering.view("cart")
                    .model(model1.asMap())
                    .build();
            return r;
        });
    }

    // READY
    @PostMapping("/{id}/buy")
    public Mono<Rendering> cartBuy(@PathVariable("id") Long cartId, Model model) {
        Mono<Model> monoModel = purchaseFcdService.purchase(model, cartId);
        return monoModel.map(model1 -> Rendering.view("order")
                .model(model.asMap())
                .build());
    }

    // READY
    @PostMapping("/{id}/item/{itemId}/changecount")
    public String cartBuy(@PathVariable("id") Long cartId, @PathVariable("itemId") Long itemId, @RequestParam("action") String action, Model model) {
        cartChangeFcdService.updateItemInCart(cartId, itemId, action);
        return "redirect:/cart/1";
    }

}
