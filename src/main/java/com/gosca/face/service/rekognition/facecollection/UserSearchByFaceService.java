package com.gosca.face.service.rekognition.facecollection;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.SearchUsersByImageRequest;
import com.amazonaws.services.rekognition.model.SearchUsersByImageResult;
import com.amazonaws.services.rekognition.model.UserMatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.amazonaws.services.rekognition.model.Image;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


@Slf4j
@Service
public class UserSearchByFaceService {

    public String searchUser(AmazonRekognition rekognitionClient, String sourceImage, String collectionId) {
        Image image = createImageFromFile(sourceImage);

        SearchUsersByImageRequest request = new SearchUsersByImageRequest()
                .withCollectionId(collectionId)
                .withImage(image)
                .withUserMatchThreshold(80F)
                .withMaxUsers(1);

        SearchUsersByImageResult result =
                rekognitionClient.searchUsersByImage(request);

        System.out.println("Printing search result with matched user and similarity score");
        for (UserMatch match: result.getUserMatches()) {
            System.out.println(match.getUser().getUserId() + " with similarity score " + match.getSimilarity());
        }

        return result.getUserMatches().get(0).getUser().getUserId();
    }


    public String searchUser(AmazonRekognition rekognitionClient, MultipartFile sourceImage, String collectionId) {
        Image image;

        try {
            image = createImageFromMultipartFile(sourceImage);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일 처리 중 오류 발생", e);
        }

        SearchUsersByImageRequest request = new SearchUsersByImageRequest()
                .withCollectionId(collectionId)
                .withImage(image)
                .withUserMatchThreshold(80F)
                .withMaxUsers(1);

        try {
            SearchUsersByImageResult result = rekognitionClient.searchUsersByImage(request);
            System.out.println("Printing search result with matched user and similarity score");

            for (UserMatch match : result.getUserMatches()) {
                System.out.println(match.getUser().getUserId() + " with similarity score " + match.getSimilarity());
            }

            if (result.getUserMatches().isEmpty()) {
                throw new IllegalStateException("얼굴 인식되는 유저가 없습니다.");
            }

            return result.getUserMatches().get(0).getUser().getUserId();
        } catch(RekognitionException e){
            log.info(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Image createImageFromMultipartFile(MultipartFile file) throws IOException {
        ByteBuffer imageBytes = ByteBuffer.wrap(file.getBytes());
        return new Image().withBytes(imageBytes);
    }



    private Image createImageFromFile(String filePath) {
        File file = new File(filePath);
        FileInputStream inputStream = null;
        FileChannel fileChannel = null;

        try {
            inputStream = new FileInputStream(file);
            fileChannel = inputStream.getChannel();

            ByteBuffer imageBytes = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(imageBytes);
            imageBytes.flip();

            return new Image().withBytes(imageBytes);

        } catch (Exception e) {
            e.printStackTrace();

            throw new IllegalStateException("Error while creating Image from file: " + e.getMessage(), e);
        } finally {

            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
