package com.boreebeko.forum_service.repository;

import com.boreebeko.forum_service.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostsByUserId(UUID userId);
}
