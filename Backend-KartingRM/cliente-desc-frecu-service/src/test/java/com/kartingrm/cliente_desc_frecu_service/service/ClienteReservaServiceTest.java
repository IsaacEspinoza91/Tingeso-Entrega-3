package com.kartingrm.cliente_desc_frecu_service.service;

import com.kartingrm.cliente_desc_frecu_service.dto.ClienteReservaDTO;
import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReserva;
import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReservaId;
import com.kartingrm.cliente_desc_frecu_service.repository.ClienteReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteReservaServiceTest {

    @Mock
    private ClienteReservaRepository clienteReservaRepository;

    @Mock
    private DescuentoClienteFrecuenteService descuentoClienteFrecuenteService;

    @InjectMocks
    private ClienteReservaService clienteReservaService;

    private ClienteReservaId clienteReservaId;
    private ClienteReserva clienteReserva;
    private ClienteReservaDTO clienteReservaDTO;

    @BeforeEach
    void setUp() {
        clienteReservaId = new ClienteReservaId(1L, 1L);
        clienteReserva = new ClienteReserva();
        clienteReserva.setId(clienteReservaId);
        clienteReserva.setEstado("completada");
        clienteReserva.setFecha(LocalDate.now());

        clienteReservaDTO = new ClienteReservaDTO();
        clienteReservaDTO.setId(clienteReservaId);
        clienteReservaDTO.setEstado("completada");
        clienteReservaDTO.setFecha(LocalDate.now());
    }

    @Test
    void getClientesReservas_ShouldReturnAllReservas() {
        // Arrange
        List<ClienteReserva> expectedReservas = Arrays.asList(clienteReserva);
        when(clienteReservaRepository.findAll()).thenReturn(expectedReservas);

        // Act
        List<ClienteReserva> result = clienteReservaService.getClientesReservas();

        // Assert
        assertEquals(expectedReservas, result);
        verify(clienteReservaRepository, times(1)).findAll();
    }

    @Test
    void getClienteReservaById_WhenExists_ShouldReturnReserva() {
        // Arrange
        when(clienteReservaRepository.findById(clienteReservaId)).thenReturn(Optional.of(clienteReserva));

        // Act
        ClienteReserva result = clienteReservaService.getClienteReservaById(clienteReservaId);

        // Assert
        assertEquals(clienteReserva, result);
        verify(clienteReservaRepository, times(1)).findById(clienteReservaId);
    }

    @Test
    void getClienteReservaById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(clienteReservaRepository.findById(clienteReservaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            clienteReservaService.getClienteReservaById(clienteReservaId);
        });
        verify(clienteReservaRepository, times(1)).findById(clienteReservaId);
    }

    @Test
    void obtenerReservasPorClienteId_ShouldReturnReservasForCliente() {
        // Arrange
        Long clienteId = 1L;
        List<ClienteReserva> expectedReservas = Arrays.asList(clienteReserva);
        when(clienteReservaRepository.findById_IdCliente(clienteId)).thenReturn(expectedReservas);

        // Act
        List<ClienteReserva> result = clienteReservaService.obtenerReservasPorClienteId(clienteId);

        // Assert
        assertEquals(expectedReservas, result);
        verify(clienteReservaRepository, times(1)).findById_IdCliente(clienteId);
    }

    @Test
    void createClienteReserva_WithValidData_ShouldSaveAndReturnReserva() {
        // Arrange
        when(clienteReservaRepository.existsById(clienteReservaId)).thenReturn(false);
        when(clienteReservaRepository.save(any(ClienteReserva.class))).thenReturn(clienteReserva);

        // Act
        ClienteReserva result = clienteReservaService.createClienteReserva(clienteReservaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteReservaId, result.getId());
        verify(clienteReservaRepository, times(1)).existsById(clienteReservaId);
        verify(clienteReservaRepository, times(1)).save(any(ClienteReserva.class));
    }

    @Test
    void createClienteReserva_WithNullReserva_ShouldThrowException() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            clienteReservaService.createClienteReserva(null);
        });
    }

    @Test
    void createClienteReserva_WithNullId_ShouldThrowException() {
        // Arrange
        clienteReservaDTO.setId(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            clienteReservaService.createClienteReserva(clienteReservaDTO);
        });
    }

    @Test
    void createClienteReserva_WithExistingId_ShouldThrowException() {
        // Arrange
        when(clienteReservaRepository.existsById(clienteReservaId)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            clienteReservaService.createClienteReserva(clienteReservaDTO);
        });
        verify(clienteReservaRepository, times(1)).existsById(clienteReservaId);
    }

    @Test
    void updateClienteReserva_WhenExists_ShouldUpdateAndReturnReserva() {
        // Arrange
        when(clienteReservaRepository.findById(clienteReservaId)).thenReturn(Optional.of(clienteReserva));
        when(clienteReservaRepository.save(clienteReserva)).thenReturn(clienteReserva);

        // Act
        ClienteReserva result = clienteReservaService.updateClienteReserva(clienteReservaId, clienteReservaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteReservaDTO.getEstado(), result.getEstado());
        assertEquals(clienteReservaDTO.getFecha(), result.getFecha());
        verify(clienteReservaRepository, times(1)).findById(clienteReservaId);
        verify(clienteReservaRepository, times(1)).save(clienteReserva);
    }

    @Test
    void updateClienteReserva_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(clienteReservaRepository.findById(clienteReservaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            clienteReservaService.updateClienteReserva(clienteReservaId, clienteReservaDTO);
        });
        verify(clienteReservaRepository, times(1)).findById(clienteReservaId);
    }

    @Test
    void deleteClienteReserva_WhenExists_ShouldReturnTrue() {
        // Arrange
        when(clienteReservaRepository.findById(clienteReservaId)).thenReturn(Optional.of(clienteReserva));

        // Act
        Boolean result = clienteReservaService.deleteClienteReserva(clienteReservaId);

        // Assert
        assertTrue(result);
        verify(clienteReservaRepository, times(1)).findById(clienteReservaId);
        verify(clienteReservaRepository, times(1)).deleteById(clienteReservaId);
    }

    @Test
    void deleteClienteReserva_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(clienteReservaRepository.findById(clienteReservaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            clienteReservaService.deleteClienteReserva(clienteReservaId);
        });
        verify(clienteReservaRepository, times(1)).findById(clienteReservaId);
    }

    @Test
    void getReservasCompletadasUltimoMes_ShouldReturnCount() {
        // Arrange
        Long clienteId = 1L;
        int expectedCount = 3;
        LocalDate fechaInicio = LocalDate.now().minusDays(30);
        when(clienteReservaRepository.countReservasCompletadasDespuesDeFecha(clienteId, fechaInicio))
                .thenReturn(expectedCount);

        // Act
        int result = clienteReservaService.getReservasCompletadasUltimoMes(clienteId);

        // Assert
        assertEquals(expectedCount, result);
        verify(clienteReservaRepository, times(1))
                .countReservasCompletadasDespuesDeFecha(clienteId, fechaInicio);
    }

    @Test
    void getDescuentoClienteFrecuenteSegunIdCliente_ShouldReturnDescuento() {
        // Arrange
        Long clienteId = 1L;
        int visitas = 3;
        double expectedDescuento = 0.1;

        when(clienteReservaRepository.countReservasCompletadasDespuesDeFecha(eq(clienteId), any(LocalDate.class)))
                .thenReturn(visitas);
        when(descuentoClienteFrecuenteService.getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(visitas))
                .thenReturn(expectedDescuento);

        // Act
        double result = clienteReservaService.getDescuentoClienteFrecuenteSegunIdCliente(clienteId);

        // Assert
        assertEquals(expectedDescuento, result);
        verify(clienteReservaRepository, times(1))
                .countReservasCompletadasDespuesDeFecha(eq(clienteId), any(LocalDate.class));
        verify(descuentoClienteFrecuenteService, times(1))
                .getPorcentajeDescuentoClienteFrecuenteByCantidadVisitas(visitas);
    }
}