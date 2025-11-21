package com.david.weather_app_weather_service.exception;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String city) {
        super("No city was found with name: " + city);
    }
}