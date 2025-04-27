package com.boreebeko.image_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
		info = @Info(
				title = "Agrotanym Image-Service API",
				version = "2.0",
				description = "API for processing user upload images: saving, fetching, calling Image-Classifier service"
		),
		servers = {
				@Server(url = "http://localhost:0000", description = "Local Server")
		}
)
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ImageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageServiceApplication.class, args);
	}

}
