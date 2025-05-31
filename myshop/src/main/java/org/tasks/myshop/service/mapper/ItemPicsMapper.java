package org.tasks.myshop.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.tasks.myshop.dao.model.ItemPicsEntity;
import org.tasks.myshop.dto.ItemPicsDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemPicsMapper {

    ItemPicsDto toDto(ItemPicsEntity entity);

}
