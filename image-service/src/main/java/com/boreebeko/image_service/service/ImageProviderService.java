package com.boreebeko.image_service.service;

import com.boreebeko.image_service.domain.image.ImageEntity;
import com.boreebeko.image_service.repository.ImageRepository;
import com.boreebeko.image_service.service.props.MinioProperties;
import com.boreebeko.image_service.web.dto.ImageDTO;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageProviderService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    private final ImageRepository imageRepository;

    public ImageProviderService(MinioClient minioClient, MinioProperties minioProperties, ImageRepository imageRepository) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.imageRepository = imageRepository;
    }

    public List<ImageDTO> getAllImages(String userId) {

        List<ImageEntity> images = imageRepository.getImagesByUserID(userId);
        List<String> fileNames = images.stream().map(ImageEntity::getFileName).toList();

        List<ImageDTO> imageDTOList = new ArrayList<>();

        try {
            for (String fileName: fileNames) {
                byte[] bytes = getImagesFromMinio(fileName);
                imageDTOList.add(new ImageDTO(fileName, bytes));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return imageDTOList;
    }

    private byte[] getImagesFromMinio(String fileName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder().bucket(minioProperties.getBucket()).object(fileName).build()
        ).readAllBytes();
    }
}
