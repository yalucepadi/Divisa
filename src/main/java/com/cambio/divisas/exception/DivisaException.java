package com.cambio.divisas.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class DivisaException extends RuntimeException {

    private String message;

    public DivisaException(String message) {
        super(message);
        this.message = message;
    }


}
