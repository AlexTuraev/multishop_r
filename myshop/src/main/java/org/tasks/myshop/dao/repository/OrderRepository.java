package org.tasks.myshop.dao.repository;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.tasks.myshop.dao.model.OrderEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<OrderEntity, Long> {

    @Query(value = "SELECT nextval('order_sequence')")
    Mono<Long> getNextOrderId();

    @Query(value = """
        SELECT o.order_id as orderId,
               o.count_item as countItem,
               o.price as price,
               o.item_id as itemId,
               it.title as title,
               it.description as description
            FROM orders o
            LEFT JOIN items it ON it.id = o.item_id
    """)
    Flux<?> findModelOrders();

    @Query("""
        SELECT o FROM OrderEntity o
            LEFT JOIN FETCH o.item
                LEFT JOIN FETCH o.item.itemPics WHERE o.orderId=:orderId
    """)
    Flux<OrderEntity> findAllWhereId(Long orderId);
}
