package com.boreebeko.image_service.service;

import com.boreebeko.image_service.client.ImageProcessingClient;
import com.boreebeko.image_service.domain.exception.ContentTypeNotIdentifiedException;
import com.boreebeko.image_service.domain.exception.ImageDeleteException;
import com.boreebeko.image_service.domain.exception.ImageNotFoundException;
import com.boreebeko.image_service.domain.exception.ImageUploadException;
import com.boreebeko.image_service.domain.image.ImageEntity;
import com.boreebeko.image_service.repository.ImageRepository;
import com.boreebeko.image_service.service.mapper.ImageMapper;
import com.boreebeko.image_service.service.props.MinioProperties;
import com.boreebeko.image_service.web.dto.ImageClassificationResponse;
import com.boreebeko.image_service.web.dto.ImageDTO;
import com.boreebeko.image_service.web.dto.ImageURLRequest;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

/**
 * Service for managing user images using MinIO Object Storage
 * and PostgresSQL Database
 *
 * <p>
 *     This service provides operations to upload, delete and fetch images.
 * </p>
 *
 * @author Tumenov Beknur
 * @version 1.0
 * @since 27.03.2025
 * */
@Service
public class MinioStorageService implements ImageStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper = ImageMapper.INSTANCE;

    private final ImageProcessingClient imageProcessingClient;

    // Number of elements in one page, while making request to database
    private static final int PAGE_SIZE = 10;

    @Autowired
    public MinioStorageService(MinioClient minioClient, MinioProperties minioProperties, ImageRepository imageRepository, ImageProcessingClient imageProcessingClient) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.imageRepository = imageRepository;
        this.imageProcessingClient = imageProcessingClient;
    }

    /**
     * Uploads user image to a database and MinIO storage
     *
     * @param image MultipartFile object uploaded by user, which represents image
     * @throws ContentTypeNotIdentifiedException if image content-type is not supported
     * @throws ImageUploadException if image was not uploaded to a storage and database
     * */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public ImageDTO upload(MultipartFile image) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        boolean uploadedToMinio = false;
        String imageName = null;

        try {
            // TODO: Refactor this line, common sense is to run this method only once when class is constructed
            createBucketIfNotExists();

            imageName = generateUniqueImageName();
            String imageExtension = getImageExtension(image.getOriginalFilename());
            String contentType = defineContentType(imageExtension);

            ImageEntity record = new ImageEntity(userId, imageName, contentType);
            record.setUrl(minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/" + imageName);

            uploadImageToMinio(image.getInputStream(), imageName, contentType);
            uploadedToMinio = true;

            ImageClassificationResponse response = imageProcessingClient.classifyImage(new ImageURLRequest(record.getUrl()));
            record.setClassifiedLabel(response.getLabel());
            ImageEntity persistedEntity = imageRepository.save(record);
            return imageMapper.toDTO(persistedEntity);

        } catch (ContentTypeNotIdentifiedException exception) {
            throw exception;
        } catch (Exception exception) {
            if (uploadedToMinio && imageName != null) {
                try {
                    deleteImageFromMinio(imageName);
                } catch (Exception compensationException) {
                    System.err.println("Failed to compensate by deleting the image: " + compensationException.getMessage());
                }

            }
            throw new ImageUploadException(exception.getMessage());
        }
    }

    /**
     * Delete user uploaded image by its unique ID
     *
     * @param id The unique ID of image
     * @throws AccessDeniedException if user has not accessed to delete this image
     * @throws ImageDeleteException if image delete operation is not performed
     * */
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

    /**
     * Fetches user uploaded images from database
     *
     * @return List of user uploaded image DTO objects
     * */
    @Transactional(readOnly = true)
    @Override
    public List<ImageDTO> listImages(int page) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        return imageMapper.toDTOList(imageRepository.findImagesByUserID(userId, pageable).stream().toList());
    }

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
        return switch (imageExtension.toLowerCase()) {
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            default -> throw new ContentTypeNotIdentifiedException("Couldn't define image content type");
        };
    }
}
