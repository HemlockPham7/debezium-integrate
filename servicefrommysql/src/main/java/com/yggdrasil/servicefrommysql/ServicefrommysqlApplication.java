package com.yggdrasil.servicefrommysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicefrommysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicefrommysqlApplication.class, args);
	}

}
