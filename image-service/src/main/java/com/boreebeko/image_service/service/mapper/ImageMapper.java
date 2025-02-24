package com.boreebeko.image_service.service.mapper;

import com.boreebeko.image_service.domain.image.ImageEntity;
import com.boreebeko.image_service.web.dto.ImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImageMapper extends Mappable<ImageEntity, ImageDTO> {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);
}
