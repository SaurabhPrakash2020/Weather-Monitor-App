package com.weather.controller;

import com.weather.model.AlertRequest;
import com.weather.model.WeatherData;
import com.weather.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    @Autowired
    private AlertService alertService;

    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody AlertRequest alertRequest) {
        // Log the incoming alert request
        logger.info("Received alert request: {}", alertRequest);
        
        // Validate email format
        if (!isValidEmail(alertRequest.getEmail())) {
            logger.error("Invalid email address: {}", alertRequest.getEmail());
            return ResponseEntity.badRequest().body("Invalid email address");
        }

        // Example weather data, you should replace this with actual data retrieval
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(alertRequest.getCity());
        weatherData.setTemp(weatherData.getTemp()); // Replace with actual temperature
        weatherData.setCondition(weatherData.getCondition()); // Replace with actual condition

        alertService.checkForAlerts(weatherData, alertRequest); // Pass the whole AlertRequest

        return ResponseEntity.ok("Alert created successfully");
    }

    // Simple regex for email validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }
}
