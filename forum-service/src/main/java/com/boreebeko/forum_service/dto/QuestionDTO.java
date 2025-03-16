package com.boreebeko.forum_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class QuestionDTO {

    private Long id;
    private UUID userId;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy:MM:dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy:MM:dd HH:mm")
    private LocalDateTime updatedAt;
}
