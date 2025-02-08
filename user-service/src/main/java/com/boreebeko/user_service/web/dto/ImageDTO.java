package com.boreebeko.user_service.web.dto;

public class ImageDTO {

    private String fileName;
    private byte[] bytes;

    public ImageDTO() {
    }

    public ImageDTO(String fileName, byte[] bytes) {
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getFileName() {
        return fileName;
    }
}
