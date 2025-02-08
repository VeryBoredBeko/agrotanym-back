package com.boreebeko.user_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder
				.name("image-upload-topic")
				.partitions(3)
				.replicas(1)
				.build();
	}

	@KafkaListener(id = "user-service", topics = "image-upload-topic")
	public void listen(String in) {
		System.out.println(in);
	}
}
