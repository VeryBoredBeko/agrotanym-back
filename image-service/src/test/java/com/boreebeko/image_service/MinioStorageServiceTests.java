package com.boreebeko.image_service;

import com.boreebeko.image_service.client.ImageProcessingClient;
import com.boreebeko.image_service.domain.image.ImageEntity;
import com.boreebeko.image_service.repository.ImageRepository;
import com.boreebeko.image_service.service.MinioStorageService;
import com.boreebeko.image_service.service.props.MinioProperties;
import com.boreebeko.image_service.web.dto.ImageClassificationResponse;
import com.boreebeko.image_service.web.dto.ImageDTO;
import io.minio.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MinioStorageServiceTests {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @Mock
    private ImageProcessingClient imageProcessingClient;

    @InjectMocks
    private MinioStorageService minioStorageService;

    private final String userId = "24dc13c3-3e04-47a5-b096-a25d7df6ae7e";

    @BeforeEach
    void setUpSecurityContext() {

        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getClaimAsString("sub")).thenReturn(userId);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getUserUploadedListOfImages() {

        ImageEntity firstImage = new ImageEntity(userId, "first-image", "image/jpeg");
        firstImage.setUrl("http://<minio-ip>:<minio-port>/first-image");

        ImageEntity secondImage = new ImageEntity(userId, "second-image", "image/png");
        secondImage.setUrl("http://<minio-ip>:<minio-port>/second-image");

        List<ImageEntity> mockImageEntityList = List.of(firstImage, secondImage);
        Page<ImageEntity> mockImageEntityPage = new PageImpl<>(mockImageEntityList);

        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(imageRepository.findImagesByUserID(userId, pageable)).thenReturn(mockImageEntityPage);

        List<ImageDTO> resultImageDTOList = minioStorageService.listImages(0);

        Assertions.assertEquals(2, resultImageDTOList.size());
    }

    @Test
    void testUploadingUserImageToMinio() throws Exception {

        Mockito.when(minioProperties.getBucket()).thenReturn("bucket");
        Mockito.when(minioProperties.getUrl()).thenReturn("http://<minio-ip>:<minio-port>");

        Mockito.when(minioClient.bucketExists(Mockito.any(BucketExistsArgs.class))).thenReturn(true);

        ObjectWriteResponse dummyResponse = Mockito.mock(ObjectWriteResponse.class);

        Mockito.when(minioClient.putObject(Mockito.any(PutObjectArgs.class)))
                .thenReturn(dummyResponse);

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

        String imageFilename = "image.jpg";
        byte[] fileBytes = imageFilename.getBytes();

        Mockito.when(multipartFile.getOriginalFilename()).thenReturn(imageFilename);
        Mockito.when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileBytes));

        ImageClassificationResponse classificationResponse = new ImageClassificationResponse();
        classificationResponse.setLabel("Apple: Healthy");

        Mockito.when(imageProcessingClient.classifyImage(Mockito.argThat(request ->
                request.getUrl().matches("http://.*")))).thenReturn(classificationResponse);

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setContentType("image/jpeg");
        imageEntity.setClassifiedLabel("Apple: Healthy");

        Mockito.when(imageRepository.save(Mockito.any(ImageEntity.class))).thenReturn(imageEntity);

        ImageDTO resultImageDTO = minioStorageService.upload(multipartFile);

        Assertions.assertEquals("image/jpeg", resultImageDTO.getContentType());
        Assertions.assertEquals("Apple: Healthy", resultImageDTO.getClassifiedLabel());
    }

    @Test
    void testExistingImageDeletion() throws Exception {

//        ImageEntity mockImageEntity = new ImageEntity(userId, "mock-image-name", "image/jpeg");
//        Optional<ImageEntity> mockImageEntityOptional = Optional.of(mockImageEntity);
//
//        Mockito.when(imageRepository.findById(Mockito.any(UUID.class))).thenReturn(mockImageEntityOptional);
//
//        Mockito.doNothing().when(minioClient.removeObject(Mockito.any(RemoveObjectArgs.class)));
//
//
    }


}
