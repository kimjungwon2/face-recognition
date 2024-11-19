package com.gosca.face.service.rekognition.facecollection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CollectionUserServiceTest {

    @Autowired
    CollectionUserService collectionUserService;

    @Test
    void test(){
        collectionUserService.createUser("GOSCA_TEST","GOSCA_3");
    }

    @Test
    void getUserLists(){
        collectionUserService.getUserLists("GOSCA_TEST");
    }

}