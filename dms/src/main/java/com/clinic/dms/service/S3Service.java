package com.clinic.dms.service;

import com.clinic.dms.dto.DocumentMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client client;
    private final S3Presigner presigner;

    public S3Service(S3Client s3Client, S3Presigner presigner){
        this.client = s3Client;
        this.presigner = presigner;
    }

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(String patientId, MultipartFile file) throws IOException{
        String key = "patients/" + patientId + "/" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        return key;
    }

    public List<DocumentMetadata> listFiles(String patientId){
        String prefix = "patients/" + patientId + "/";

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .prefix(prefix)
                .bucket(bucketName)
                .build();

        return client.listObjectsV2(request)
                .contents()
                .stream()
                .map((o) -> new DocumentMetadata(
                        o.key(),
                        o.key().replace(prefix,""),
                        o.size(),
                        o.lastModified().toString()) )
                .collect(Collectors.toList());
    }

    public void deleteFile(String key){
        client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
    }

    public String generatePresignedURL(String key){
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(r -> r.bucket(bucketName).key(key))
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }
}
