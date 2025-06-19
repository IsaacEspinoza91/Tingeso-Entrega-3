package com.kartingrm.reservas_comprobantes_service.controller;

import com.kartingrm.reservas_comprobantes_service.entity.Comprobante;
import com.kartingrm.reservas_comprobantes_service.model.ComprobanteConDetallesDTO;
import com.kartingrm.reservas_comprobantes_service.service.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/reservas-comprobantes-service/comprobantes")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    // GET comprobante segun id
    @GetMapping("/{idComprobante}")
    public ResponseEntity<?> getComprobante(@PathVariable("idComprobante") Long idComprobante) {
        try {
            ComprobanteConDetallesDTO comprobante = comprobanteService.getComprobanteConDetallesById(idComprobante);
            return ResponseEntity.ok(comprobante);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET comprobante segun id reserva
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<?> getComprobanteByIdReserva(@PathVariable("idReserva") Long idReserva) {
        try {
            ComprobanteConDetallesDTO comprobante = comprobanteService.getComprobanteConDetallesByIdReserva(idReserva);
            return ResponseEntity.ok(comprobante);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // CREATE comprobante con todos sus detalles
    // Deben estar previamente los integrantes asociados a la reserva (tabla cliente_reserva)
    @PostMapping("/reserva/{reservaId}/descuento-extra/{descuentoExtra}")
    public ResponseEntity<?> createComprobante(@PathVariable Long reservaId, @PathVariable double descuentoExtra) {
        try {
            ComprobanteConDetallesDTO comprobante = comprobanteService.createComprobante(reservaId, descuentoExtra);
            return ResponseEntity.ok(comprobante);
        } catch (IllegalArgumentException e) {
            // Ya existe un comprobante y descuento negativo
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // No hay clientes asociados y No están todos los clientes
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            // Reserva no encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // UPDATE
    // Actualizar estado pagado de comprobante
    @PutMapping("/{id}/estado/{pagado}")
    public ResponseEntity<?> updatePagadoDeComprobante(@PathVariable Long id, @PathVariable boolean pagado) {
        try {
            Comprobante comprobante = comprobanteService.updateEstadoPagadoDeComprobante(id, pagado);
            return ResponseEntity.ok(comprobante);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE comprobante con todos sus detalles
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComprobante(@PathVariable Long id) {
        try {
            comprobanteService.deleteComprobante(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
