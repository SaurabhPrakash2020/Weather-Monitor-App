package com.weather.schedular;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.weather.service.WeatherService;

import java.util.logging.Logger;

@Component
public class WeatherSchedular {
    
    private static final Logger logger = Logger.getLogger(WeatherSchedular.class.getName());
    private final WeatherService weatherService;
    
    public WeatherSchedular(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Scheduled(fixedRate = 300000)
    public void fetchWeatherForCities() throws JsonMappingException, JsonProcessingException {
        String[] cities = {"Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"};
        for (String city : cities) {
            logger.info("Fetching weather data for city: " + city);
            weatherService.fetchWeatherData(city);
        }
    }
}
