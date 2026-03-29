package com.clinic.notification_service.service;

import com.clinic.notification_service.dto.DocumentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class SNSService {
    private final SnsClient snsClient;

    @Value("${aws.sns.topic-arn}")
    private String topicArn;

    public void sendUploadNotification(DocumentEvent event){
        String message = String.format(
                "New document uploaded!\n\nPatient ID: %s\nFile: %s\nSize: %s KB\nTime: %s",
                event.getPatientId(),
                event.getFileName(),
                event.getFileSize() / 1024,
                event.getTimeStamp()
        );

        snsClient.publish(PublishRequest.builder().
                topicArn(topicArn)
                        .subject("New Document uploaded - " + event.getEventType())
                        .message(message)
                .build());

        log.info("Email notification sent for: {}", event.getFileName());
    }

}