package com.weather.model;

public class AlertRequest {
    private double threshold; // Temperature threshold for alerts
    private String email;     // User's email address
    private String city;      // City for which the alert is set

    // Default constructor
    public AlertRequest() {}

    // Getters and Setters
    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
