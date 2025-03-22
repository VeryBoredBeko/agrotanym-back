package com.boreebeko.forum_service_v2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTagId implements Serializable {

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "tag_id")
    private Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionTagId)) return false;
        QuestionTagId that = (QuestionTagId) o;
        return questionId.equals(that.questionId) && tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, tagId);
    }
}

