package com.gosca.face.repository;

import com.gosca.face.entity.StoreFaceCollection;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class StoreFaceCollectionRepository {
    private final DynamoDbTable<StoreFaceCollection> storeFaceCollectionTable;

    public StoreFaceCollectionRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.storeFaceCollectionTable = dynamoDbEnhancedClient.table("store_face_collection", TableSchema.fromBean(StoreFaceCollection.class));;
    }

    public void save(StoreFaceCollection collection) {

        StoreFaceCollection updatedCollection = StoreFaceCollection.builder()
                .collectionId(collection.getCollectionId())
                .storeType(collection.getStoreType())
                .storeId(collection.getStoreId())
                .createdDate(collection.getCreatedDate() != null ? collection.getCreatedDate() : LocalDateTime.now())
                .updatedDate(collection.getCreatedDate() != null ? collection.getCreatedDate() : LocalDateTime.now())
                .build();

        storeFaceCollectionTable.putItem(updatedCollection);
    }

    public StoreFaceCollection findByCollectionId(String collectionId) {
        return storeFaceCollectionTable.getItem(r -> r.key(k -> k.partitionValue(collectionId)));
    }

    public List<StoreFaceCollection> findAll() {
        Iterator<StoreFaceCollection> results = storeFaceCollectionTable.scan().items().iterator();
        List<StoreFaceCollection> collections = new ArrayList<>();
        results.forEachRemaining(collections::add);
        return collections;
    }

    public List<StoreFaceCollection> findByStoreType(String storeType) {
        Iterator<StoreFaceCollection> results = storeFaceCollectionTable.query(
                r -> r.queryConditional(
                        QueryConditional.keyEqualTo(k -> k.partitionValue(storeType))
                )
        ).items().iterator();

        List<StoreFaceCollection> collections = new ArrayList<>();
        results.forEachRemaining(collections::add);

        return collections;
    }

    public void delete(String collectionId) {
        storeFaceCollectionTable.deleteItem(r -> r.key(k -> k.partitionValue(collectionId)));
    }


}
