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

    @Test
    void associcateUser(){
        collectionUserService.associateFace("GOSCA_TEST", "f05a32be-13d8-470c-8cb7-f129c68cd987","GOSCA_2");
    }

}