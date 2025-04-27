package com.boreebeko.farm_monitoring_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.UUID;

@Entity
@Table(name = "field_roles")
@NoArgsConstructor
@Getter
public class FieldRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    @Setter
    private Field field;

    @Setter
    private UUID userId;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Setter
    private Role role;

    public enum Role {
        OWNER,
        WORKER
    }
}
