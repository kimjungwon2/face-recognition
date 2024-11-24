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

    @GetMapping("/save")
    public String webcamPage() {
        return "webcam";
    }

    @GetMapping("/recognition")
    public String faceRecognitionPage() {
        return "recognition";
    }

}