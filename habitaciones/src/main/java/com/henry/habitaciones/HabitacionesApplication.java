package com.henry.habitaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.henry.habitaciones", "com.henry.commons"})
@EnableFeignClients
public class HabitacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitacionesApplication.class, args);
	}

}
