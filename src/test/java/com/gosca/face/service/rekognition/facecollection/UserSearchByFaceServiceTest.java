package com.gosca.face.service.rekognition.facecollection;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserSearchByFaceServiceTest {

    @Autowired
    private UserSearchByFaceService userSearchByFaceService;
    private AmazonRekognition rekognitionClient;

    @Value("${aws.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.credentials.secretKey}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    @PostConstruct
    public void init() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        this.rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @Test
    void searchUser() {
        String collectionId = "GOSCA_TEST11";
        String sourceImage = "C:\\Users\\gram\\Desktop\\face\\src\\main\\resources\\김정원.jpg";

        userSearchByFaceService.searchUser(rekognitionClient, sourceImage,collectionId);
    }


}