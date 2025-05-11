package org.tasks.myshop.dao.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dao.model.ItemModel;
import reactor.core.publisher.Flux;

@Repository
public interface ItemRespository extends R2dbcRepository<ItemEntity, Long> {
    @Query("""
        SELECT item FROM ItemEntity item LEFT JOIN FETCH item.itemPics WHERE item.title LIKE :search%
    """)
//    Mono<Page<ItemEntity>> findByTitle(String search, Pageable pageable);
    Flux<ItemEntity> findByTitle(String search);

//    @Query("""
//        SELECT item FROM ItemEntity item LEFT JOIN FETCH item.itemPics WHERE item.title LIKE :search% AND item.quantity >= :minQuantity
//    """)
//    Mono<Page<ItemEntity>> findByTitleAndOverMinQuantity(String search, Pageable pageable, int minQuantity);

    /*@Query("""
        SELECT new org.tasks.myshop.dao.model.ItemModel(item, coalesce(c.countItem, 0) )
            FROM ItemEntity item
            LEFT JOIN CartEntity c ON c.itemId = item.id
            LEFT JOIN FETCH item.itemPics
                WHERE item.title LIKE :search% AND item.quantity >= :minQuantity
                    AND (c.countItem IS NULL OR c.cartId = 1)
    """)*/
    @Query("""
        SELECT new org.tasks.myshop.dao.model.ItemModel(
        new org.tasks.myshop.dao.model.ItemEntity(item.id as id, item.title as title, item.description as description, item.price as price, item.quantity as quantity), 
        coalesce(c.countItem, 0) )
            FROM items item
            LEFT JOIN cart c ON c.item_id = item.id

                WHERE item.title LIKE :search AND item.quantity >= :minQuantity
                    AND (c.count_item IS NULL OR c.cart_id = 1)
    """)
//    Mono<Page<ItemModel>> findByTitleAndOverMinQuantityNew(String search, Pageable pageable, int minQuantity);
    Flux<ItemModel> findByTitleAndOverMinQuantityNew(String search, int minQuantity);
}
