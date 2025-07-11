package com.kartingrm.cliente_desc_frecu_service.controller;

import com.kartingrm.cliente_desc_frecu_service.dto.ClienteReservaDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReserva;
import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReservaId;
import com.kartingrm.cliente_desc_frecu_service.service.ClienteReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente-service/cliente-reserva")
public class ClienteReservaController {

    private ClienteReservaService clienteReservaService;
    public ClienteReservaController(ClienteReservaService clienteReservaService) {
        this.clienteReservaService = clienteReservaService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ClienteReserva>> getAllClienteReserva() {
        List<ClienteReserva> clientesReservas = clienteReservaService.getClientesReservas();
        return ResponseEntity.ok(clientesReservas);
    }

    @GetMapping("/reserva/{idReserva}/cliente/{idCliente}")
    public ResponseEntity<ClienteReserva> getClienteReservaById(@PathVariable  Long idReserva, @PathVariable Long idCliente) {
        ClienteReservaId id = new ClienteReservaId(idReserva, idCliente);
        ClienteReserva clienteReserva = clienteReservaService.getClienteReservaById(id);
        return ResponseEntity.ok(clienteReserva);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ClienteReserva>> getAllClienteReservaByClienteId(@PathVariable Long idCliente) {
        List<ClienteReserva> clienteReservas = clienteReservaService.obtenerReservasPorClienteId(idCliente);
        return ResponseEntity.ok(clienteReservas);
    }

    @PostMapping("/")
    public ResponseEntity<ClienteReserva> createClienteReserva(@RequestBody ClienteReservaDTO clienteReserva) {
        ClienteReserva clienteReservaCreado = clienteReservaService.createClienteReserva(clienteReserva);
        return ResponseEntity.ok(clienteReservaCreado);
    }

    @PutMapping("/reserva/{idReserva}/cliente/{idCliente}")
    public ResponseEntity<ClienteReserva> updateClienteReserva(@PathVariable  Long idReserva, @PathVariable Long idCliente, @RequestBody ClienteReservaDTO clienteReserva) {
        ClienteReservaId id = new ClienteReservaId(idReserva, idCliente);
        ClienteReserva clienteReservaActualizado = clienteReservaService.updateClienteReserva(id, clienteReserva);
        return ResponseEntity.ok(clienteReservaActualizado);
    }

    @DeleteMapping("/reserva/{idReserva}/cliente/{idCliente}")
    public ResponseEntity<Boolean> deleteClienteReserva(@PathVariable  Long idReserva, @PathVariable Long idCliente) {
        ClienteReservaId id = new ClienteReservaId(idReserva, idCliente);
        clienteReservaService.deleteClienteReserva(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/porcen-desc-by-visitas-cliente/{idCliente}")
    public ResponseEntity<Double> getDescuentoClienteFrecuenteSegunIdCliente(@PathVariable Long idCliente) {
        double porcentaje = clienteReservaService.getDescuentoClienteFrecuenteSegunIdCliente(idCliente);
        return ResponseEntity.ok(porcentaje);
    }

}
