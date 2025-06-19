package com.kartingrm.dias_especiales_service.controller;

import com.kartingrm.dias_especiales_service.entity.ClienteCumpleanios;
import com.kartingrm.dias_especiales_service.service.ClienteCumpleaniosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dias-especiales-service/cliente-cumpleanios")
public class ClienteCumpleaniosController {

    @Autowired
    private ClienteCumpleaniosService clienteCumpleaniosService;

    @GetMapping("/")
    public ResponseEntity<List<ClienteCumpleanios>> getClientesCumpleanios() {
        List<ClienteCumpleanios> clientesCumpleanios = clienteCumpleaniosService.getClientesCumpleanios();
        return ResponseEntity.ok(clientesCumpleanios);
    }

    @GetMapping("{id}")
    public ResponseEntity<ClienteCumpleanios> getClienteCumpleaniosByIdCliente(@PathVariable Long id) {
        ClienteCumpleanios clienteCumpleanios = clienteCumpleaniosService.getClienteCumpleaniosByIdCliente(id);
        return ResponseEntity.ok(clienteCumpleanios);
    }

    @PostMapping("/")
    public ResponseEntity<ClienteCumpleanios> createClienteCumpleanios(@RequestBody ClienteCumpleanios clienteCumpleanios) {
        ClienteCumpleanios clienteCumpleaniosNuevo = clienteCumpleaniosService.createClienteCumpleanios(clienteCumpleanios);
        return ResponseEntity.ok(clienteCumpleaniosNuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteCumpleanios> updateClienteCumpleaniosByIdCliente(@PathVariable Long id, @RequestBody ClienteCumpleanios clienteCumpleanios) {
        ClienteCumpleanios clienteCumpleaniosActualizado = clienteCumpleaniosService.updateClienteCumpleaniosByIdCliente(id, clienteCumpleanios);
        return ResponseEntity.ok(clienteCumpleaniosActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClienteCumpleaniosByIdCliente(@PathVariable Long id) {
        clienteCumpleaniosService.deleteClienteCumpleaniosByIdCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{idCliente}/esCumpleaniero")
    public ResponseEntity<Boolean> verificarCumpleanios(
            @PathVariable Long idCliente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        boolean esCumpleanios = clienteCumpleaniosService.estaDeCumpleanios(idCliente, fecha);
        return ResponseEntity.ok(esCumpleanios);
    }
}