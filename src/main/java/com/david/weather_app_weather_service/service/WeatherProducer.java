package com.david.weather_app_weather_service.service;

import com.david.weather_app_weather_service.config.RabbitConfig;
import com.david.weather_app_weather_service.dto.ResponseWeatherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class WeatherProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public WeatherProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendResponse(ResponseWeatherDTO dto) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.RESPONSE_ROUTING_KEY,
                dto
        );
        log.info("Sent weather response: " + dto);
    }
}

