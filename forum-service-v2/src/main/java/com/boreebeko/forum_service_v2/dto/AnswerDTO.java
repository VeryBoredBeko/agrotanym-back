package com.boreebeko.forum_service_v2.dto;

import com.boreebeko.forum_service_v2.dto.validation.OnCreate;
import com.boreebeko.forum_service_v2.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class AnswerDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID userId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Answer body must not be empty")
    @Size(min = 8, groups = {OnCreate.class, OnUpdate.class}, message = "The question body must be at least 8 characters long")
    private String body;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isAccepted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer votesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isCurrentUserAnswer;
}
