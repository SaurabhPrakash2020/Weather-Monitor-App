package com.weather.repository;

import com.weather.model.AlertHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlertHistoryRepository extends MongoRepository<AlertHistory, String> {
    // MongoDB repository for saving AlertHistory
}
