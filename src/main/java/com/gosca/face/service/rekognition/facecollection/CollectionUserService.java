package com.gosca.face.service.rekognition.facecollection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CreateUserRequest;
import com.amazonaws.services.rekognition.model.User;
import com.amazonaws.services.rekognition.model.AssociateFacesRequest;
import com.amazonaws.services.rekognition.model.AssociateFacesResult;
import com.amazonaws.services.rekognition.model.ListUsersRequest;
import com.amazonaws.services.rekognition.model.ListUsersResult;

import java.util.Arrays;
import java.util.List;



@Slf4j
@Service
public class CollectionUserService {

    public void createUser(AmazonRekognition rekognitionClient, String collectionId, String userId) {
        CreateUserRequest request = new CreateUserRequest()
                .withCollectionId(collectionId)
                .withUserId(userId);

        rekognitionClient.createUser(request);
    }

    public void getUserLists(AmazonRekognition rekognitionClient, String collectionId){
        int limit = 10;
        ListUsersResult listUsersResult = null;
        String paginationToken = null;
        do {
            if (listUsersResult != null) {
                paginationToken = listUsersResult.getNextToken();
            }
            ListUsersRequest request = new ListUsersRequest()
                    .withCollectionId(collectionId)
                    .withMaxResults(limit)
                    .withNextToken(paginationToken);
            listUsersResult = rekognitionClient.listUsers(request);

            List<User> users = listUsersResult.getUsers();
            for (User currentUser: users) {
                System.out.println(currentUser.getUserId() + " : " + currentUser.getUserStatus());
            }
        } while (listUsersResult.getNextToken() != null);
    }

    public void associateFace(AmazonRekognition rekognitionClient, String collectionId, String faceId, String userId){

        System.out.println("Associating faces to the existing user: " + userId);
        List<String> faceIds = Arrays.asList(faceId);

        AssociateFacesRequest request = new AssociateFacesRequest()
                .withCollectionId(collectionId)
                .withUserId(userId)
                .withFaceIds(faceIds);

        AssociateFacesResult result = rekognitionClient.associateFaces(request);

        System.out.println("Successful face associations: " + result.getAssociatedFaces().size());
        System.out.println("Unsuccessful face associations: " + result.getUnsuccessfulFaceAssociations().size());
    }

}
