package com.kartingrm.reservas_comprobantes_service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservaCreationException extends RuntimeException {

    public ReservaCreationException(String message) {
        super(message);
    }

    public ReservaCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}