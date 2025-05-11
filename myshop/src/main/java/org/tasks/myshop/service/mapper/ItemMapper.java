package org.tasks.myshop.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dto.ItemDto;
import org.tasks.myshop.dto.ItemPicsDto;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ItemPicsMapper.class})
public interface ItemMapper {

    ItemDto toDto(ItemEntity entity);
    List<ItemDto> toDto(List<ItemEntity> entity);
    ItemEntity toEntity(ItemDto dto);
    List<ItemEntity> toEntity(List<ItemDto> dto);

    @AfterMapping
    default void afterMapping(@MappingTarget ItemDto itemDto) {
        ItemPicsDto itemPicsDto = itemDto.getItemPics();
        if (itemPicsDto == null) {
            return;
        }

        if(itemDto.getItemPics().getImage() != null && itemDto.getItemPics().getImageType() != null) {
            itemDto.setBase64Image("data:" + itemDto.getItemPics().getImageType() + ";base64," + Base64.getEncoder().encodeToString(itemDto.getItemPics().getImage()));
        }
    }

}
