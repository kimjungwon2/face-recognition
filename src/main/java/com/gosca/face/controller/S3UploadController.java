package com.gosca.face.controller;

import com.gosca.face.service.picture.FaceImageSaveService;
import com.gosca.face.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class S3UploadController {

    private final FaceImageSaveService faceImageSaveService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비어있습니다.");
            }

            log.info("파일 업로드 시작: {}", file.getOriginalFilename());

            String collectionId="GOSCA_TEST10";

            faceImageSaveService.saveUserFace(collectionId, file);

            return ResponseEntity.ok("저장 완료");

        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
