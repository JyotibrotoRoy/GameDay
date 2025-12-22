package com.example.Dasboard.DTO;

import com.example.Dasboard.Enums.LEVEL;

public class LogDTOs {
    public record IngestRequest(
            String serviceName,
            String message,
            LEVEL logLevel
    ){}
}
