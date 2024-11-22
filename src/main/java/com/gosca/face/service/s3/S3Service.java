package com.gosca.face.service.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Service
public class S3Service {

    private S3Client s3Client;

    @Value("${aws.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.credentials.secretKey}")
    private String secretKey;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @PostConstruct
    public void init() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }


    public String uploadFileToS3(MultipartFile file, String storeType, Long storeId) {
        String path = generateS3Path(file.getOriginalFilename(), storeType, storeId);

        try {
            byte[] bytes = file.getBytes();
            uploadFile(bytes, bucketName, path);
            return "File uploaded successfully: " + path;
        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }

    private void uploadFile(byte[] bytes, String bucketName, String keyName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
    }

    private String generateS3Path(String fileName, String storeType, Long storeId){
        StringBuilder path = new StringBuilder();

        path.append(storeType);
        path.append("/");
        path.append(storeId);
        path.append("/");
        path.append(fileName);

        log.info("S3 PATH:{}", path.toString());

        return path.toString();
    }
}
