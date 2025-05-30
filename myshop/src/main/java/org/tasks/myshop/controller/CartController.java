package org.tasks.myshop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import org.tasks.myshop.dao.model.UserEntity;
import org.tasks.myshop.dao.repository.UserRepository;
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
    private final UserRepository userRepository;

    public CartController(CartService cartService, PurchaseFcdService purchaseFcdService, CartChangeFcdService cartChangeFcdService, UserRepository userRepository) {
        this.cartService = cartService;
        this.purchaseFcdService = purchaseFcdService;
        this.cartChangeFcdService = cartChangeFcdService;
        this.userRepository = userRepository;
    }

    // READY
    @GetMapping("/{id}")
    public Mono<Rendering> getCartByCartId(@PathVariable("id") Long cartId, Model model, Authentication authentication) {
        var userName = authentication.getName();
        UserEntity user = userRepository.findByUsername(userName).blockFirst();

        Mono<Model> monoModel = cartService.getModelByCartId(model, user.getCartId());

        return monoModel.map(model1 -> {
            Rendering r = Rendering.view("cart")
                    .model(model1.asMap())
                    .build();
            return r;
        });
    }

    // READY
    @PostMapping("/{id}/buy")
    public Mono<Rendering> cartBuy(@PathVariable("id") Long cartId, Model model, Authentication authentication) {
        var userName = authentication.getName();
        UserEntity user = userRepository.findByUsername(userName).blockFirst();

        Mono<Model> monoModel = purchaseFcdService.purchase(model, user.getCartId());
        return monoModel.map(model1 -> Rendering.view("order")
                .model(model.asMap())
                .build());
    }

    // READY
    @PostMapping("/{id}/item/{itemId}/changecount")
    public String cartBuy(@PathVariable("id") Long cartId, @PathVariable("itemId") Long itemId, @RequestParam("action") String action, Model model, Authentication authentication) {
        var userName = authentication.getName();
        UserEntity user = userRepository.findByUsername(userName).blockFirst();

        cartChangeFcdService.updateItemInCart(user.getCartId(), itemId, action);
        return "redirect:/cart/1";
    }

}
