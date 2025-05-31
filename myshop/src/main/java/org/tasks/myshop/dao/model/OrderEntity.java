package org.tasks.myshop.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
//@IdClass(OrderEntityId.class)
public class OrderEntity {

    @Column("order_id")
    private Long orderId;

    @Column("item_id")
    private Long itemId;

    @Column("count_item")
    private Integer countItem;

    @Column("price")
    private BigDecimal price;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id", insertable=false, updatable=false)
    private ItemEntity item;

    public OrderEntity orderId(Long newOrderId) {
        this.orderId = newOrderId;
        return this;
    }

}
