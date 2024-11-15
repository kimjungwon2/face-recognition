package com.gosca.face.service.rekognition.facecollection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class CollectionDeletionService {
    private RekognitionClient rekognitionClient;

    @Value("${aws.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.credentials.secretKey}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    @PostConstruct
    public void init() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        this.rekognitionClient = RekognitionClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public void deleteMyCollection(String collectionId) {
        try {
            DeleteCollectionRequest deleteCollectionRequest = DeleteCollectionRequest.builder()
                    .collectionId(collectionId)
                    .build();

            DeleteCollectionResponse deleteCollectionResponse = rekognitionClient.deleteCollection(deleteCollectionRequest);
            log.info(collectionId + ": " + deleteCollectionResponse.statusCode().toString());

        } catch(RekognitionException e) {
            log.info(e.getMessage());
            System.exit(1);
        }
    }
}
