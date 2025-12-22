package com.example.Dasboard.Entity;

import com.example.Dasboard.Enums.LEVEL;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Logs_Table", indexes = {
        @Index(name = "idx_service", columnList = "serviceName"),
        @Index(name = "idx_level", columnList = "logLevel")
})
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String serviceName;
    private String message;

    @Enumerated(EnumType.STRING)
    private LEVEL logLevel;

    private LocalDateTime receivedAt;
}
