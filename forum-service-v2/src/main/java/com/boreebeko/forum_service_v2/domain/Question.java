package com.boreebeko.forum_service_v2.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_seq")
    @SequenceGenerator(name = "questions_seq", sequenceName = "questions_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Setter
    private UUID userId;

    @Column(nullable = false)
    @Setter
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String body;

    @Column(nullable = false)
    @Setter
    private Integer views = 0;

    @Column(name = "votes_count", nullable = false)
    @Setter
    private Integer votesCount = 0;

    @Column(name = "answers_count", nullable = false)
    @Setter
    private Integer answersCount = 0;

    @Column(name = "is_closed", nullable = false)
    @Setter
    private Boolean isClosed = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}

