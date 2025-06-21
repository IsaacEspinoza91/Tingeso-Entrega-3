package com.kartingrm.cliente_desc_frecu_service.controller;

import com.kartingrm.cliente_desc_frecu_service.dto.ClienteDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import com.kartingrm.cliente_desc_frecu_service.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setRut("12345678-9");
        cliente.setActivo(true);

        clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan");
        clienteDTO.setApellido("Perez");
        clienteDTO.setRut("12345678-9");
        clienteDTO.setActivo(true);
    }

    @Test
    void getClientes_ShouldReturnAllClientes() {
        when(clienteService.getClientes()).thenReturn(List.of(cliente));

        ResponseEntity<List<Cliente>> response = clienteController.getClientes();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(cliente, response.getBody().get(0));
    }

    @Test
    void getClientesActivos_ShouldReturnActiveClientes() {
        when(clienteService.getClientesActivos()).thenReturn(List.of(cliente));

        ResponseEntity<List<Cliente>> response = clienteController.getClientesActivos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).isActivo());
    }

    @Test
    void getClientesInactivas_ShouldReturnInactiveClientes() {
        cliente.setActivo(false);
        when(clienteService.getClientesInactivos()).thenReturn(List.of(cliente));

        ResponseEntity<List<Cliente>> response = clienteController.getClientesInactivas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertFalse(response.getBody().get(0).isActivo());
    }

    @Test
    void getClienteByRut_ShouldReturnCliente() {
        when(clienteService.getClienteByRut("12345678-9")).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.getClienteByRut("12345678-9");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void getClientesByNombreApellido_ShouldReturnClientes() {
        when(clienteService.getClientesByNombreApellido("Juan", "Perez"))
                .thenReturn(List.of(cliente));

        ResponseEntity<List<Cliente>> response = clienteController.getClientesByNombreApellido("Juan", "Perez");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(cliente, response.getBody().get(0));
    }

    @Test
    void getClienteById_ShouldReturnCliente() {
        when(clienteService.getClienteById(1L)).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.getClienteById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void createCliente_ShouldReturnCreatedCliente() {
        when(clienteService.createCliente(clienteDTO)).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.createCliente(clienteDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void updateCliente_ShouldReturnUpdatedCliente() {
        when(clienteService.updateCliente(1L, clienteDTO)).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.updateCliente(1L, clienteDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void deleteCliente_ShouldReturnTrueWhenSuccess() {
        when(clienteService.deleteCliente(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = clienteController.deleteCliente(1L);

        assertEquals(200, response.getStatusCodeValue()); // Verifica status code 200
        assertTrue(response.getBody()); // Verifica que el body sea true
        verify(clienteService, times(1)).deleteCliente(1L); // Verifica que se llamó al servicio
    }

    @Test
    void activateCliente_ShouldReturnActivatedCliente() {
        when(clienteService.activateCliente(1L)).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.activateCliente(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
        assertTrue(response.getBody().isActivo());
    }

    @Test
    void inactivateCliente_ShouldReturnInactivatedCliente() {
        cliente.setActivo(false);
        when(clienteService.inactivateCliente(1L)).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.inactivateCliente(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cliente, response.getBody());
        assertFalse(response.getBody().isActivo());
    }

    @Test
    void getNombreCompletoById_ShouldReturnFullName() {
        when(clienteService.getNombreCompletoClienteByid(1L)).thenReturn("Juan Perez");

        ResponseEntity<String> response = clienteController.getNombreCompletoById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Juan Perez", response.getBody());
    }
}