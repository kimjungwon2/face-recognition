package com.gosca.face.service.rekognition.facecollection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.rekognition.model.DetectFacesRequest;
import software.amazon.awssdk.services.rekognition.model.DetectFacesResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Attribute;
import software.amazon.awssdk.services.rekognition.model.FaceDetail;
import software.amazon.awssdk.services.rekognition.model.AgeRange;

import java.util.List;

@Slf4j
@Service
public class ImageFaceDetector {
    public void isFaceValidForRegistrationWithS3(
            RekognitionClient rekognitionClient,
            String bucket,
            String imagePath
    ){
        try {
            S3Object s3Object = S3Object.builder()
                    .bucket(bucket)
                    .name(imagePath)
                    .build() ;

            Image myImage = Image.builder()
                    .s3Object(s3Object)
                    .build();

            DetectFacesRequest facesRequest = DetectFacesRequest.builder()
                    .attributes(Attribute.FACE_OCCLUDED)
                    .image(myImage)
                    .build();

            DetectFacesResponse facesResponse = rekognitionClient.detectFaces(facesRequest);
            List<FaceDetail> faceDetails = facesResponse.faceDetails();
            log.info("faceDetails:{}", faceDetails);

        } catch (RekognitionException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
