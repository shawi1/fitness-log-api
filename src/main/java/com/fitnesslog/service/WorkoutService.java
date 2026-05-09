package com.fitnesslog.service;

import com.fitnesslog.dto.WorkoutRequest;
import com.fitnesslog.exception.ForbiddenException;
import com.fitnesslog.exception.ResourceNotFoundException;
import com.fitnesslog.model.WorkoutSession;
import com.fitnesslog.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public WorkoutSession create(String userId, WorkoutRequest request) {
        WorkoutSession workout = new WorkoutSession();
        workout.setUserId(userId);
        workout.setExerciseName(request.getExerciseName());
        workout.setDuration(request.getDuration());
        workout.setSets(request.getSets());
        workout.setReps(request.getReps());
        workout.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        return workoutRepository.save(workout);
    }

    public List<WorkoutSession> getAll(String userId) {
        return workoutRepository.findByUserId(userId);
    }

    public WorkoutSession getById(String id, String userId) {
        WorkoutSession workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout with id " + id + " not found"));

        if (!workout.getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have access to this workout");
        }

        return workout;
    }

    public WorkoutSession update(String id, String userId, WorkoutRequest request) {
        WorkoutSession workout = getById(id, userId);
        workout.setExerciseName(request.getExerciseName());
        workout.setDuration(request.getDuration());
        workout.setSets(request.getSets());
        workout.setReps(request.getReps());
        if (request.getDate() != null) {
            workout.setDate(request.getDate());
        }
        return workoutRepository.save(workout);
    }

    public void delete(String id, String userId) {
        WorkoutSession workout = getById(id, userId);
        workoutRepository.delete(workout);
    }
}
