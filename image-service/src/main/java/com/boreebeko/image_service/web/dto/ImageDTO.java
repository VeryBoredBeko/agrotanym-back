package com.boreebeko.image_service.web.dto;

public class ImageDTO {

    private String fileName;
    private byte[] bytes;

    public ImageDTO(String fileName, byte[] bytes) {
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public ImageDTO() {
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
