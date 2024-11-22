package com.gosca.face.service.rekognition.facecollection;

import com.amazonaws.services.rekognition.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.amazonaws.services.rekognition.AmazonRekognition;

import java.util.Arrays;
import java.util.List;



@Slf4j
@Service
public class CollectionUserService {


    public void createUser(AmazonRekognition rekognitionClient, String collectionId, Long userId) {

        CreateUserRequest request = new CreateUserRequest()
                .withCollectionId(collectionId)
                .withUserId(String.valueOf(userId));

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

        System.out.println("Associating faces to the existing userId: " + userId);
        List<String> faceIds = Arrays.asList(faceId);

        AssociateFacesRequest request = new AssociateFacesRequest()
                .withCollectionId(collectionId)
                .withUserId(userId)
                .withFaceIds(faceIds);

        AssociateFacesResult result = rekognitionClient.associateFaces(request);

        log.info("Successful face associations: {}", result.getAssociatedFaces().size());
        log.info("Unsuccessful face associations: {}", result.getUnsuccessfulFaceAssociations().size());

        if(result.getUnsuccessfulFaceAssociations().size()>=1) {
            log.error("result:{}", result);
            deleteUser(rekognitionClient, collectionId, userId);
            throw new IllegalStateException("유저와 얼굴 연계가 안 됐습니다.");
        }
    }

    public void deleteUser(AmazonRekognition rekognitionClient, String collectionId, String userId){
        DeleteUserRequest request = new DeleteUserRequest()
                .withCollectionId(collectionId)
                .withUserId(userId);

        rekognitionClient.deleteUser(request);
    }

}
