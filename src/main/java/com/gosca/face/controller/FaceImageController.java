package com.gosca.face.controller;

import com.gosca.face.controller.dto.FaceRecognitionRequestDto;
import com.gosca.face.controller.dto.FaceSaveRequestDto;
import com.gosca.face.service.picture.FaceImageSaveService;
import com.gosca.face.service.picture.FaceRecognitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FaceImageController {

    private final FaceImageSaveService faceImageSaveService;
    private final FaceRecognitionService faceRecognitionService;

    @PostMapping("/test/upload")
    public ResponseEntity<String> uploadTestFile(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비어있습니다.");
            }

            log.info("파일 업로드 시작: {}", file.getOriginalFilename());

            String collectionId="GOSCA_TEST11";
            Long userId = 1L;

            faceImageSaveService.saveTestUserFace(file, collectionId, userId);

            return ResponseEntity.ok("저장 완료");

        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Validated FaceSaveRequestDto request
    ) {
       if (file.isEmpty()) {
           return ResponseEntity.badRequest().body("파일이 비어있습니다.");
       }

       log.info("파일 업로드 시작: {}", file.getOriginalFilename());

       faceImageSaveService.saveUserFace(file, request);

       return ResponseEntity.ok("저장 완료");
    }

    @PostMapping("/face/recognition")
    public ResponseEntity<String> recognitionFace(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Validated FaceRecognitionRequestDto request
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
        }

        log.info("파일 업로드 시작: {}", file.getOriginalFilename());

        String userId = faceRecognitionService.userSearchByFace(file, request);

        return ResponseEntity.ok(userId);
    }


}
