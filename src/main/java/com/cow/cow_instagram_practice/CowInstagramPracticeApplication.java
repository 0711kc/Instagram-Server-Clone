package com.cow.cow_instagram_practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CowInstagramPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CowInstagramPracticeApplication.class, args);
	}

}
