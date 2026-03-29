package com.clinic.audit_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class DocumentMetadataEntity {

    private String patientId;
    private String fileKey;
    private String fileName;
    private Long fileSizeBytes;
    private String uploadedAt;
    private String status;

    @DynamoDbPartitionKey
    public String getPatientId() { return patientId; }

    @DynamoDbSortKey
    public String getFileKey() { return fileKey; }
}