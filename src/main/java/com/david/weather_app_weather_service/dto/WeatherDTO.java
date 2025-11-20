package com.david.weather_app_weather_service.dto;

public record WeatherDTO(

        String time,
        double temperatureMin,
        double temperatureMax,
        String weatherStatus,
        double precipitationSum // Total nederbörd i mm
) {}
