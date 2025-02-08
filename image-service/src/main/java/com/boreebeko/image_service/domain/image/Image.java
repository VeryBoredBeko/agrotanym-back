package com.boreebeko.image_service.domain.image;

import org.springframework.web.multipart.MultipartFile;

public class Image {

    private MultipartFile multipartFile;

    public Image(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
