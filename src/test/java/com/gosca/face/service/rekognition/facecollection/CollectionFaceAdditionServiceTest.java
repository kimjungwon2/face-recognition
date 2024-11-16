package com.gosca.face.service.rekognition.facecollection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CollectionFaceAdditionServiceTest {

    @Autowired
    CollectionFaceAdditionService collectionFaceAdditionService;

    @Test
    void test(){
        String sourceImage = "C:\\Users\\gram\\Desktop\\face\\src\\main\\resources\\지연.jpg";

        collectionFaceAdditionService.addToCollection("GOSCA_TEST",sourceImage);
    }
}