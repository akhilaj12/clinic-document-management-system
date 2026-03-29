package com.clinic.audit_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class AuditLogEntity {
    private String patientId;
    private String timestamp;
    private String eventType;
    private String fileName;
    private String fileKey;
    private Long fileSize;

    @DynamoDbPartitionKey
    public String getPatientId(){
        return patientId;
    }

    @DynamoDbSortKey
    public String getTimestamp(){
        return timestamp;
    }
}
