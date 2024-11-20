package com.gosca.face.service.rekognition.facecollection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

import javax.annotation.PostConstruct;

@SpringBootTest
class CollectionFaceServiceTest {

    @Autowired
    CollectionFaceService collectionFaceService;
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

    @Test
    void test(){
        String sourceImage = "C:\\Users\\gram\\Desktop\\face\\src\\main\\resources\\김승수.jpg";

        collectionFaceService.addToCollection(rekognitionClient,"GOSCA_TEST",sourceImage);
    }

    @Test
    void listFacesCollection(){
        collectionFaceService.listFacesCollection(rekognitionClient,"GOSCA_TEST10");
    }

}