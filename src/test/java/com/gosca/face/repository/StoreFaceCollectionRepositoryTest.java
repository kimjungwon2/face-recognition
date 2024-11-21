package com.gosca.face.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gosca.face.entity.StoreFaceCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;


@SpringBootTest
class StoreFaceCollectionRepositoryTest {
    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<StoreFaceCollection> storeFaceCollectionTable;

    @Autowired
    private StoreFaceCollectionRepository repository;


    @Test
    void testSave() {
        // Given
        StoreFaceCollection storeFaceCollection = StoreFaceCollection.builder()
                .collectionId("gosca_1")
                .storeType("gosca")
                .storeId(1L)
                .build();

        // When
        repository.save(storeFaceCollection);
    }


}