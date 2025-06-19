package com.kartingrm.reservas_comprobantes_service.controller;

import com.kartingrm.reservas_comprobantes_service.entity.ClienteReserva;
import com.kartingrm.reservas_comprobantes_service.model.ClienteDTO;
import com.kartingrm.reservas_comprobantes_service.service.ClienteReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas-comprobantes-service/cliente-reserva")
public class ClienteReservaController {

    private final ClienteReservaService service;

    public ClienteReservaController(ClienteReservaService service) {
        this.service = service;
    }

    @GetMapping("/reserva/{idReserva}")
    public List<ClienteReserva> obtenerIntegrantesByIdReserva(@PathVariable Long idReserva) {
        return service.obtenerIntegrantesByIdReserva(idReserva);
    }

    @GetMapping("/ClienteDTO/reserva/{id}")
    public ResponseEntity<List<ClienteDTO>> getIntegrantesDTOByIdReserva(@PathVariable Long id) {
        List<ClienteDTO> reserva =  service.obtenerIntegrantesDTOByIdReserva(id);
        return ResponseEntity.ok(reserva);
    }

    @PostMapping("/agregar/cliente/{idCliente}/reserva/{idReserva}")
    public ResponseEntity<?> agregarIntegrante(@PathVariable Long idCliente, @PathVariable Long idReserva) {
        try {
            boolean resultado = service.agregarIntegrante(idCliente, idReserva);
            return ResponseEntity.ok(resultado);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/quitar/cliente/{idCliente}/reserva/{idReserva}")
    public ResponseEntity<?> quitarIntegrante(@PathVariable Long idCliente, @PathVariable Long idReserva) {
        try {
            service.quitarIntegrante(idCliente, idReserva);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}