package com.david.weather_app_weather_service.dto;

import java.util.UUID;

public record ResponseWeatherDTO(

        String time,
        double temperatureMin,
        double temperatureMax,
        String weatherStatus,
        double precipitationSum, // Total nederbörd i mm
        UUID userId,
        String city
) {}
