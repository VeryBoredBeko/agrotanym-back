package com.boreebeko.forum_service_v2.mapper;

import com.boreebeko.forum_service_v2.domain.Tag;
import com.boreebeko.forum_service_v2.dto.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper extends Mappable<Tag, TagDTO> {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);
}
