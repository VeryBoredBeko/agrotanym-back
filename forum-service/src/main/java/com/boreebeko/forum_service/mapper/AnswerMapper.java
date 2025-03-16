package com.boreebeko.forum_service.mapper;

import com.boreebeko.forum_service.domain.Answer;
import com.boreebeko.forum_service.dto.AnswerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper extends Mappable<Answer, AnswerDTO> {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);
}
