package com.gosca.face.service.rekognition.facecollection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FaceSearchServiceTest {

    @Autowired
    FaceSearchService faceSearchService;

    @Test
    void searchFaceInCollection() {
        String sourceImage = "C:\\Users\\gram\\Desktop\\face\\src\\main\\resources\\지연.jpg";

        faceSearchService.searchFaceInCollection("GOSCA_TEST", sourceImage);
    }
}