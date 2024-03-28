package com.backend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

	private final String CLOUD_NAME = "dj6tsctnn";
	private final String API_KEY = "959871865335863";
	private final String API_SECRET = "VtKWslY2eQlpY_50J5IPKuHOv94";

	@Bean
	public Cloudinary cloudinary() {
		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", CLOUD_NAME);
		config.put("api_key", API_KEY);
		config.put("api_secret", API_SECRET);
		Cloudinary cloudinary = new Cloudinary(config);
		return cloudinary;
	}

}
