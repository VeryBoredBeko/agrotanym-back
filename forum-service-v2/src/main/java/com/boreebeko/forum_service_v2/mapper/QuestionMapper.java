package com.boreebeko.forum_service_v2.mapper;

import com.boreebeko.forum_service_v2.domain.Question;
import com.boreebeko.forum_service_v2.dto.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper extends Mappable<Question, QuestionDTO> {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
}
