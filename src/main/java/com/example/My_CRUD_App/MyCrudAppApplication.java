package com.example.My_CRUD_App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MyCrudAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyCrudAppApplication.class, args);
		System.out.println("---------------Custom Logs---------------");
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
