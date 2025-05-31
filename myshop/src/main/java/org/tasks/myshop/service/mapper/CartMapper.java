package org.tasks.myshop.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.dto.CartDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ItemMapper.class})
public interface CartMapper {

    CartDto toDto(CartEntity entity);

}
