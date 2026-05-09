package com.fitnesslog.controller;

import com.fitnesslog.dto.WorkoutRequest;
import com.fitnesslog.model.WorkoutSession;
import com.fitnesslog.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    private String getUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<WorkoutSession> create(@Valid @RequestBody WorkoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutService.create(getUserId(), request));
    }

    @GetMapping
    public ResponseEntity<List<WorkoutSession>> getAll() {
        return ResponseEntity.ok(workoutService.getAll(getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSession> getById(@PathVariable String id) {
        return ResponseEntity.ok(workoutService.getById(id, getUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutSession> update(@PathVariable String id,
                                                  @Valid @RequestBody WorkoutRequest request) {
        return ResponseEntity.ok(workoutService.update(id, getUserId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        workoutService.delete(id, getUserId());
        return ResponseEntity.noContent().build();
    }
}
