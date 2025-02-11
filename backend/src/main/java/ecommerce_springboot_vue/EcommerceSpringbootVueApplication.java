package ecommerce_springboot_vue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ecommerce_springboot_vue.config.EnvConfig;

@SpringBootApplication
public class EcommerceSpringbootVueApplication {

	public static void main(String[] args) {
		EnvConfig.load(); // Load environment variables
		SpringApplication.run(EcommerceSpringbootVueApplication.class, args);
	}

}
