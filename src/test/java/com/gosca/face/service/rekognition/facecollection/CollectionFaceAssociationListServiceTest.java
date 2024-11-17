package com.gosca.face.service.rekognition.facecollection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CollectionFaceAssociationListServiceTest {
    @Autowired
    CollectionFaceAssociationListService collectionFaceAssociationListService;


    @Test
    void listFacesCollection() {
        collectionFaceAssociationListService.listFacesCollection("GOSCA_TEST");
    }
}