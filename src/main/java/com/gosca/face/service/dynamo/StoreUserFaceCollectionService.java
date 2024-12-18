package com.gosca.face.service.dynamo;

import com.gosca.face.entity.StoreFaceCollection;
import com.gosca.face.entity.StoreUserFaceCollection;
import com.gosca.face.repository.StoreUserFaceCollectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StoreUserFaceCollectionService {
    private final StoreUserFaceCollectionRepository storeUserFaceCollectionRepository;

    public StoreUserFaceCollectionService(StoreUserFaceCollectionRepository storeUserFaceCollectionRepository) {
        this.storeUserFaceCollectionRepository = storeUserFaceCollectionRepository;
    }

    public void saveStoreUserFaceCollection(
            String userFaceCollectionKey,
            String collectionId,
            String faceId,
            Long userId,
            Long storeId
    ){
        StoreUserFaceCollection storeUserFaceCollection = StoreUserFaceCollection.builder()
                .userFaceCollectionKey(userFaceCollectionKey)
                .collectionId(collectionId)
                .storeId(storeId)
                .userId(userId)
                .faceId(faceId)
                .build();

        storeUserFaceCollectionRepository.save(storeUserFaceCollection);
    }

    public void checkUserIdAndCollectionId(Long userId, String collectionId){
        Optional<StoreUserFaceCollection> storeUserFaceCollection = storeUserFaceCollectionRepository.findByUserIdAndCollectionId(userId, collectionId);

        if(storeUserFaceCollection.isPresent()){
            log.error("이미 해당 지점에 존재하는 유저 collectionId: {}, userId:{}", collectionId, userId);
            throw new IllegalStateException("이미 해당 지점에 존재하는 유저입니다.");
        }
    }
}
