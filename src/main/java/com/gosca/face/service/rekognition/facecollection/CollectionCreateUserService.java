package com.gosca.face.service.rekognition.facecollection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateUserRequest;
import com.amazonaws.services.rekognition.model.CreateUserResult;


@Slf4j
@Service
public class CollectionCreateUserService {

    public void createUser(String collectionId, String userId) {
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.


        CreateUserRequest request = new CreateUserRequest()
                .withCollectionId(collectionId)
                .withUserId(userId);


        rekognitionClient.createUser(request);
    }

}
