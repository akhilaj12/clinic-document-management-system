package com.clinic.dms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEvent {
    private String eventType;
    private String patientId;
    private String fileName;
    private String fileKey;
    private long fileSizeBytes;
    private String timestamp;

    public static DocumentEvent upload(String patientId, String fileName,
                                       String fileKey, long size) {
        return new DocumentEvent("UPLOAD", patientId, fileName, fileKey,
                size, java.time.Instant.now().toString());
    }

    public static DocumentEvent delete(String patientId,
                                       String fileName, String fileKey) {
        return new DocumentEvent("DELETE", patientId, fileName, fileKey,
                0, java.time.Instant.now().toString());
    }

    public static DocumentEvent view(String patientId, String fileKey) {
        return new DocumentEvent("VIEW", patientId, fileKey, fileKey,
                0, java.time.Instant.now().toString());
    }
}