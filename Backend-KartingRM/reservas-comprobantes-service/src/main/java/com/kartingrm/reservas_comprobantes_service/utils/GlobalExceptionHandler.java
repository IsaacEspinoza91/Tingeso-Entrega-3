package com.kartingrm.reservas_comprobantes_service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        if (ex.getMessage().contains("Ya existe una reserva con ese horario")) {
            return new ResponseEntity<>("Conflicto: " + ex.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>("Recurso no encontrado: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservaCreationException.class)
    public ResponseEntity<String> handleReservaCreationException(ReservaCreationException ex) {
        return new ResponseEntity<>("Error en la creación de la reserva: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceIntegrationException.class)
    public ResponseEntity<String> handleServiceIntegrationException(ServiceIntegrationException ex) {
        return new ResponseEntity<>("Error de integración con servicio externo: " + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE); // O HttpStatus.BAD_GATEWAY
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Error interno inesperado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Error inesperado en el servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}