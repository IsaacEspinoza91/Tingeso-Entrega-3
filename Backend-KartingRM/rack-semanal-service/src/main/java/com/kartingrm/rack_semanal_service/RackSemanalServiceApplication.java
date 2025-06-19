package com.kartingrm.rack_semanal_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RackSemanalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RackSemanalServiceApplication.class, args);
	}

}
