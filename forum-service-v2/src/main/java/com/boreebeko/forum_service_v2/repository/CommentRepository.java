package com.boreebeko.forum_service_v2.repository;

import com.boreebeko.forum_service_v2.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByPostId(Long postId);
}
