package com.weather.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.weather.model.WeatherData;

@Repository
public interface WeatherRepository extends MongoRepository<WeatherData, String> {
    List<WeatherData> findByCity(String city); // This should match the method in WeatherService
}
