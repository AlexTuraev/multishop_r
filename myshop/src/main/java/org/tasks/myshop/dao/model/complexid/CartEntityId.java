package org.tasks.myshop.dao.model.complexid;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartEntityId implements Serializable {

    private Long itemId;
    private Long cartId;

}
