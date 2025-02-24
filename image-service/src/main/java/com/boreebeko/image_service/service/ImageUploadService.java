package com.boreebeko.image_service.service;

import com.boreebeko.image_service.domain.exception.ImageUploadException;
import com.boreebeko.image_service.domain.image.Image;
import com.boreebeko.image_service.domain.image.ImageEntity;
import com.boreebeko.image_service.repository.ImageRepository;
import com.boreebeko.image_service.service.props.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

//@Service
public class ImageUploadService {

//    // TODO: Change the code
//
//    private final MinioClient minioClient;
//    private final MinioProperties minioProperties;
//
//    private final ImageRepository imageRepository;
//
//    public ImageUploadService(MinioClient minioClient, MinioProperties minioProperties, ImageRepository imageRepository) {
//        this.minioClient = minioClient;
//        this.minioProperties = minioProperties;
//        this.imageRepository = imageRepository;
//    }
//
//    public String upload(Image image, String userID) {
//
//        try {
//            createBucket();
//        } catch (Exception e) {
//            throw new ImageUploadException(e.getMessage());
//        }
//
//        MultipartFile file = image.getMultipartFile();
//
//        if (file.isEmpty() || file.getOriginalFilename() == null) {
//            throw new ImageUploadException("Image must have name.");
//        }
//
//        String fileName = generateFileName(file);
//        InputStream inputStream;
//
//        try {
//            inputStream = file.getInputStream();
//        } catch (Exception exception) {
//            throw new ImageUploadException("Image upload failed: " + exception.getMessage());
//        }
//
//        try {
//            saveImage(inputStream, fileName);
//        } catch (Exception exception) {
//            throw new ImageUploadException("Image upload failed: " + exception.getMessage());
//        }
//
//        ImageEntity imageEntity = new ImageEntity(userID, fileName);
//        imageRepository.save(imageEntity);
//
//        return fileName;
//    }
//
//    private void saveImage(InputStream inputStream, String fileName) throws Exception {
//
//        minioClient.putObject(
//                PutObjectArgs
//                        .builder()
//                        .bucket(minioProperties.getBucket())
//                        .stream(inputStream, inputStream.available(), -1)
//                        .object(fileName)
//                        .build()
//        );
//    }
//
//    private String generateFileName(MultipartFile file) {
//
//        String extension = getExtension(file);
//        return UUID.randomUUID() + "." + extension;
//    }
//
//    private String getExtension(MultipartFile file) {
//        return file.getOriginalFilename()
//                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//    }
//
//    private void createBucket() throws Exception {
//
//        boolean exists = minioClient.bucketExists(
//                BucketExistsArgs
//                        .builder()
//                        .bucket(minioProperties.getBucket())
//                        .build()
//        );
//
//        if (!exists)
//            minioClient.makeBucket(
//                    MakeBucketArgs
//                            .builder()
//                            .bucket(minioProperties.getBucket())
//                            .build()
//            );
//    }
}
