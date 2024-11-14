package com.gosca.face.controller;

import com.gosca.face.service.webcam.WebcamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/webcam")
public class WebcamController {

    private final WebcamService webcamService;

    public WebcamController(WebcamService webcamService) {
        this.webcamService = webcamService;
    }

    @GetMapping
    public String webcamPage() {
        return "webcam";
    }

    @PostMapping("/capture")
    @ResponseBody
    public ResponseEntity<String> saveCapture(@RequestParam("image") String base64Image) {
        try {
            String filename = webcamService.saveCapture(base64Image);
            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 저장 실패: " + e.getMessage());
        }
    }
}