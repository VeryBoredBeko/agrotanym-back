package com.boreebeko.forum_service_v2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Agrotanym Forum-Service API",
				version = "2.0",
				description = "API for storing and processing user questions or answers"
		),
		servers = {
				@Server(url = "http://localhost:0000", description = "Local Server")
		}
)
@SpringBootApplication
public class ForumServiceV2Application {

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceV2Application.class, args);
	}

}
