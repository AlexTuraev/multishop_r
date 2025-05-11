package org.tasks.myshop.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ItemModel {

    private ItemEntity item;
    private Integer countInCart;

    public ItemModel(ItemEntity item, Integer countInCart) {
        this.item = item;
        this.countInCart = countInCart;
    }
}
