package com.boreebeko.forum_service_v2.repository;

import com.boreebeko.forum_service_v2.domain.Question;
import com.boreebeko.forum_service_v2.dto.QuestionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Modifying
    @Query("UPDATE Question q SET q.votesCount = COALESCE(q.votesCount, 0) + 1 WHERE q.id = :questionId")
    void upVoteToQuestion(@Param("questionId") Long questionId);

    @Modifying
    @Query("UPDATE Question q SET q.votesCount = COALESCE(q.votesCount, 0) - 1 WHERE q.id = :questionId")
    void downVoteToQuestion(@Param("questionId") Long questionId);

    @Modifying
    @Query("UPDATE Question q SET q.isClosed = true WHERE q.id = :questionId")
    void closeQuestion(@Param("questionId") Long questionId);

    @Query("SELECT new com.boreebeko.forum_service_v2.dto.QuestionDTO(q.id, q.userId) FROM Question q WHERE q.id = :questionId")
    Optional<QuestionDTO> getQuestionDTO(@Param("questionId") Long questionId);

    Page<Question> findQuestionByUserId(UUID userId, Pageable pageable);

    boolean existsByIdAndUserId(Long questionId, UUID userId);
}
