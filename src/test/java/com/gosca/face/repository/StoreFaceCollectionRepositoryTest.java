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

import java.util.List;


@SpringBootTest
class StoreFaceCollectionRepositoryTest {

    @Autowired
    private StoreFaceCollectionRepository repository;


    @Test
    void findByCollectionId(){
        StoreFaceCollection storeFaceCollection = repository.findByCollectionIdAndStoreId("gosca_1",1L);

        System.out.println(storeFaceCollection);
    }

    @Test
    void testSave() {
        // Given
        StoreFaceCollection storeFaceCollection = StoreFaceCollection.builder()
                .collectionId("gosca_2")
                .storeType("gosca")
                .storeId(2L)
                .build();

        // When
        repository.save(storeFaceCollection);
    }

    @Test
    void findAll(){
        List<StoreFaceCollection> storeFaceCollectionList = repository.findAll();
        System.out.println(storeFaceCollectionList);
    }

    @Test
    void delete(){
        repository.delete("gosca_1",1L);
    }


}