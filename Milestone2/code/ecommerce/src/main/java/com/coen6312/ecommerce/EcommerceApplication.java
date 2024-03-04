package com.coen6312.ecommerce;

import com.coen6312.ecommerce.service.EcommerceSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}

@Component
class EcommerceCommandLineRunner implements CommandLineRunner {

	@Autowired
	private EcommerceSystemService systemService;

	@Override
	public void run(String... args) throws Exception {
		// Add more console interactions as needed
	}
}