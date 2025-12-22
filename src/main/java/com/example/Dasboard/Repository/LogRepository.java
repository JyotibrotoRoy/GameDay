package com.example.Dasboard.Repository;

import com.example.Dasboard.Entity.LogEntry;
import com.example.Dasboard.Enums.LEVEL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<LogEntry, Long> {
    List<LogEntry> findByLogLevel(LEVEL logLevel);
    List<LogEntry> findByServiceName(String serviceName);
    long countByReceivedAtAfter(LocalDateTime startOfDay);
    Page<LogEntry> findByReceivedAtAfter(LocalDateTime startOfDay, Pageable pageable);
}
