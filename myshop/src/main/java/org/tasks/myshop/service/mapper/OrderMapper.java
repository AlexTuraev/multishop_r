package org.tasks.myshop.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.tasks.myshop.dao.model.OrderEntity;
import org.tasks.myshop.dto.OrderDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ItemMapper.class})
public interface OrderMapper {

    OrderDto toDto(OrderEntity order);

}
