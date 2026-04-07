package com.clinic.dms.controller;

import com.clinic.dms.dto.DocumentEvent;
import com.clinic.dms.dto.DocumentMetadata;
import com.clinic.dms.kafka.producer.DocumentEventProducer;
import com.clinic.dms.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/documents")
public class DocumentController {

    private final S3Service s3Service;

    private final DocumentEventProducer eventProducer;

    public DocumentController(S3Service s3Service, DocumentEventProducer eventProducer) {
        this.s3Service = s3Service;
        this.eventProducer = eventProducer;
    }

    @PostMapping("/upload/{patientId}")
    public ResponseEntity<String> uploadDocument(@PathVariable String patientId,
                                                 @RequestParam("file")MultipartFile file) throws IOException {
        String key = s3Service.uploadFile(patientId, file);

        eventProducer.publish(DocumentEvent.upload(file.getOriginalFilename(), patientId, key, file.getSize()));
        return ResponseEntity.ok("Uploaded successfully!");
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<List<DocumentMetadata>> listFiles(@PathVariable String patientId){
        return ResponseEntity.ok(s3Service.listFiles(patientId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String key){
        s3Service.deleteFile(key);

        String filename = key.split("/")[2];
        String patientID = key.split("/")[1];

        eventProducer.publish(DocumentEvent.delete(filename, patientID, key));
        return ResponseEntity.ok("Deleted successfully!");
    }

    @GetMapping("/view")
    public ResponseEntity<String> viewDocument(@RequestParam String key){
        String patientID = key.split("/")[1];
        String filename = key.split("/")[2];

        eventProducer.publish(DocumentEvent.view(patientID, key));
        return ResponseEntity.ok(s3Service.generatePresignedURL(key));
    }
}
