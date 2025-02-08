package com.boreebeko.image_service.domain.image;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "userID")
    private String userID;

    @Column(name = "fileName")
    private String fileName;

    public ImageEntity() {
    }

    public ImageEntity(String userID, String fileName) {
        this.userID = userID;
        this.fileName = fileName;
    }

    public UUID getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "id=" + id +
                ", userID=" + userID +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
