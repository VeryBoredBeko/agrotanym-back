package com.boreebeko.forum_service_v2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class QuestionDTO {

    private Long id;

    // to set the tag while creating entity
    private List<Long> tagIdList;
    // for returning and showing to end-user purposes only
    private List<TagDTO> tagDTOList;

    private String title;
    private String body;
    private Integer views;
    private Integer votesCount;
    private Integer answersCount;
    private Boolean isClosed;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
