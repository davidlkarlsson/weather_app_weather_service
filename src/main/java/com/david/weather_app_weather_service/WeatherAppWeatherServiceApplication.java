package com.david.weather_app_weather_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class WeatherAppWeatherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherAppWeatherServiceApplication.class, args);
	}

}
