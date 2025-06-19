package com.kartingrm.reservas_comprobantes_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ReservasComprobantesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservasComprobantesServiceApplication.class, args);
	}

}
