package com.weather.controller;

import com.weather.model.AlertRequest; // Import AlertRequest
import com.weather.model.WeatherData;
import com.weather.service.AlertService;
import com.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class WeatherController {

    private final WeatherService weatherService;
    private final AlertService alertService;

    public WeatherController(WeatherService weatherService, AlertService alertService) {
        this.weatherService = weatherService;
        this.alertService = alertService;
    }

    @GetMapping("/weather")
    public ResponseEntity<List<WeatherData>> getWeatherData(@RequestParam(required = false) Optional<String> city) {
        List<WeatherData> weatherDataList;

        if (city.isPresent() && !city.get().isEmpty()) {
            weatherDataList = weatherService.getWeatherDataByCity(city.get());
            if (weatherDataList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
        } else {
            weatherDataList = weatherService.getAllWeatherData();
        }

        return ResponseEntity.ok(weatherDataList);
    }

    @GetMapping("/alerts")
    public ResponseEntity<String> setAlert(
            @RequestParam String city,
            @RequestParam double threshold,
            @RequestParam String email) {

        // Fetch the latest weather data for the specified city
        WeatherData weatherData = weatherService.getLatestWeatherData(city);

        if (weatherData == null) {
            return ResponseEntity.notFound().build();
        }

        // Create an AlertRequest object for alert checks
        AlertRequest alertRequest = new AlertRequest();
        alertRequest.setThreshold(threshold);
        alertRequest.setEmail(email);
        alertRequest.setCity(city);

        // Check for alerts and trigger if threshold is crossed
        alertService.checkForAlerts(weatherData, alertRequest);

        return ResponseEntity.ok("Alert set for " + city + " at " + threshold + "°C.");
    }
}
