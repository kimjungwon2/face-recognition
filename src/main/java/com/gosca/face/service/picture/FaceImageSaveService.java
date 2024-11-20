package com.gosca.face.service.picture;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.gosca.face.service.rekognition.facecollection.CollectionFaceService;
import com.gosca.face.service.rekognition.facecollection.CollectionManagementService;
import com.gosca.face.service.rekognition.facecollection.CollectionUserService;
import com.gosca.face.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Service
public class FaceImageSaveService {

    private final CollectionManagementService collectionManagementService;
    private final CollectionFaceService collectionFaceService;
    private final CollectionUserService collectionUserService;
    private final S3Service s3Service;


    private RekognitionClient rekognitionClientV2;
    private AmazonRekognition rekognitionClientV1;

    @Value("${aws.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.credentials.secretKey}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    @PostConstruct
    public void init() {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        BasicAWSCredentials awsCreds2 = new BasicAWSCredentials(accessKey, secretKey);

        this.rekognitionClientV2 = RekognitionClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.rekognitionClientV1 = AmazonRekognitionClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds2))
                .build();
    }


    public boolean saveUserFace(String collectionId, MultipartFile file){
        collectionManagementService.createMyCollection(rekognitionClientV2, collectionId);
        String faceId = collectionFaceService.addToCollection(rekognitionClientV2, collectionId, file);

        collectionUserService.createUser(rekognitionClientV1,collectionId,"user1");
        collectionUserService.associateFace(rekognitionClientV1, collectionId, faceId,"1");

        s3Service.uploadFileToS3(file);

        rekognitionClientV2.close();

        return true;
    }

}
