package com.gosca.face.service.rekognition.facecollection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class CollectionFaceService {


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

            if(facesResponse!=null && facesResponse.faceRecords().size()==0){
                throw new IllegalStateException("얼굴이 인식되지 않았습니다.");
            }

            List<FaceRecord> faceRecords = facesResponse.faceRecords();
            for (FaceRecord faceRecord : faceRecords) {
                log.info("  Face ID: {}", faceRecord.face().faceId());
                log.info("  Location:{}", faceRecord.faceDetail().boundingBox().toString());
            }

            List<UnindexedFace> unindexedFaces = facesResponse.unindexedFaces();
            log.info("Faces not indexed:");
            for (UnindexedFace unindexedFace : unindexedFaces) {
                log.info("  Location:{}",unindexedFace.faceDetail().boundingBox().toString());

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


    public void searchFaceInCollection(RekognitionClient rekognitionClient, String collectionId, String sourceImage) {

        try {
            InputStream sourceStream = new FileInputStream(new File(sourceImage));
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
            Image souImage = Image.builder()
                    .bytes(sourceBytes)
                    .build();

            SearchFacesByImageRequest facesByImageRequest = SearchFacesByImageRequest.builder()
                    .image(souImage)
                    .maxFaces(10)
                    .faceMatchThreshold(80F)
                    .collectionId(collectionId)
                    .build();

            SearchFacesByImageResponse imageResponse = rekognitionClient.searchFacesByImage(facesByImageRequest);
            System.out.println("Faces matching in the collection");
            List<FaceMatch> faceImageMatches = imageResponse.faceMatches();
            for (FaceMatch face: faceImageMatches) {
                System.out.println("The similarity level is  "+face.similarity());
                System.out.println();
            }

        } catch (RekognitionException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
