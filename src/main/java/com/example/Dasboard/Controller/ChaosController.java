package com.example.Dasboard.Controller;

import com.example.Dasboard.DTO.LogDTOs;
import com.example.Dasboard.Enums.LEVEL;
import com.example.Dasboard.Repository.LogRepository;
import com.example.Dasboard.Service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/chaos")
@CrossOrigin(origins = "*")
public class ChaosController {
    private final LogService logService;
    ChaosController(LogService logService) {
        this.logService = logService;
    }

    //Success Log
    @PostMapping("/success")
    public ResponseEntity<String> triggerSuccess() {
        LogDTOs.IngestRequest log = new LogDTOs.IngestRequest(
                "Payment Service", "Transaction processed succesfully", LEVEL.INFO
        );
        logService.saveLogEntry(log);
        return ResponseEntity.ok("Success log generated");
    }


    //Database Failure
    @PostMapping("/db-failure")
    public ResponseEntity<String> triggerFailure() {
        LogDTOs.IngestRequest log = new LogDTOs.IngestRequest(
                "Database Shard 01", "Connection failed", LEVEL.ERROR
        );
        logService.saveLogEntry(log);
        return ResponseEntity.ok("Failure log generated");
    }


    //Trigger Latency
    @PostMapping("/latency")
    public ResponseEntity<String> triggerLatency() throws InterruptedException {
        Thread.sleep(2000);
        LogDTOs.IngestRequest log = new LogDTOs.IngestRequest(
            "Auth Service", "Response time > 2000ms",  LEVEL.WARN);
        logService.saveLogEntry(log);
        return ResponseEntity.ok("High latency simulated");
    }

    //JVM memory spike
    @PostMapping("/spike-memory")
    public ResponseEntity<String> triggerSpikeMemory() {
        byte[] waste = new byte[1024 * 1024 * 100];
        return ResponseEntity.ok("Allocated 100MB memory spike");
    }

}
