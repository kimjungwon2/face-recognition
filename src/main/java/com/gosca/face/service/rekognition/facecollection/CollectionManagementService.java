package com.gosca.face.service.rekognition.facecollection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;

@Slf4j
@Service
public class CollectionManagementService {


    public void createMyCollection(RekognitionClient rekognitionClient, String collectionId) {
        try {
            CreateCollectionRequest collectionRequest = CreateCollectionRequest.builder()
                    .collectionId(collectionId)
                    .build();

            CreateCollectionResponse collectionResponse = rekognitionClient.createCollection(collectionRequest);
            log.info("CollectionArn: {}", collectionResponse.collectionArn());
            log.info("Status code: {}", collectionResponse.statusCode().toString());

        } catch(RekognitionException e) {
            log.info(e.getMessage());
            System.exit(1);
        }
    }

    public void deleteMyCollection(RekognitionClient rekognitionClient, String collectionId) {
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

    public void describeCollection(RekognitionClient rekognitionClient, String collectionId) {
        try {
            DescribeCollectionRequest describeCollectionRequest = DescribeCollectionRequest.builder()
                    .collectionId(collectionId)
                    .build();

            DescribeCollectionResponse describeCollectionResponse = rekognitionClient.describeCollection(describeCollectionRequest);
            log.info("Collection Arn : {}", describeCollectionResponse.collectionARN());
            log.info("Created : {}", describeCollectionResponse.creationTimestamp().toString());
            log.info("FaceCount : {}", describeCollectionResponse.faceCount());
            log.info("FaceModelVersion : {}", describeCollectionResponse.faceModelVersion());

        } catch(RekognitionException e) {
            log.info(e.getMessage());
            System.exit(1);
        }
    }

    public void retrieveCollections(RekognitionClient rekognitionClient) {
        try {
            ListCollectionsRequest listCollectionsRequest = ListCollectionsRequest.builder()
                    .maxResults(10)
                    .build();

            ListCollectionsResponse response = rekognitionClient.listCollections(listCollectionsRequest);
            List<String> collectionIds = response.collectionIds();
            for (String resultId : collectionIds) {
                log.info(resultId);
            }

        } catch (RekognitionException e) {
            log.info(e.getMessage());
            System.exit(1);
        }
    }

}
