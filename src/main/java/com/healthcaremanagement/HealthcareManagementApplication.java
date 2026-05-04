package com.healthcaremanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HealthcareManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcareManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner test() {
		System.out.println("Welcome to this healthcare server");
		return args -> System.out.println("APP IS RUNNING!");
	}

}
