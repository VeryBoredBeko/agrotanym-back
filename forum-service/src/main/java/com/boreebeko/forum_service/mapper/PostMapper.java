package com.boreebeko.forum_service.mapper;

import com.boreebeko.forum_service.domain.Post;
import com.boreebeko.forum_service.dto.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper extends Mappable<Post, PostDTO> {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
}
