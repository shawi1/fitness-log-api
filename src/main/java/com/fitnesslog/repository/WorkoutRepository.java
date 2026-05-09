package com.fitnesslog.repository;

import com.fitnesslog.model.WorkoutSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkoutRepository extends MongoRepository<WorkoutSession, String> {
    List<WorkoutSession> findByUserId(String userId);
}
