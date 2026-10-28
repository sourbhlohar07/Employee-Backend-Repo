package com.example.Aivra_Backend;


import com.example.Aivra_Backend.config.BCryptPasswordGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AivraBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AivraBackendApplication.class, args);



	}

}
