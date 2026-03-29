package com.clinic.audit_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEvent {
    private String eventType;
    private String patientId;
    private String fileName;
    private String fileKey;
    private long fileSize;
    private String timestamp;
}
