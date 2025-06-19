package com.kartingrm.cliente_desc_frecu_service.controller;

import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import com.kartingrm.cliente_desc_frecu_service.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente-service/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;



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

    @GetMapping("/nombre/{nombre}/apellido/{apellido}")
    public ResponseEntity<List<Cliente>> getClientesByNombreApellido(@PathVariable String nombre, @PathVariable String apellido) {
        List<Cliente> clientes = clienteService.getClientesByNombreApellido(nombre, apellido);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id_cliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id_cliente) {
        Cliente cliente = clienteService.getClienteById(id_cliente);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/")
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        Cliente clienteCreated = clienteService.createCliente(cliente);
        return ResponseEntity.ok(clienteCreated);
    }

    @PutMapping("/{id_cliente}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id_cliente, @RequestBody Cliente cliente) {
        Cliente clienteUpdated = clienteService.updateCliente(id_cliente, cliente);
        return ResponseEntity.ok(clienteUpdated);
    }

    @DeleteMapping("/{id_cliente}")
    public ResponseEntity<Boolean> deleteCliente(@PathVariable Long id_cliente) {
        clienteService.deleteCliente(id_cliente);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activate/{id_cliente}")
    public ResponseEntity<Cliente> activateCliente(@PathVariable Long id_cliente) {
        Cliente cliente = clienteService.activateCliente(id_cliente);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/inactivate/{id_cliente}")
    public ResponseEntity<Cliente> inactivateCliente(@PathVariable Long id_cliente) {
        Cliente cliente = clienteService.inactivateCliente(id_cliente);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/nombre-completo/{id}")
    public ResponseEntity<String> getNombreCompletoById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getNombreCompletoClienteByid(id));
    }

}
