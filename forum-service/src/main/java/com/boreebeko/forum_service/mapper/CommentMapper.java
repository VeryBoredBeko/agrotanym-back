package com.boreebeko.forum_service.mapper;

import com.boreebeko.forum_service.domain.Comment;
import com.boreebeko.forum_service.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper extends Mappable<Comment, CommentDTO> {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
}
