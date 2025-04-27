package com.boreebeko.farm_monitoring_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Agrotanym Farm Monitoring Service API",
				version = "1.0",
				description = "API for managing and monitoring user farm fields"
		),
		servers = {
				@Server(url = "http://localhost:0000", description = "Local Server")
		}
)
@SpringBootApplication
public class FarmMonitoringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmMonitoringServiceApplication.class, args);
	}

}
