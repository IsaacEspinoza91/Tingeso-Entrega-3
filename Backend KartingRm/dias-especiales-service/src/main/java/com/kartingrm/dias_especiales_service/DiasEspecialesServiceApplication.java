package com.kartingrm.dias_especiales_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DiasEspecialesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiasEspecialesServiceApplication.class, args);
	}

}
