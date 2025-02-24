package com.boreebeko.image_service.service;

import com.boreebeko.image_service.domain.exception.ContentTypeNotIdentifiedException;
import com.boreebeko.image_service.domain.exception.ImageDeleteException;
import com.boreebeko.image_service.domain.exception.ImageNotFoundException;
import com.boreebeko.image_service.domain.exception.ImageUploadException;
import com.boreebeko.image_service.domain.image.ImageEntity;
import com.boreebeko.image_service.repository.ImageRepository;
import com.boreebeko.image_service.service.mapper.ImageMapper;
import com.boreebeko.image_service.service.props.MinioProperties;
import com.boreebeko.image_service.web.dto.ImageDTO;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MinioStorageService implements ImageStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper = ImageMapper.INSTANCE;

    @Autowired
    public MinioStorageService(MinioClient minioClient, MinioProperties minioProperties, ImageRepository imageRepository) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.imageRepository = imageRepository;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void upload(MultipartFile image) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        try {
            // TODO: Refactor this line, common sense is to run this method only once when class is constructed
            createBucketIfNotExists();

            String imageName = generateUniqueImageName();
            String imageExtension = getImageExtension(image.getOriginalFilename());
            String contentType = defineContentType(imageExtension);

            ImageEntity record = new ImageEntity(userId, imageName, contentType);
            record.setUrl(minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/" + imageName);
            record.setClassifiedLabel("NOT PROCESSED");
            imageRepository.save(record);

            uploadImageToMinio(image.getInputStream(), imageName, contentType);

        } catch (ContentTypeNotIdentifiedException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ImageUploadException(exception.getMessage());
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(UUID id) {

        // TODO: Refactor this method

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        try {
            ImageEntity record = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);

            if (record.getUserID().compareTo(userId) != 0) throw new AccessDeniedException("Access Denied");

            deleteImageFromMinio(record.getImageName());
            imageRepository.deleteById(id);
        } catch (AccessDeniedException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ImageDeleteException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ImageDTO> listImages() {
        return imageMapper.toDTOList(imageRepository.findAll());
    }

    // TODO: Refactor hardcoded content-type while uploading
    private void uploadImageToMinio(InputStream inputStream, String imageName, String contentType) throws Exception {

        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(minioProperties.getBucket())
                        .object(imageName)
                        .contentType(contentType)
                        .stream(inputStream, inputStream.available(), -1)
                        .build()
        );
    }

    private void deleteImageFromMinio(String imageName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(minioProperties.getBucket())
                        .object(imageName)
                        .build()
        );
    }

    private void createBucketIfNotExists() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs
                        .builder()
                        .bucket(minioProperties.getBucket())
                        .build()
        );

        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        }
    }

    private String generateUniqueImageName() {
        return UUID.randomUUID().toString();
    }

    private String getImageExtension(String originalFileName) {
        return Objects.requireNonNull(originalFileName).substring(
                originalFileName.lastIndexOf('.') + 1
        );
    }

    private String defineContentType(String imageExtension) {
        return switch (imageExtension) {
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> throw new ContentTypeNotIdentifiedException("Couldn't define image content type");
        };
    }
}
