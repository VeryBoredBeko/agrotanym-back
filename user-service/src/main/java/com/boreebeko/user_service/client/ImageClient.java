package com.boreebeko.user_service.client;

import com.boreebeko.user_service.web.dto.ImageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "image-service")
public interface ImageClient {

    @GetMapping("/images/{userId}")
    List<ImageDTO> getImagesByUserId(@PathVariable("userId") String userId);
}
