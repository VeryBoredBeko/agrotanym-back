package com.boreebeko.forum_service.mapper;

import com.boreebeko.forum_service.domain.Question;
import com.boreebeko.forum_service.dto.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper extends Mappable<Question, QuestionDTO> {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
}
