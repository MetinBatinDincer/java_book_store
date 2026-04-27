package com.bookstore.bookservice.service;

import com.bookstore.bookservice.model.ActivityLog;
import com.bookstore.bookservice.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public void log(String action, String entity, Long entityId, String detail) {
        activityLogRepository.save(new ActivityLog(action, entity, entityId, detail));
    }

    public List<ActivityLog> getAll() {
        return activityLogRepository.findAll();
    }

    public List<ActivityLog> getByEntity(String entity) {
        return activityLogRepository.findByEntity(entity);
    }

    public List<ActivityLog> getByEntityId(Long entityId) {
        return activityLogRepository.findByEntityId(entityId);
    }
}
