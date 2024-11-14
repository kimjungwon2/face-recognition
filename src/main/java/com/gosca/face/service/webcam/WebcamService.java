package com.gosca.face.service.webcam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@Slf4j
public class WebcamService {

    @Value("${capture.save.path:/captures}")
    private String capturePath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(capturePath));
        } catch (IOException e) {
            throw new RuntimeException("캡처 저장 디렉토리 생성 실패", e);
        }
    }

    public String saveCapture(String base64Image) throws IOException {
        String[] parts = base64Image.split(",");
        String imageData = parts.length > 1 ? parts[1] : parts[0];


        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".png";
        Path filepath = Paths.get(capturePath, filename);

        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        Files.write(filepath, imageBytes);

        log.info("캡처 이미지 저장됨: {}", filepath);
        return filename;
    }
}