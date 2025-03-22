package com.boreebeko.forum_service_v2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
    @SequenceGenerator(
            name = "comments_seq",
            sequenceName = "comments_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Setter
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    @Setter
    private PostType postType;

    @Column(name = "post_id", nullable = false)
    @Setter
    private Long postId;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String body;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
