package org.tasks.myshop.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_pics")
public class ItemPicsEntity {

    @Column("item_id")
    private Long itemId;

    @Column("image_type")
    private String imageType;

    @Column("image")
    private byte[] image;

}
