package org.tasks.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartId;

    private Long itemId;

    private Integer countItem;

    private ItemDto item;

}
