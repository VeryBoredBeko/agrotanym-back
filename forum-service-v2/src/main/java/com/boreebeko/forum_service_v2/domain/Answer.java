package com.boreebeko.forum_service_v2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "answers")
@Getter
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answers_seq")
    @SequenceGenerator(name = "answers_seq", sequenceName = "answers_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    @Setter
    private Question question;

    @Column(name = "user_id", nullable = false)
    @Setter
    private UUID userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String body;

    @Column(name = "is_accepted", nullable = false)
    @Setter
    private Boolean isAccepted = false;

    @Column(name = "votes_count", nullable = false)
    @Setter
    private Integer votesCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}

