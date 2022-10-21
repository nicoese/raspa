package com.nicoese.scraping_hardware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.registry.Registry;

@SpringBootApplication
public class ScrapingHardwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrapingHardwareApplication.class, args);
	}

	public String hola(){
		return "Hola Mundo";
	}
}
