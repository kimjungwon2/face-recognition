package com.gosca.face.service.rekognition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.BoundingBox;
import software.amazon.awssdk.services.rekognition.model.CompareFacesMatch;
import software.amazon.awssdk.services.rekognition.model.CompareFacesRequest;
import software.amazon.awssdk.services.rekognition.model.CompareFacesResponse;
import software.amazon.awssdk.services.rekognition.model.ComparedFace;
import software.amazon.awssdk.core.SdkBytes;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class FaceSampleService {
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

    public void compareTwoFaces(String sourceImage, String targetImage, Float similarityThreshold) {
        try (
                InputStream sourceStream = getClass().getClassLoader().getResourceAsStream(sourceImage);
                InputStream targetStream = getClass().getClassLoader().getResourceAsStream(targetImage)
        ) {
            if (sourceStream == null || targetStream == null) {
                log.info("One or both image files were not found in the resources folder.");
                return;
            }

            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
            SdkBytes targetBytes = SdkBytes.fromInputStream(targetStream);

            Image souImage = Image.builder().bytes(sourceBytes).build();
            Image tarImage = Image.builder().bytes(targetBytes).build();

            CompareFacesRequest facesRequest = CompareFacesRequest.builder()
                    .sourceImage(souImage)
                    .targetImage(tarImage)
                    .similarityThreshold(similarityThreshold)
                    .build();

            CompareFacesResponse compareFacesResult = rekognitionClient.compareFaces(facesRequest);
            List<CompareFacesMatch> faceDetails = compareFacesResult.faceMatches();

            for (CompareFacesMatch match : faceDetails) {
                ComparedFace face = match.face();
                BoundingBox position = face.boundingBox();
                log.info("Face at {} {} matches with {}% confidence.", position.left(), position.top(), face.confidence());
            }

            List<ComparedFace> uncompared = compareFacesResult.unmatchedFaces();
            log.info("There were {} face(s) that did not match", uncompared.size());
            log.info("Source image rotation: {}", compareFacesResult.sourceImageOrientationCorrection());
            log.info("Target image rotation: {}", compareFacesResult.targetImageOrientationCorrection());

        } catch (RekognitionException e) {
            log.error("Error with Rekognition service: {}", e.getMessage());
        } catch (Exception e) {
            log.error("An unknown error occurred: {}", e.getMessage());
        }
    }

    public void closeClient() {
        rekognitionClient.close();
    }

}
