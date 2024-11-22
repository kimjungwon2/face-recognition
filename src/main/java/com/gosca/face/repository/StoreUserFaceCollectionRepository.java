package com.gosca.face.repository;

import com.gosca.face.entity.StoreUserFaceCollection;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class StoreUserFaceCollectionRepository {
    private final DynamoDbTable<StoreUserFaceCollection> storeUserFaceCollectionTable;

    public StoreUserFaceCollectionRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.storeUserFaceCollectionTable = dynamoDbEnhancedClient.table("store_user_face_collection", TableSchema.fromBean(StoreUserFaceCollection.class));
    }

    public void save(StoreUserFaceCollection collection) {

        StoreUserFaceCollection updatedCollection = StoreUserFaceCollection.builder()
                .userFaceCollectionKey(collection.getUserFaceCollectionKey())
                .collectionId(collection.getCollectionId())
                .userId(collection.getUserId())
                .storeId(collection.getStoreId())
                .createdDate(collection.getCreatedDate() != null ? collection.getCreatedDate().withNano(0) : LocalDateTime.now().withNano(0))
                .updatedDate(collection.getCreatedDate() != null ? collection.getCreatedDate().withNano(0) : LocalDateTime.now().withNano(0))
                .build();

        storeUserFaceCollectionTable.putItem(updatedCollection);
    }

    public Optional<StoreUserFaceCollection> findByUserIdAndCollectionId(Long userId, String collectionId) {

        DynamoDbIndex<StoreUserFaceCollection> userIdIndex = storeUserFaceCollectionTable.index("userId-collectionId-index");

        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(collectionId)
                .build();

        try {
            return userIdIndex.query(r -> r.queryConditional(QueryConditional.keyEqualTo(key)))
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .findFirst();
        } catch (DynamoDbException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
