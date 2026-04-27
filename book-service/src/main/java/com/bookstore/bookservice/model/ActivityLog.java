package com.bookstore.bookservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "activity_logs")
public class ActivityLog {

    @Id
    private String id;

    private String action;
    private String entity;
    private Long entityId;
    private String detail;
    private LocalDateTime timestamp;

    public ActivityLog(String action, String entity, Long entityId, String detail) {
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.detail = detail;
        this.timestamp = LocalDateTime.now();
    }
}
