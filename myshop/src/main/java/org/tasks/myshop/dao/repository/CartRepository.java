package org.tasks.myshop.dao.repository;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.tasks.myshop.dao.model.CartEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CartRepository extends ReactiveCrudRepository<CartEntity, Long> {

    Mono<CartEntity> findByItemIdAndCartId(Long itemId, Long cartId);

    Mono<Void> deleteByItemIdAndCartId(Long itemId, Long cartId);

    @Query("""
        SELECT ct FROM CartEntity ct 
            JOIN FETCH ct.item 
                LEFT JOIN FETCH ct.item.itemPics WHERE ct.cartId=:cartId
    """)
    Flux<CartEntity> getCartModelByCartId(Long cartId);

}
