package com.gosca.face.service.name;

import org.springframework.stereotype.Service;

@Service
public class NameGeneratorService {

    public String generateFaceCollectionId(String storeType, Long storeId){
        StringBuilder collectionId = new StringBuilder();

        collectionId.append(storeType);
        collectionId.append("_");
        collectionId.append(storeId);

        return collectionId.toString();
    }

    public String generateUserFaceCollectionKey(String collectionId, Long userId){
        StringBuilder userFaceCollectionKey = new StringBuilder();

        userFaceCollectionKey.append(collectionId);
        userFaceCollectionKey.append("#");
        userFaceCollectionKey.append(userId);

        return userFaceCollectionKey.toString();
    }
}
