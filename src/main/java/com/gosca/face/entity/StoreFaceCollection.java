package com.gosca.face.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@DynamoDBTable(tableName = "store_face_collection")
public class StoreFaceCollection {

    @DynamoDBHashKey(attributeName = "collection_id")
    private String collectionId;

    @DynamoDBAttribute(attributeName = "store_id")
    private Integer storeId;

    @DynamoDBAttribute(attributeName = "store_type")
    private String storeType;

    @DynamoDBAttribute(attributeName = "created_date")
    private String createdDate;

    @DynamoDBAttribute(attributeName = "updated_date")
    private String updatedDate;

}
