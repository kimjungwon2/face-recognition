package com.gosca.face.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@DynamoDbBean
public class StoreUserFaceCollection {
    private String userFaceCollectionKey;
    private String collectionId;
    private Long storeId;
    private Long userId;
    private String faceId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    @Builder
    public StoreUserFaceCollection(String userFaceCollectionKey, String collectionId, Long storeId, Long userId, String faceId, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.userFaceCollectionKey = userFaceCollectionKey;
        this.collectionId = collectionId;
        this.storeId = storeId;
        this.userId = userId;
        this.faceId = faceId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    @DynamoDbPartitionKey
    public String getUserFaceCollectionKey() {
        return userFaceCollectionKey;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"userId-collectionId-index"})
    public Long getUserId() {
        return userId;
    }

    @DynamoDbSecondarySortKey(indexNames = {"userId-collectionId-index"})
    public String getCollectionId() {
        return collectionId;
    }

}
