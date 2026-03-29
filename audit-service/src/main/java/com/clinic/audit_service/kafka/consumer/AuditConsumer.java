package com.clinic.audit_service.kafka.consumer;

import com.clinic.audit_service.dto.DocumentEvent;
import com.clinic.audit_service.service.DynamoDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuditConsumer {

    private final DynamoDbService dynamoDbService;

    // Simulates writing to an audit log / database
    @KafkaListener(
            topics = {"${kafka.topic.document-events}"}
            , groupId = "audit-group"
    )
    public void consume(ConsumerRecord<String, DocumentEvent> record){
        DocumentEvent e = record.value();

        dynamoDbService.saveAuditLog(e);

        switch(e.getEventType()){
            case "DELETE" -> dynamoDbService.markAsDeleted(e);
            case "UPLOAD" -> dynamoDbService.saveMetadata(e);
        }

        log.info("Audit processed | {} | {} | {}",
                e.getEventType(), e.getPatientId(), e.getFileName());
    }
}
