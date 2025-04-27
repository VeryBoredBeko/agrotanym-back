package com.boreebeko.farm_monitoring_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "markers")
@NoArgsConstructor
@Getter
public class Marker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    @Setter
    private Field field;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter
    private String imageURL;

    @Column(name = "location", columnDefinition = "geometry(Point,4326)", nullable = false)
    @Setter
    private Point location;
}
