package org.tasks.myshop.dao.model.complexid;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderEntityId implements Serializable {

    private Long orderId;
    private Long itemId;

}
