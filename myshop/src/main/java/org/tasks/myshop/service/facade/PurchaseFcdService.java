package org.tasks.myshop.service.facade;

import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

public interface PurchaseFcdService {

    public Mono<Model> purchase(Model model, Long cartId);

}
