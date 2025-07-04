package com.kartingrm.reservas_comprobantes_service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Opcional: Puedes usar @ResponseStatus para dar un HTTP status por defecto si no lo manejas en @ControllerAdvice
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // O HttpStatus.BAD_GATEWAY si el otro servicio responde mal
public class ServiceIntegrationException extends RuntimeException {

    public ServiceIntegrationException(String message) {
        super(message);
    }

    public ServiceIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}