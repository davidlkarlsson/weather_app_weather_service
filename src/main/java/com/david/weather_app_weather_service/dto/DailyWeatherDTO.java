package com.david.weather_app_weather_service.dto;

import java.util.List;

public record DailyWeatherDTO(
        List<String> time,
        List<Double> temperature_2m_max,
        List<Double> temperature_2m_min,
        List<Integer> weather_code,
        List<Double> precipitation_sum
) {}
