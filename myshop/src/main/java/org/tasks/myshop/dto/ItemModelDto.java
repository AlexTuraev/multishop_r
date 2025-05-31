package org.tasks.myshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemModelDto {

    private ItemDto item;

    private Integer countInCart;

}
