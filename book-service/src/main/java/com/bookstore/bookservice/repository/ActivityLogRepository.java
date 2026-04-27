package com.bookstore.bookservice.repository;

import com.bookstore.bookservice.model.ActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends MongoRepository<ActivityLog, String> {
    List<ActivityLog> findByEntity(String entity);
    List<ActivityLog> findByEntityId(Long entityId);
}
