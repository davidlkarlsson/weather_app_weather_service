package com.david.weather_app_weather_service.controller;

import com.david.weather_app_weather_service.dto.RequestWeatherDTO;
import com.david.weather_app_weather_service.dto.ResponseWeatherDTO;
import com.david.weather_app_weather_service.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/dailyresult")
    public ResponseEntity<?> getWeather(@RequestParam String city) {
        try {
            RequestWeatherDTO request = new RequestWeatherDTO(city);
            ResponseWeatherDTO dto = weatherService.getWeatherForCity(request);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
}

}
