package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoPasetoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoPasetoApplication.class, args);
	}

	@Bean
	public  RestTemplate getreTemplate() {
		return new RestTemplate();
	};
}
