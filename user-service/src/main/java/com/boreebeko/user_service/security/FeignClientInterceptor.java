package com.boreebeko.user_service.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null ) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
        }
    }
}
