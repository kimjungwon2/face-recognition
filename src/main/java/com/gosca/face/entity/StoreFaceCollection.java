package com.gosca.face.entity;


import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@DynamoDbBean
public class StoreFaceCollection {

    private String collectionId;
    private Long storeId;
    private String storeType;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    @Builder
    public StoreFaceCollection(String collectionId, Long storeId, String storeType, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.collectionId = collectionId;
        this.storeId = storeId;
        this.storeType = storeType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    @DynamoDbPartitionKey
    public String getCollectionId() {
        return collectionId;
    }

    @DynamoDbSortKey
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }
}
