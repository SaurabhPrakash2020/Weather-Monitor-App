package com.weather.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "alert_history") // MongoDB collection
public class AlertHistory {

    @Id
    private String id;
    private String city;
    private double currentTemp;
    private String condition;
    private LocalDateTime alertTime;

    // Constructors
    public AlertHistory() {
    }

    public AlertHistory(String city, double currentTemp, String condition, LocalDateTime alertTime) {
        this.city = city;
        this.currentTemp = currentTemp;
        this.condition = condition;
        this.alertTime = alertTime;
    }

    // Getters and Setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public LocalDateTime getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(LocalDateTime alertTime) {
        this.alertTime = alertTime;
    }
}
