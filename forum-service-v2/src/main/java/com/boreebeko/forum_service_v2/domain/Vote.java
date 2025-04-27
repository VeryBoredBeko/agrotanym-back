package com.boreebeko.forum_service_v2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_type", "post_id"})
})
@Getter
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votes_seq")
    @SequenceGenerator(name = "votes_seq", sequenceName = "votes_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Setter
    private UUID userId;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "post_type", nullable = false)
    @Setter
    private PostType postType;

    @Column(name = "post_id", nullable = false)
    @Setter
    private Long postId;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "vote_type", nullable = false)
    @Setter
    private VoteType voteType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
