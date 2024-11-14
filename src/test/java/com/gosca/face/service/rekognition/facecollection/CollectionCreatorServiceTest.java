package com.gosca.face.service.rekognition.facecollection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CollectionCreatorServiceTest {

    @Autowired
    CollectionCreatorService collectionCreatorService;

    @Test
    void createMyCollection() {
        String collectionId = "TEST";

        collectionCreatorService.createMyCollection(collectionId);

    }
}