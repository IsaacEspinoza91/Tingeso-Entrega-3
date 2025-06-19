package com.kartingrm.cliente_desc_frecu_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ClienteDescFrecuServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteDescFrecuServiceApplication.class, args);
	}

}
