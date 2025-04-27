package com.boreebeko.image_service.client;

import com.boreebeko.image_service.web.dto.ImageClassificationResponse;
import com.boreebeko.image_service.web.dto.ImageURLRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "image-classifier-service", url = "http://127.0.0.1:8000")
public interface ImageProcessingClient {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    String getRequest();

    @RequestMapping(method = RequestMethod.POST, value = "/process-image")
    ImageClassificationResponse classifyImage(@RequestBody ImageURLRequest imageURLRequest);
}
