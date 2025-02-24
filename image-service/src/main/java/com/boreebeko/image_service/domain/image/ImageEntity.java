package com.boreebeko.image_service.domain.image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "images")
@NoArgsConstructor
@Getter
public class ImageEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "userID")
    @Setter
    private String userID;

    @Column(name = "image_name")
    @Setter
    private String imageName;

    @Column(name = "content_type")
    @Setter
    private String contentType;

    @Column(name = "url")
    @Setter
    private String url;

    @Column(name = "classified_label")
    @Setter
    private String classifiedLabel;

    @Column(name = "processed_at")
    @CreationTimestamp
    private LocalDateTime processedAt;

    public ImageEntity(String userID, String imageName, String contentType) {
        this.userID = userID;
        this.imageName = imageName;
        this.contentType = contentType;
    }
}
