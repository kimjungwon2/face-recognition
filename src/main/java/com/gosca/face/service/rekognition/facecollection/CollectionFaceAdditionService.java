package com.gosca.face.service.rekognition.facecollection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class CollectionFaceAdditionService {
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


    public void addToCollection(RekognitionClient rekognitionClient, String collectionId, String sourceImage) {

        try {
            InputStream sourceStream = new FileInputStream(sourceImage);
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
            Image souImage = Image.builder()
                    .bytes(sourceBytes)
                    .build();

            IndexFacesRequest facesRequest = IndexFacesRequest.builder()
                    .collectionId(collectionId)
                    .image(souImage)
                    .maxFaces(1)
                    .qualityFilter(QualityFilter.AUTO)
                    .detectionAttributes(Attribute.DEFAULT)
                    .build();

            IndexFacesResponse facesResponse = rekognitionClient.indexFaces(facesRequest);
            log.info("Results for the image");
            log.info("\n Faces indexed:");
            List<FaceRecord> faceRecords = facesResponse.faceRecords();
            for (FaceRecord faceRecord : faceRecords) {
                log.info("  Face ID: {}", faceRecord.face().faceId());
                log.info("  Location:{}", faceRecord.faceDetail().boundingBox().toString());
            }

            List<UnindexedFace> unindexedFaces = facesResponse.unindexedFaces();
            log.info("Faces not indexed:");
            for (UnindexedFace unindexedFace : unindexedFaces) {
                log.info("  Location:{}",unindexedFace.faceDetail().boundingBox().toString());
                log.info("  Reasons:");
                for (Reason reason : unindexedFace.reasons()) {
                    log.info("Reason:  {}", reason);
                }
            }

        } catch (RekognitionException | FileNotFoundException e) {
            log.info(e.getMessage());
            System.exit(1);
        }
    }

    public void listFacesCollection(RekognitionClient rekognitionClient, String collectionId) {
        try {
            ListFacesRequest facesRequest = ListFacesRequest.builder()
                    .collectionId(collectionId)
                    .maxResults(10)
                    .build();

            ListFacesResponse facesResponse = rekognitionClient.listFaces(facesRequest);
            List<Face> faces = facesResponse.faces();
            for (Face face: faces) {
                log.info("Confidence level there is a face: {}",face.confidence());
                log.info("The face Id value is {}",face.faceId());
            }

        } catch (RekognitionException e) {
            log.info(e.getMessage());
            System.exit(1);
        }
    }
}
