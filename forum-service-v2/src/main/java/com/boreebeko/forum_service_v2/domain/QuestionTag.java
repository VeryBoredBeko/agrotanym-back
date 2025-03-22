package com.boreebeko.forum_service_v2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_tags")
@Getter
@NoArgsConstructor
public class QuestionTag {

    @EmbeddedId
    private QuestionTagId id = new QuestionTagId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public QuestionTag(Question question, Tag tag) {
        this.question = question;
        this.tag = tag;
        this.id = new QuestionTagId(question.getId(), tag.getId());
    }
}

