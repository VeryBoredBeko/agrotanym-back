package com.boreebeko.forum_service_v2.repository;

import com.boreebeko.forum_service_v2.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId")
    Page<Answer> findAnswersByQuestionId(@Param("questionId") Long questionId, Pageable pageable);

    @Modifying
    @Query("UPDATE Answer a SET a.isAccepted = true WHERE a.id = :answerId")
    void acceptAnswer(@Param("answerId") Long answerId);

    boolean existsByIdAndUserId(Long answerId, UUID userId);
}
