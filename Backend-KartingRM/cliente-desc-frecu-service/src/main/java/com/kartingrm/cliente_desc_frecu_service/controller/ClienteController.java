package com.kartingrm.cliente_desc_frecu_service.controller;

import com.kartingrm.cliente_desc_frecu_service.dto.ClienteDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import com.kartingrm.cliente_desc_frecu_service.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente-service/cliente")
public class ClienteController {

    private ClienteService clienteService;
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Cliente>> getClientes() {
        List<Cliente> clientes = clienteService.getClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> getClientesActivos() {
        List<Cliente> clientes = clienteService.getClientesActivos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Cliente>> getClientesInactivas() {
        List<Cliente> clientes = clienteService.getClientesInactivos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<Cliente> getClienteByRut(@PathVariable String rut) {
        Cliente cliente = clienteService.getClienteByRut(rut);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/busqueda-rut/{rut}")
    public ResponseEntity<List<Cliente>> getClienteByRutBusqueda(@PathVariable String rut) {
        List<Cliente> clientes = clienteService.getClientesByRut(rut);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/nombre/{nombre}/apellido/{apellido}")
    public ResponseEntity<List<Cliente>> getClientesByNombreApellido(@PathVariable String nombre, @PathVariable String apellido) {
        List<Cliente> clientes = clienteService.getClientesByNombreApellido(nombre, apellido);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/busqueda-nombre/{nombre}")
    public ResponseEntity<List<Cliente>> getClientesByBusquedaParcialNombre(@PathVariable String nombre) {
        List<Cliente> clientes = clienteService.getClientesByBusquedaParcialNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long idCliente) {
        Cliente cliente = clienteService.getClienteById(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/")
    public ResponseEntity<Cliente> createCliente(@RequestBody ClienteDTO cliente) {
        Cliente clienteCreated = clienteService.createCliente(cliente);
        return ResponseEntity.ok(clienteCreated);
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long idCliente, @RequestBody ClienteDTO cliente) {
        Cliente clienteUpdated = clienteService.updateCliente(idCliente, cliente);
        return ResponseEntity.ok(clienteUpdated);
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Boolean> deleteCliente(@PathVariable Long idCliente) {
        boolean result = clienteService.deleteCliente(idCliente);
        return ResponseEntity.ok(result); // Retorna 200 OK con el booleano
    }

    @PutMapping("/activate/{idCliente}")
    public ResponseEntity<Cliente> activateCliente(@PathVariable Long idCliente) {
        Cliente cliente = clienteService.activateCliente(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/inactivate/{idCliente}")
    public ResponseEntity<Cliente> inactivateCliente(@PathVariable Long idCliente) {
        Cliente cliente = clienteService.inactivateCliente(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/nombre-completo/{id}")
    public ResponseEntity<String> getNombreCompletoById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getNombreCompletoClienteByid(id));
    }

}
