package com.boreebeko.forum_service_v2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class AnswerDTO {

    private Long id;
    private UUID userId;
    private String body;
    private Boolean isAccepted;
    private Integer votesCount;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private boolean isCurrentUserAnswer;
}
