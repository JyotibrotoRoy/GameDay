package com.example.Dasboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DasboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DasboardApplication.class, args);
	}

}
