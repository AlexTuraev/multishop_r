package org.tasks.myshop.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.dao.model.OrderEntity;
import org.tasks.myshop.dto.CartDto;
import org.tasks.myshop.dto.OrderDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartOrderMapper {

    @Mapping(target = "price", source = "cart.item.price")
    OrderEntity cartToOrderEntity(CartEntity cart);

    OrderDto cartToOrderDto(CartDto cartDto);

}
