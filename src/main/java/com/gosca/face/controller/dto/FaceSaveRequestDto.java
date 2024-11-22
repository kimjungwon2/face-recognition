package com.gosca.face.controller.dto;

import lombok.Data;

@Data
public class FaceSaveRequestDto {

    private Long userId;
    private Long storeId;
    private String storeType;
}
