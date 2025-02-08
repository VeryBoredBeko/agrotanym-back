package com.boreebeko.user_service.controller;

import com.boreebeko.user_service.client.ImageClient;
import com.boreebeko.user_service.web.dto.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private final ImageClient imageClient;

    public UserController(ImageClient imageClient) {
        this.imageClient = imageClient;
    }

    @GetMapping(value = "/user")
    public ResponseEntity<String> getUser(@AuthenticationPrincipal Jwt jwt) {

        String name = jwt.getClaimAsString("name");
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String userInfo = username + "\n" + name + "\n" + email;

        return new ResponseEntity<>(userInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/history")
    public ResponseEntity<List<ImageDTO>> getHistory(@AuthenticationPrincipal Jwt jwt) {

        List<ImageDTO> imageDTOList = imageClient.getImagesByUserId(jwt.getClaimAsString("sub"));
        return new ResponseEntity<>(imageDTOList, HttpStatus.OK);
    }
}
