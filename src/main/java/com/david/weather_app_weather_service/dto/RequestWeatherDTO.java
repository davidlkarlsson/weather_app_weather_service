package com.david.weather_app_weather_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RequestWeatherDTO(

        @NotBlank(message = "City cannot be blank")
        @Pattern(
                regexp = "^[a-zA-ZåäöÅÄÖ\\- ]+$",
                message = "City name contains invalid characters"
        )
        String city
) {}
