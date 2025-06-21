package com.kartingrm.reservas_comprobantes_service.controller;

import com.kartingrm.reservas_comprobantes_service.entity.Comprobante;
import com.kartingrm.reservas_comprobantes_service.model.ComprobanteConDetallesDTO;
import com.kartingrm.reservas_comprobantes_service.service.ComprobanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/reservas-comprobantes-service/comprobantes")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;
    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    // GET comprobante segun id
    @GetMapping("/{idComprobante}")
    public ResponseEntity<Object> getComprobante(@PathVariable("idComprobante") Long idComprobante) {
        try {
            ComprobanteConDetallesDTO comprobante = comprobanteService.getComprobanteConDetallesById(idComprobante);
            return ResponseEntity.ok(comprobante);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }

    // GET comprobante segun id reserva
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<Object> getComprobanteByIdReserva(@PathVariable("idReserva") Long idReserva) {
        try {
            ComprobanteConDetallesDTO comprobante = comprobanteService.getComprobanteConDetallesByIdReserva(idReserva);
            return ResponseEntity.ok(comprobante);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }

    // CREATE comprobante con todos sus detalles
    // Deben estar previamente los integrantes asociados a la reserva (tabla cliente_reserva)
    @PostMapping("/reserva/{reservaId}/descuento-extra/{descuentoExtra}")
    public ResponseEntity<Object> createComprobante(@PathVariable Long reservaId, @PathVariable double descuentoExtra) {
        try {
            ComprobanteConDetallesDTO comprobante = comprobanteService.createComprobante(reservaId, descuentoExtra);
            return ResponseEntity.ok(comprobante);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Ya existe un comprobante, descuento negativo, o problemas con clientes
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            // Reserva o plan no encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (HttpServerErrorException e) {
            // Servicio de planes no disponible
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    // UPDATE
    // Actualizar estado pagado de comprobante
    @PutMapping("/{id}/estado/{pagado}")
    public ResponseEntity<Object> updatePagadoDeComprobante(@PathVariable Long id, @PathVariable boolean pagado) {
        try {
            Comprobante comprobante = comprobanteService.updateEstadoPagadoDeComprobante(id, pagado);
            return ResponseEntity.ok(comprobante);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }

    // DELETE comprobante con todos sus detalles
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteComprobante(@PathVariable Long id) {
        try {
            comprobanteService.deleteComprobante(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }
}
