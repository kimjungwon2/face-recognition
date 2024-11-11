package com.gosca.face;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaceApplication {

    public static void main(String[] args) {

        String sourceImage = "138bdfca-3e86-4c09-9632-d22df52a0484.jpg";
        String targetImage = "679760_1044505_3053.jpg";
        Float similarityThreshold = 70F;
        Region region = Region.AP_NORTHEAST_2;


        RekognitionClient rekClient = RekognitionClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create("default"))
                .build();


        compareTwoFaces(rekClient, similarityThreshold, sourceImage, targetImage);
        rekClient.close();
    }

    public static void compareTwoFaces(RekognitionClient rekClient, Float similarityThreshold, String sourceImage, String targetImage) {
        try (
                InputStream sourceStream = FaceApplication.class.getClassLoader().getResourceAsStream(sourceImage);
                InputStream targetStream = FaceApplication.class.getClassLoader().getResourceAsStream(targetImage)
        ) {
            if (sourceStream == null || targetStream == null) {
                System.err.println("One or both image files were not found in the resources folder.");
                System.exit(1);
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

            CompareFacesResponse compareFacesResult = rekClient.compareFaces(facesRequest);
            List<CompareFacesMatch> faceDetails = compareFacesResult.faceMatches();

            for (CompareFacesMatch match : faceDetails) {
                ComparedFace face = match.face();
                BoundingBox position = face.boundingBox();
                System.out.println("Face at " + position.left() + " " + position.top()
                        + " matches with " + face.confidence() + "% confidence.");
            }

            List<ComparedFace> uncompared = compareFacesResult.unmatchedFaces();
            System.out.println("There were " + uncompared.size() + " face(s) that did not match");
            System.out.println("Source image rotation: " + compareFacesResult.sourceImageOrientationCorrection());
            System.out.println("Target image rotation: " + compareFacesResult.targetImageOrientationCorrection());

        } catch (RekognitionException e) {
            System.err.println("Error with Rekognition service: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unknown error occurred: " + e.getMessage());
            System.exit(1);
        }
    }
}
