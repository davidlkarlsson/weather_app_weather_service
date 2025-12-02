package com.david.weather_app_weather_service.service;

import com.david.weather_app_weather_service.config.RabbitConfig;
import com.david.weather_app_weather_service.dto.RequestWeatherDTO;
import com.david.weather_app_weather_service.dto.ResponseWeatherDTO;
import com.david.weather_app_weather_service.exception.CityNotFoundException;
import com.david.weather_app_weather_service.exception.InvalidCredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WeatherRequestListener {

    private final WeatherService weatherService;
    private final WeatherProducer weatherProducer;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public WeatherRequestListener(WeatherService weatherService, WeatherProducer weatherProducer) {
        this.weatherService = weatherService;
        this.weatherProducer = weatherProducer;
    }

    @RabbitListener(queues = RabbitConfig.REQUEST_QUEUE)
    public void onCityRequest(RequestWeatherDTO request) {
        log.info("Received request for city: " + request.city());

        ResponseWeatherDTO response;

        try {
            // Normal weather request
            response = weatherService.getWeatherForCity(request);
        } catch (CityNotFoundException e) {
            log.warn("City not found: " + request.city());
            // Skickar ett "fel" i weatherStatus
            response = new ResponseWeatherDTO(
                    Instant.now().toString(),
                    0,
                    0,
                    "",
                    0,
                    "ERROR: City not found",
                    ""
            );

        } catch (InvalidCredException e) {
            log.warn("Invalid characters in city: " + request.city() + " -> " + e.getMessage());
            response = new ResponseWeatherDTO(
                    Instant.now().toString(),
                    0, 0,
                    "",
                    0,
                    "ERROR: Invalid city",
                    ""
            );

        } catch (Exception e) {
            log.error("Unexpected error for city: " + request.city(), e);
            response = new ResponseWeatherDTO(
                    Instant.now().toString(),
                    0,
                    0,
                    "",
                    0,
                    "ERROR: Unexpected error",
                    ""
            );
        }

        // Skicka alltid ResponseWeatherDTO via producenten
        weatherProducer.sendResponse(response);
    }
}
