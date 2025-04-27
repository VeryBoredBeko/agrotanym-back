package com.boreebeko.forum_service_v2.dto;

import com.boreebeko.forum_service_v2.dto.validation.OnCreate;
import com.boreebeko.forum_service_v2.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "User posted question DTO")
@NoArgsConstructor
@Data
public class QuestionDTO {

    @Schema(description = "Question ID", example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "User ID", example = "24dc13c3-3e04-47a5-b096-a25d7df6ae7e")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID userId;

    // to set the tag while creating entity
    @NotEmpty(groups = {OnCreate.class}, message = "List of tag id must not be empty")
    private List<Long> tagIdList;

    // for returning and showing to end-user purposes only
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<TagDTO> tagDTOList;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isOwner;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isVoted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private VoteType voteType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Question title must not be empty")
    @Size(min = 8, max = 255, groups = {OnCreate.class, OnUpdate.class}, message = "The question title must be between 8 and 255 characters long")
    private String title;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Question body must not be empty")
    @Size(min = 8, groups = {OnCreate.class, OnUpdate.class}, message = "The question body must be at least 8 characters long")
    private String body;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer views;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer votesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer answersCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isClosed;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private ZonedDateTime updatedAt;

    // this constructor is called when mapping to QuestionDTO,
    // while @Query annotated function called in QuestionRepository
    public QuestionDTO(Long id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }
}
