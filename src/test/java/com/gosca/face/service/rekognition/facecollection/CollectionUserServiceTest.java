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

@SpringBootTest
class CollectionUserServiceTest {

    @Autowired
    CollectionUserService collectionUserService;

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
    void test(){
        collectionUserService.createUser(rekognitionClient,"GOSCA_TEST10",1L);
    }

    @Test
    void getUserLists(){
        collectionUserService.getUserLists(rekognitionClient,"gosca_1");
    }

    @Test
    void associcateUser(){
        collectionUserService.associateFace(rekognitionClient,"GOSCA_TEST10", "8964a214-0950-4f60-a2cd-c78be4a09509","user1");
    }

}