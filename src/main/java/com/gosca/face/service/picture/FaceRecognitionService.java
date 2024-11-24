package com.gosca.face.service.picture;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.gosca.face.controller.dto.FaceRecognitionRequestDto;
import com.gosca.face.service.dynamo.StoreFaceCollectionService;
import com.gosca.face.service.name.NameGeneratorService;
import com.gosca.face.service.rekognition.facecollection.UserSearchByFaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.PostConstruct;
import java.util.Locale;

@Slf4j
@Service
public class FaceRecognitionService {
    private final UserSearchByFaceService userSearchByFaceService;
    private final NameGeneratorService nameGeneratorService;
    private final StoreFaceCollectionService storeFaceCollectionService;

    private AmazonRekognition rekognitionClient;

    @Value("${aws.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.credentials.secretKey}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    public FaceRecognitionService(UserSearchByFaceService userSearchByFaceService, NameGeneratorService nameGeneratorService, StoreFaceCollectionService storeFaceCollectionService) {
        this.userSearchByFaceService = userSearchByFaceService;
        this.nameGeneratorService = nameGeneratorService;
        this.storeFaceCollectionService = storeFaceCollectionService;
    }

    @PostConstruct
    public void init() {
        BasicAWSCredentials awsCreds2 = new BasicAWSCredentials(accessKey, secretKey);

        this.rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds2))
                .build();
    }

    public String userSearchByFace(MultipartFile image, FaceRecognitionRequestDto request){
        Long storeId = request.getStoreId();
        String storeType = convertToUpperCase(request.getStoreType());


        String userId = userSearchByFaceService.searchUser(rekognitionClient, image, collectionId);

        log.info("userId:{}", userId);

        return userId;
    }

    private String convertToUpperCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.toUpperCase(Locale.KOREAN);
    }
}
