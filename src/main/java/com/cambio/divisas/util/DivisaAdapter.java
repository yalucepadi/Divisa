package com.cambio.divisas.util;

import com.cambio.divisas.domain.response.ResponseGeneralDto;

public class DivisaAdapter {
    public static ResponseGeneralDto responseGeneral(String code, Integer status, String message, Object data) {
        return ResponseGeneralDto.builder()
                .status(status)
                .code(code)
                .comment(message)
                .data(data)
                .build();



    }}
