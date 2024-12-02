package com.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.model.AlertRequest; // Import AlertRequest
import com.weather.model.WeatherData;
import com.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final RestTemplate restTemplate;
    private final WeatherRepository weatherRepository;
    private final AlertService alertService;

    private static final Logger logger = Logger.getLogger(WeatherService.class.getName());

    @Autowired
    public WeatherService(RestTemplate restTemplate, WeatherRepository weatherRepository, AlertService alertService) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
        this.alertService = alertService;
    }

    public WeatherData fetchWeatherData(String city) throws JsonMappingException, JsonProcessingException {
        String url = BASE_URL + "?q=" + city + "&appid=" + apiKey;
        logger.info("Fetching weather data for city: " + city);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                // Populate WeatherData object
                WeatherData weatherData = new WeatherData();
                weatherData.setCity(city);
                weatherData.setTemp(root.get("main").get("temp").asDouble() - 273.15);
                weatherData.setMaxTemp(root.get("main").get("temp_max").asDouble() - 273.15);
                weatherData.setMinTemp(root.get("main").get("temp_min").asDouble() - 273.15);
                weatherData.setFeelsLike(root.get("main").get("feels_like").asDouble() - 273.15);
                weatherData.setMain(root.get("weather").get(0).get("main").asText());
                weatherData.setDate(LocalDateTime.now());

                // Calculate and set average temperature
                weatherData.setAvgTemp((weatherData.getMaxTemp() + weatherData.getMinTemp()) / 2.0);

                // Save weather data to MongoDB
                weatherRepository.save(weatherData);
                logger.info("Weather data saved for city: " + city);

                // Create an AlertRequest object for alert checks
                AlertRequest alertRequest = new AlertRequest();
                alertRequest.setThreshold(0); // Set the appropriate threshold
                alertRequest.setEmail("default@example.com"); // Set a default email or modify as needed
                alertRequest.setCity(city);

                // Check for any alerts
                alertService.checkForAlerts(weatherData, alertRequest);

                return weatherData;
            } else {
                logger.warning("Failed to fetch weather data for city: " + city + ". Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.severe("Error fetching weather data for city: " + city + " - " + e.getMessage());
        }

        return null;
    }

    public WeatherData getLatestWeatherData(String city) {
        List<WeatherData> weatherDataList = weatherRepository.findByCity(city);
        return weatherDataList.isEmpty() ? null : weatherDataList.get(0);
    }

    public List<WeatherData> getWeatherDataByCity(String city) {
        logger.info("Retrieving weather data for city: " + city);
        return weatherRepository.findByCity(city);
    }

    public List<WeatherData> getAllWeatherData() {
        logger.info("Retrieving all weather data");
        return weatherRepository.findAll();
    }
}
