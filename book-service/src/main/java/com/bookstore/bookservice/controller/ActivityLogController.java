package com.bookstore.bookservice.controller;

import com.bookstore.bookservice.dto.ApiResponse;
import com.bookstore.bookservice.model.ActivityLog;
import com.bookstore.bookservice.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityLog>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(activityLogService.getAll()));
    }

    @GetMapping("/entity/{entity}")
    public ResponseEntity<ApiResponse<List<ActivityLog>>> getByEntity(@PathVariable String entity) {
        return ResponseEntity.ok(ApiResponse.success(activityLogService.getByEntity(entity)));
    }

    @GetMapping("/entity-id/{entityId}")
    public ResponseEntity<ApiResponse<List<ActivityLog>>> getByEntityId(@PathVariable Long entityId) {
        return ResponseEntity.ok(ApiResponse.success(activityLogService.getByEntityId(entityId)));
    }
}
