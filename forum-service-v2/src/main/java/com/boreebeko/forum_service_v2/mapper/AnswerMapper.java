package com.boreebeko.forum_service_v2.mapper;

import com.boreebeko.forum_service_v2.domain.Answer;
import com.boreebeko.forum_service_v2.dto.AnswerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper extends Mappable<Answer, AnswerDTO> {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);
}
