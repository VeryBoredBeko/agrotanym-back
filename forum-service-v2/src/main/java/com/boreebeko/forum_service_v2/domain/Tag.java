package com.boreebeko.forum_service_v2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_seq")
    @SequenceGenerator(
            name = "tags_seq",
            sequenceName = "tags_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Setter
    private String name;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}

