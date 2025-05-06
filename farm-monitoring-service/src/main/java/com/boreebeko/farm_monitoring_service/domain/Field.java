package com.boreebeko.farm_monitoring_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Polygon;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a monitored agricultural field in the system.
 *
 * <p>
 *     This entity holds metadata about a field such as its owner, name.
 * </p>
 */
@Entity
@Table(name = "fields")
@NoArgsConstructor
@Getter
public class Field implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "owner_id")
    private UUID ownerId;

    @Setter
    private String name;

    @Setter
    private String crop;

    @Setter
    private String hybrid;

    @Setter
    private LocalDate sowingDate;

    @Setter
    private Double area;

    @Setter
    private String soilType;

    @Setter
    private String tillageType;

    @Setter
    private String manager;

    @Column(name = "territory", columnDefinition = "geometry(Polygon,4326)", nullable = false)
    @Setter
    private Polygon territory;
}
