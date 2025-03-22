package com.boreebeko.forum_service_v2.repository;

import com.boreebeko.forum_service_v2.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
