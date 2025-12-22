package com.example.Dasboard.Service;

import com.example.Dasboard.DTO.LogDTOs;
import com.example.Dasboard.Entity.LogEntry;
import com.example.Dasboard.Enums.LEVEL;
import com.example.Dasboard.Repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LogService {
    private final LogRepository logRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public LogService(LogRepository logRepository, SimpMessagingTemplate messagingTemplate) {
        this.logRepository = logRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Page<LogEntry> getAllLogEntries(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now().toLocalDate().atStartOfDay();
        return logRepository.findByReceivedAtAfter(now, pageable);
    }

    public List<LogEntry> getLogsByLevel(LEVEL logLevel){
        return logRepository.findByLogLevel(logLevel);
    }

    public List<LogEntry> getLogsByServiceName(String serviceName){
        return logRepository.findByServiceName(serviceName);
    }

    public LogEntry saveLogEntry(LogDTOs.IngestRequest entry){
        try {
            LogEntry newEntry = new LogEntry();
            newEntry.setLogLevel(entry.logLevel());
            newEntry.setServiceName(entry.serviceName());
            newEntry.setMessage(entry.message());
            newEntry.setReceivedAt(LocalDateTime.now());

            //save in db
            LogEntry savedLog = logRepository.save(newEntry);

            //push to /topic/logs
            messagingTemplate.convertAndSend("/topic/logs", savedLog);

            return savedLog;

        }catch (Exception e){
            log.error("error saving log to database {}",e.getMessage());
            throw new RuntimeException("Failed to persist log: "+ e.getMessage());
        }
    }

    @Scheduled(fixedRate = 10000)
    public void pushSystemMetrics(){
        Map<String, Object> metrics = getSystemMetrics();
        messagingTemplate.convertAndSend("/topic/metrics", (Object) metrics);
    }

    public Map<String, Object> getSystemMetrics(){
        Map<String, Object> response = new HashMap<>();

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        long totalLogs = logRepository.countByReceivedAtAfter(startOfDay);
        response.put("Total Logs Today", totalLogs);

        List<LogEntry> errorlogs = logRepository.findByLogLevel(LEVEL.ERROR);
        Map<String, Integer> errorStats = new HashMap<>();
        for(LogEntry log: errorlogs){
            String serviceName = log.getServiceName();
            errorStats.put(serviceName, errorStats.getOrDefault(serviceName, 0) + 1);
        }
        response.put("Error Logs", errorStats);

        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long usedMem = totalMem - freeMem;

        response.put("Used Memory", usedMem);

        return response;
    }


}
