package com.clinic.notification_service.kafka.consumer;

import com.clinic.notification_service.dto.DocumentEvent;
import com.clinic.notification_service.service.SNSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationConsumer {

    private final SNSService snsService;

    // Simulates sending email/SMS notifications
    @KafkaListener(
            topics = {"${kafka.topic.document-events}"},
            groupId = "notification-group"
    )
    public void consume(DocumentEvent event){
        if(event.getEventType().equals("UPLOAD")){
            snsService.sendUploadNotification(event);
        }

        // In production: trigger SNS, SES email, or SMS via Twilio
    }
}
