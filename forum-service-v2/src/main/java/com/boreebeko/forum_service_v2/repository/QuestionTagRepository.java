package com.boreebeko.forum_service_v2.repository;

import com.boreebeko.forum_service_v2.domain.Question;
import com.boreebeko.forum_service_v2.domain.QuestionTag;
import com.boreebeko.forum_service_v2.domain.QuestionTagId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, QuestionTagId> {
    List<QuestionTag> findQuestionTagByQuestionId(Long questionId);

    @Query("SELECT q FROM Question q " +
            "JOIN QuestionTag qt ON qt.id.questionId = q.id " +
            "WHERE qt.id.tagId = :tagId")
    Page<Question> findQuestionsByTagId(@Param("tagId") Long tagId, Pageable pageable);
}
