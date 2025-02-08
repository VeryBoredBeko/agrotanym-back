package com.boreebeko.image_service.service.event;

public class ImageUploadedEvent {

    private String fileName;

    public ImageUploadedEvent() {
    }

    public ImageUploadedEvent(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
