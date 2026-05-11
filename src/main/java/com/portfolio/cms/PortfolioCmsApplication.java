package com.portfolio.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PortfolioCmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioCmsApplication.class, args);
	}

}
