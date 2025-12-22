package com.example.Dasboard.Controller;

import com.example.Dasboard.DTO.LogDTOs;
import com.example.Dasboard.Entity.LogEntry;
import com.example.Dasboard.Enums.LEVEL;
import com.example.Dasboard.Service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/ingest")
    public LogEntry ingestLog(@RequestBody LogDTOs.IngestRequest request){
        return logService.saveLogEntry(request);
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMetrics(){
        return logService.getSystemMetrics();
    }

    @GetMapping("/level")
    public List<LogEntry> getErrorLog(@RequestParam LEVEL logLevel){
        return logService.getLogsByLevel(logLevel);
    }

    @GetMapping("service")
    public List<LogEntry> getServiceLog(@RequestParam String serviceName){
        return logService.getLogsByServiceName(serviceName);
    }

    @GetMapping("/allEntries")
    public List<LogEntry> getAllLogEntries(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<LogEntry> result = logService.getAllLogEntries(pageable);
        return result.getContent();
    }
}
