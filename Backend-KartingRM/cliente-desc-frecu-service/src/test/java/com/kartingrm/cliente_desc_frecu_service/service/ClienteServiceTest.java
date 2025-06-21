package com.kartingrm.cliente_desc_frecu_service.service;

import com.kartingrm.cliente_desc_frecu_service.dto.ClienteCumpleaniosRequest;
import com.kartingrm.cliente_desc_frecu_service.dto.ClienteDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.Cliente;
import com.kartingrm.cliente_desc_frecu_service.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setRut("12345678-9");
        cliente.setCorreo("juan@example.com");
        cliente.setTelefono("+56912345678");
        cliente.setActivo(true);
        cliente.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan");
        clienteDTO.setApellido("Perez");
        clienteDTO.setRut("12345678-9");
        clienteDTO.setCorreo("juan@example.com");
        clienteDTO.setTelefono("+56912345678");
        clienteDTO.setActivo(true);
        clienteDTO.setFechaNacimiento(LocalDate.of(1990, 5, 15));
    }

    @Test
    void getClientes_ShouldReturnAllClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> result = clienteService.getClientes();

        assertEquals(1, result.size());
        assertEquals(cliente, result.get(0));
    }

    @Test
    void getClientesActivos_ShouldReturnActiveClientes() {
        when(clienteRepository.findClientesByActivo(true)).thenReturn(List.of(cliente));

        List<Cliente> result = clienteService.getClientesActivos();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActivo());
    }

    @Test
    void getClientesInactivos_ShouldReturnInactiveClientes() {
        cliente.setActivo(false);
        when(clienteRepository.findClientesByActivo(false)).thenReturn(List.of(cliente));

        List<Cliente> result = clienteService.getClientesInactivos();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isActivo());
    }

    @Test
    void getClienteById_WhenExists_ShouldReturnCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.getClienteById(1L);

        assertEquals(cliente, result);
    }

    @Test
    void getClienteById_WhenNotExists_ShouldThrowException() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.getClienteById(1L));
    }

    @Test
    void getClienteByRut_WhenExists_ShouldReturnCliente() {
        when(clienteRepository.findClienteByRut("12345678-9")).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.getClienteByRut("12345678-9");

        assertEquals(cliente, result);
    }

    @Test
    void getClienteByRut_WhenNotExists_ShouldThrowException() {
        when(clienteRepository.findClienteByRut("12345678-9")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteService.getClienteByRut("12345678-9"));
    }

    @Test
    void getClientesByNombreApellido_WhenExists_ShouldReturnClientes() {
        when(clienteRepository.findClienteByNombreAndApellido("Juan", "Perez"))
                .thenReturn(Optional.of(List.of(cliente)));

        List<Cliente> result = clienteService.getClientesByNombreApellido("Juan", "Perez");

        assertEquals(1, result.size());
        assertEquals(cliente, result.get(0));
    }

    @Test
    void createCliente_WithValidDTO_ShouldReturnCreatedCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(ClienteCumpleaniosRequest.class)))
                .thenReturn(new ClienteCumpleaniosRequest(1L, LocalDate.now()));

        Cliente result = clienteService.createCliente(clienteDTO);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(restTemplate, times(1)).postForObject(anyString(), any(HttpEntity.class), eq(ClienteCumpleaniosRequest.class));
    }

    @Test
    void createCliente_WithNullDTO_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> clienteService.createCliente(null));
    }

    @Test
    void updateCliente_WhenExists_ShouldReturnUpdatedCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.updateCliente(1L, clienteDTO);

        assertEquals(cliente, result);
        verify(restTemplate, never()).put(anyString(), any(HttpEntity.class), eq(ClienteCumpleaniosRequest.class));
    }

    @Test
    void updateCliente_WithChangedBirthDate_ShouldUpdateCumpleanios() {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setFechaNacimiento(LocalDate.of(1980, 1, 1));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        clienteService.updateCliente(1L, clienteDTO);

        verify(restTemplate, times(1)).put(anyString(), any(HttpEntity.class), eq(ClienteCumpleaniosRequest.class));
    }

    @Test
    void deleteCliente_WhenExists_ShouldReturnTrue() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).deleteById(1L);

        Boolean result = clienteService.deleteCliente(1L);

        assertTrue(result);
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void inactivateCliente_ShouldSetActivoFalse() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.inactivateCliente(1L);

        assertFalse(result.isActivo());
    }

    @Test
    void activateCliente_ShouldSetActivoTrue() {
        cliente.setActivo(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.activateCliente(1L);

        assertTrue(result.isActivo());
    }

    @Test
    void getNombreCompletoClienteById_ShouldReturnFullName() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        String result = clienteService.getNombreCompletoClienteByid(1L);

        assertEquals("Juan Perez", result);
    }
}