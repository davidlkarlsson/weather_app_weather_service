package com.david.weather_app_weather_service.exception;

public class InvalidCredException extends RuntimeException {

    public InvalidCredException(String message) {
        super(message);
    }
}
