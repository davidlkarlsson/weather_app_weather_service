package com.david.weather_app_weather_service.service;


import com.david.weather_app_weather_service.dto.DailyWeatherDTO;
import com.david.weather_app_weather_service.dto.RequestWeatherDTO;
import com.david.weather_app_weather_service.dto.WeatherApiResponseDTO;
import com.david.weather_app_weather_service.dto.ResponseWeatherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final WebClient webClient;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public WeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseWeatherDTO getWeatherForCity(RequestWeatherDTO request) throws Exception {

        // Hämta geocoding via api för stad (lon + lat)

        String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + request +
                "&count=1&language=en&format=json";

        log.debug(geoUrl);


        Map<String, Object> geoResponse =
                webClient.get()
                .uri(geoUrl)
                .retrieve()
                // Castar om JSON-objektet till en Map
                .bodyToMono(Map.class)
                .block();
        // Använder Object eftersom värdet som kommer in kan vara av blandad typ (int, double, String osv.)
        List<Map<String, Object>> results = (List<Map<String, Object>>) geoResponse.get("results");

        // TODO - make a custom exception and a exceptionhandler
        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException("No city was found with name: " + request.city());
        }



        Map<String, Object> result = results.get(0);
        double lat = (double) result.get("latitude");
        double lon = (double) result.get("longitude");

        log.debug("Latitude: " + lat + ", Longitude: " + lon);


        // Hämta väder via väder-api
        String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                "&longitude=" + lon +
                "&daily=temperature_2m_max,temperature_2m_min,weather_code,precipitation_sum&timezone=Europe/Berlin&forecast_days=1";

        log.debug(weatherUrl);

        WeatherApiResponseDTO weatherResponse = webClient.get()
                .uri(weatherUrl)
                .retrieve()
                .bodyToMono(WeatherApiResponseDTO.class)
                .block();

        DailyWeatherDTO daily = weatherResponse.daily();

        String time = daily.time().get(0);
        double tempMin = daily.temperature_2m_min().get(0);
        double tempMax = daily.temperature_2m_max().get(0);
        double precipitation = daily.precipitation_sum().get(0);
        int weatherCode = daily.weather_code().get(0);

        String status = mapWeatherCodeToStatus(weatherCode);

        return new ResponseWeatherDTO(time, tempMin, tempMax, status, precipitation);

    }


    private String mapWeatherCodeToStatus(int weatherCode) {
        return switch (weatherCode) {
            case 0 -> "Clear sky";
            case 1, 2, 3 -> "Mainly clear, partly cloudy, or overcast";
            case 45, 48 -> "Fog";
            case 51, 53, 55 -> "Drizzle";
            case 56, 57 -> "Freezing Drizzle";
            case 61, 63, 65 -> "Rain";
            case 66, 67 -> "Freezing Rain";
            case 71, 73, 75 -> "Snow fall";
            case 77 -> "Snow grains";
            case 80, 81, 82 -> "Rain showers";
            case 85, 86 -> "Snow showers";
            case 95 -> "Thunderstorm";
            case 96, 99 -> "Thunderstorm with hail";
            default -> "Unknown";
        };
    }
}

/* Open-Meteo Weather Forecast API Documentation for codes

Code	Description
0	Clear sky
1, 2, 3	Mainly clear, partly cloudy, and overcast
45, 48	Fog and depositing rime fog
51, 53, 55	Drizzle: Light, moderate, and dense intensity
56, 57	Freezing Drizzle: Light and dense intensity
61, 63, 65	Rain: Slight, moderate and heavy intensity
66, 67	Freezing Rain: Light and heavy intensity
71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
77	Snow grains
80, 81, 82	Rain showers: Slight, moderate, and violent
85, 86	Snow showers slight and heavy
95 *	Thunderstorm: Slight or moderate
96, 99 *	Thunderstorm with slight and heavy hail
 */
