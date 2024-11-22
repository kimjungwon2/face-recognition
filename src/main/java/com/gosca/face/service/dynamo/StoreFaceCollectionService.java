package com.gosca.face.service.dynamo;

import com.gosca.face.entity.StoreFaceCollection;
import com.gosca.face.repository.StoreFaceCollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreFaceCollectionService {
    private final StoreFaceCollectionRepository storeFaceCollectionRepository;

    public StoreFaceCollectionService(StoreFaceCollectionRepository storeFaceCollectionRepository) {
        this.storeFaceCollectionRepository = storeFaceCollectionRepository;
    }

    public boolean isCollectionIdPresent(String collectionId, Long storeId){
        StoreFaceCollection storeFaceCollection = storeFaceCollectionRepository.findByCollectionIdAndStoreId(collectionId, storeId);

        if(storeFaceCollection == null) return false;

        return true;
    }

    public void saveFaceCollection(String collectionId, String storeType, Long storeId){
        StoreFaceCollection storeFaceCollection = StoreFaceCollection.builder()
                .collectionId(collectionId)
                .storeType(storeType)
                .storeId(storeId)
                .build();

        storeFaceCollectionRepository.save(storeFaceCollection);
    }

}
