package com.clinic.notification_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEvent {
    private String eventType;

    private String fileName;

    private String patientId;

    private String fileKey;

    @JsonProperty("fileSizeBytes")
    private long fileSize;

    @JsonProperty("timestamp")
    private String timeStamp;
}
