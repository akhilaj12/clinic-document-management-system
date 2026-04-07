package com.clinic.dms.kafka.producer;

import com.clinic.dms.dto.DocumentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentEventProducer {
    private final KafkaTemplate<String, DocumentEvent> kafkaTemplate;

    @Value("${kafka.topic.document-events}")
    private String topicName;

    public void publish(DocumentEvent documentEvent){
        // patientId is the Kafka message key
        // → all events for same patient go to same partition (ordering guaranteed)
        kafkaTemplate.send(topicName, documentEvent.getPatientId(), documentEvent)
                .whenComplete((result, ex) ->{
                        if(ex != null){
                            log.error("Failed to publish the event! " + ex.getMessage());
                        }
                        else{
                            log.info("Published [{}] event -> partition {} , offset {}",
                                    documentEvent.getEventType()
                            ,result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                        }
                        );
    }
}
