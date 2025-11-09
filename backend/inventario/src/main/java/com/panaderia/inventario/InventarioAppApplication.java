package com.panaderia.inventario;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.panaderia.inventario.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class InventarioAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioAppApplication.class, args);
	}

}
