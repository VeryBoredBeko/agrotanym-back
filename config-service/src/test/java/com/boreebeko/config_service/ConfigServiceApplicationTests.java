package com.boreebeko.config_service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Base64;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ConfigServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void unauthorizedRemoteConfigFetchForImageService() throws Exception {
		String applicationName = "image-service";
		String label = "default";
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/" + applicationName + "/" + label)
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	void authorizedRemoteConfigFetchForImageService() throws Exception {
		String applicationName = "image-service";
		String label = "main";
		String credentials = "client:12345";
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/" + applicationName + "/" + label)
								.header("Authorization", "Basic " + encodedCredentials)
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void validRemoteConfigFetchResultForImageService() throws Exception {

		String applicationName = "image-service";
		String label = "main";
		String credentials = "client:12345";
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/" + applicationName + "/" + label)
								.header("Authorization", "Basic " + encodedCredentials)
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("propertySources"));
	}
}
