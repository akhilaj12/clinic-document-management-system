package com.clinic.audit_service.service;

import com.clinic.audit_service.dto.DocumentEvent;
import com.clinic.audit_service.entity.AuditLogEntity;
import com.clinic.audit_service.entity.DocumentMetadataEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamoDbService {

    private final DynamoDbEnhancedClient enhancedClient;

    public void saveAuditLog(DocumentEvent event) {
        DynamoDbTable<AuditLogEntity> table = enhancedClient
                .table("document-audit-log", TableSchema.fromBean(AuditLogEntity.class));

        table.putItem(new AuditLogEntity(
                event.getPatientId(),
                event.getTimestamp(),
                event.getEventType(),
                event.getFileName(),
                event.getFileKey(),
                event.getFileSize()
        ));
        log.info("Audit log saved | {} | {}", event.getEventType(), event.getPatientId());
    }

    public void saveMetadata(DocumentEvent event) {
        DynamoDbTable<DocumentMetadataEntity> table = enhancedClient
                .table("document-metadata", TableSchema.fromBean(DocumentMetadataEntity.class));

        table.putItem(new DocumentMetadataEntity(
                event.getPatientId(),
                event.getFileKey(),
                event.getFileName(),
                event.getFileSize(),
                event.getTimestamp(),
                "ACTIVE"
        ));
        log.info("Metadata saved | {}", event.getFileName());
    }

    public void markAsDeleted(DocumentEvent event) {
        DynamoDbTable<DocumentMetadataEntity> table = enhancedClient
                .table("document-metadata", TableSchema.fromBean(DocumentMetadataEntity.class));

        DocumentMetadataEntity entity = table.getItem(Key.builder()
                .partitionValue(event.getPatientId())
                .sortValue(event.getFileKey())
                .build());

        if (entity != null) {
            entity.setStatus("DELETED");
            table.putItem(entity);
        }
    }
}