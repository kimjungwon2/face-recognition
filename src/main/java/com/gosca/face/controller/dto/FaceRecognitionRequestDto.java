package com.gosca.face.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class FaceRecognitionRequestDto {
    @NotNull
    @Positive
    private Long storeId;

    @NotBlank
    private String storeType;
}
