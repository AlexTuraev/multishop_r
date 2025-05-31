package org.tasks.myshop.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "cart")
//@IdClass(CartEntityId.class)
public class CartEntity {

    @Column("cart_id")
    private Long cartId;

    @Column("item_id")
    private Long itemId;

    @Column("count_item")
    private Integer countItem;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id", insertable=false, updatable=false)
    @Transient
    private ItemEntity item;

}
