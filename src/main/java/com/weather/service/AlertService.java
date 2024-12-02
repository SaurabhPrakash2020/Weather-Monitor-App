package com.weather.service;

import com.weather.model.AlertRequest;
import com.weather.model.WeatherData;
import com.weather.model.AlertHistory;
import com.weather.repository.AlertHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    public void checkForAlerts(WeatherData weatherData, AlertRequest alertRequest) {
        // Check for null values
        if (weatherData == null || alertRequest == null) {
            logger.error("WeatherData or AlertRequest is null; cannot check for alerts.");
            return;
        }

        double currentTemperature = weatherData.getTemp();
        double threshold = alertRequest.getThreshold();
        String email = alertRequest.getEmail();

        // Log the received parameters
        logger.debug("Current Temperature: {}, Threshold: {}, Email: {}", currentTemperature, threshold, email);

        // Ensure email and threshold are set properly
        if (email == null || email.isEmpty()) {
            logger.warn("No email provided in AlertRequest; skipping email alert.");
            return;
        }

        if (Double.isNaN(threshold) || threshold <= 0) { // Check for valid positive thresholds
            logger.warn("Threshold is not set in AlertRequest or invalid; please provide a valid threshold.");
            return;
        }

        // Validate temperature
        if (currentTemperature < -50 || currentTemperature > 50) {
            logger.warn("Unreasonable temperature value for {}: {}°C", weatherData.getCity(), currentTemperature);
            return;
        }

        logger.debug("Checking alert for {}: Current Temp = {}°C, Threshold = {}°C", weatherData.getCity(), currentTemperature, threshold);

        // Check if current temperature exceeds the threshold
        if (currentTemperature >= threshold) {
            logger.info("Alert triggered for {} at temperature: {}", weatherData.getCity(), currentTemperature);
            emailService.sendAlertEmail(weatherData, email);

            // Save alert history
            AlertHistory alertHistory = new AlertHistory();
            alertHistory.setCity(weatherData.getCity());
            alertHistory.setCurrentTemp(currentTemperature);
            alertHistory.setCondition(weatherData.getCondition());
            alertHistoryRepository.save(alertHistory);

            logger.info("Alert history saved for city: {}, Temp: {}", weatherData.getCity(), currentTemperature);
        } else {
            logger.info("No alert needed for {}: current temperature {}°C is below the threshold of {}°C",
                        weatherData.getCity(), currentTemperature, threshold);
        }
    }
}
