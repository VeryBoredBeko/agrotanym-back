package com.boreebeko.forum_service_v2.repository;

import com.boreebeko.forum_service_v2.domain.PostType;
import com.boreebeko.forum_service_v2.domain.Vote;
import com.boreebeko.forum_service_v2.domain.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByPostIdAndUserId(Long postId, UUID userId);

    Optional<Vote> findByPostIdAndUserId(Long postId, UUID userId);

    @Query("SELECT v.voteType FROM Vote v WHERE v.userId = :userId AND v.postId = :postId AND v.postType = :postType")
    Optional<VoteType> findVoteTypeByUserIdAndPostIdAndPostType(
            @Param("userId") UUID userId,
            @Param("postId") Long postId,
            @Param("postType") PostType postType
    );


}
