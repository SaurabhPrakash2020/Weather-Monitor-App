package com.weather.service;

import com.weather.model.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender; // Spring's mail sender

    /**
     * Sends an alert email to the user when the temperature threshold is breached.
     *
     * @param weatherData the weather data for the alert
     * @param userEmail   the email address to send the alert to
     */
    public void sendAlertEmail(WeatherData weatherData, String userEmail) {
        // Validate input parameters
        if (weatherData == null) {
            logger.error("Weather data is null; cannot send email.");
            return;
        }

        if (userEmail == null || userEmail.isEmpty()) {
            logger.warn("User email is null or empty; skipping email send.");
            return;
        }

        // Create a new email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail); // Set recipient email
        message.setSubject("Weather Alert for " + weatherData.getCity()); // Set email subject

        // Set email body with full weather details
        String emailBody = "ALERT! Current conditions in " + weatherData.getCity() +
                "\nTemperature: " + weatherData.getTemp() + "°C" +
                "\nMax Temp: " + weatherData.getMaxTemp() + "°C" +
                "\nMin Temp: " + weatherData.getMinTemp() + "°C" +
                "\nFeels Like: " + weatherData.getFeelsLike() + "°C" +
                "\nCondition: " + weatherData.getCondition();

        message.setText(emailBody); // Set email body

        try {
            // Send the email
            mailSender.send(message);
            logger.info("Alert email sent to {} for {}: {}°C, Condition: {}", userEmail, weatherData.getCity(), weatherData.getTemp(), weatherData.getCondition());
        } catch (Exception e) {
            // Log error if sending email fails
            logger.error("Failed to send alert email to {}: {}", userEmail, e.getMessage());
        }
    }
}
