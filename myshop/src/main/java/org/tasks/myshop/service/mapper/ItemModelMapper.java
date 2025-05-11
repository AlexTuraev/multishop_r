package org.tasks.myshop.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.tasks.myshop.dao.model.ItemModel;
import org.tasks.myshop.dto.ItemModelDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ItemMapper.class})
public interface ItemModelMapper {

    ItemModelDto toDto(ItemModel model);

}
