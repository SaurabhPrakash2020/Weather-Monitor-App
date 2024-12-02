package com.weather.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertConfig {
    private double temperatureThreshold = 35.0;
    private int consecutiveUpdates = 2;
    private String specificWeatherCondition = "Thunderstorm";

    public double getTemperatureThreshold() {
        return temperatureThreshold;
    }

    public void setTemperatureThreshold(double temperatureThreshold) {
        this.temperatureThreshold = temperatureThreshold;
    }

    public int getConsecutiveUpdates() {
        return consecutiveUpdates;
    }

    public void setConsecutiveUpdates(int consecutiveUpdates) {
        this.consecutiveUpdates = consecutiveUpdates;
    }

    public String getSpecificWeatherCondition() {
        return specificWeatherCondition;
    }

    public void setSpecificWeatherCondition(String specificWeatherCondition) {
        this.specificWeatherCondition = specificWeatherCondition;
    }
}
