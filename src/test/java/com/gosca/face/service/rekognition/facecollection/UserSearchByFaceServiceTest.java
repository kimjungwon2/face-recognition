package com.gosca.face.service.rekognition.facecollection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserSearchByFaceServiceTest {

    @Autowired
    private UserSearchByFaceService userSearchByFaceService;

    @Test
    void searchUser() throws Exception {
        String collectionId = "GOSCA_TEST";
        String sourceImage = "C:\\Users\\gram\\Desktop\\face\\src\\main\\resources\\유재석.jpg";

        userSearchByFaceService.searchUser(sourceImage,collectionId);
    }


}