package com.kartingrm.diasespecialesservice.service;

import com.kartingrm.diasespecialesservice.dto.ClienteCumpleaniosDTO;
import com.kartingrm.diasespecialesservice.entity.ClienteCumpleanios;
import com.kartingrm.diasespecialesservice.repository.ClienteCumpleaniosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteCumpleaniosServiceTest {

    @Mock
    private ClienteCumpleaniosRepository clienteCumpleaniosRepository;

    @InjectMocks
    private ClienteCumpleaniosService clienteCumpleaniosService;

    private ClienteCumpleanios clienteCumpleanios;
    private ClienteCumpleaniosDTO clienteCumpleaniosDTO;

    @BeforeEach
    void setUp() {
        clienteCumpleanios = new ClienteCumpleanios();
        clienteCumpleanios.setIdCliente(100L);
        clienteCumpleanios.setFecha(LocalDate.of(1990, 5, 15));

        clienteCumpleaniosDTO = new ClienteCumpleaniosDTO();
        clienteCumpleaniosDTO.setIdCliente(100L);
        clienteCumpleaniosDTO.setFecha(LocalDate.of(1990, 5, 15));
    }

    @Test
    void getClientesCumpleanios_ShouldReturnAllClientes() {
        // Arrange
        when(clienteCumpleaniosRepository.findAll()).thenReturn(List.of(clienteCumpleanios));

        // Act
        List<ClienteCumpleanios> result = clienteCumpleaniosService.getClientesCumpleanios();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clienteCumpleanios, result.get(0));
        verify(clienteCumpleaniosRepository, times(1)).findAll();
    }

    @Test
    void getClienteCumpleaniosByIdCliente_WhenExists_ShouldReturnCliente() {
        // Arrange
        when(clienteCumpleaniosRepository.findById(1L)).thenReturn(Optional.of(clienteCumpleanios));

        // Act
        ClienteCumpleanios result = clienteCumpleaniosService.getClienteCumpleaniosByIdCliente(1L);

        // Assert
        assertNotNull(result);
        assertEquals(clienteCumpleanios, result);
        verify(clienteCumpleaniosRepository, times(1)).findById(1L);
    }

    @Test
    void getClienteCumpleaniosByIdCliente_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(clienteCumpleaniosRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteCumpleaniosService.getClienteCumpleaniosByIdCliente(1L));
        verify(clienteCumpleaniosRepository, times(1)).findById(1L);
    }

    @Test
    void createClienteCumpleanios_WithValidDTO_ShouldReturnCreatedCliente() {
        // Arrange
        when(clienteCumpleaniosRepository.save(any(ClienteCumpleanios.class))).thenReturn(clienteCumpleanios);

        // Act
        ClienteCumpleanios result = clienteCumpleaniosService.createClienteCumpleanios(clienteCumpleaniosDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteCumpleaniosDTO.getIdCliente(), result.getIdCliente());
        assertEquals(clienteCumpleaniosDTO.getFecha(), result.getFecha());
        verify(clienteCumpleaniosRepository, times(1)).save(any(ClienteCumpleanios.class));
    }

    @Test
    void createClienteCumpleanios_WithNullDTO_ShouldThrowException() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteCumpleaniosService.createClienteCumpleanios(null));
        verify(clienteCumpleaniosRepository, never()).save(any(ClienteCumpleanios.class));
    }

    @Test
    void updateClienteCumpleaniosByIdCliente_WhenExists_ShouldReturnUpdatedCliente() {
        // Arrange
        when(clienteCumpleaniosRepository.findById(1L)).thenReturn(Optional.of(clienteCumpleanios));
        when(clienteCumpleaniosRepository.save(any(ClienteCumpleanios.class))).thenReturn(clienteCumpleanios);

        // Act
        ClienteCumpleanios result = clienteCumpleaniosService.updateClienteCumpleaniosByIdCliente(1L, clienteCumpleaniosDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteCumpleaniosDTO.getIdCliente(), result.getIdCliente());
        assertEquals(clienteCumpleaniosDTO.getFecha(), result.getFecha());
        verify(clienteCumpleaniosRepository, times(1)).findById(1L);
        verify(clienteCumpleaniosRepository, times(1)).save(any(ClienteCumpleanios.class));
    }

    @Test
    void updateClienteCumpleaniosByIdCliente_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(clienteCumpleaniosRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteCumpleaniosService.updateClienteCumpleaniosByIdCliente(1L, clienteCumpleaniosDTO));
        verify(clienteCumpleaniosRepository, times(1)).findById(1L);
        verify(clienteCumpleaniosRepository, never()).save(any(ClienteCumpleanios.class));
    }

    @Test
    void deleteClienteCumpleaniosByIdCliente_WhenExists_ShouldReturnTrue() {
        // Arrange
        when(clienteCumpleaniosRepository.findById(1L)).thenReturn(Optional.of(clienteCumpleanios));
        doNothing().when(clienteCumpleaniosRepository).deleteById(1L);

        // Act
        Boolean result = clienteCumpleaniosService.deleteClienteCumpleaniosByIdCliente(1L);

        // Assert
        assertTrue(result);
        verify(clienteCumpleaniosRepository, times(1)).findById(1L);
        verify(clienteCumpleaniosRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteClienteCumpleaniosByIdCliente_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(clienteCumpleaniosRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteCumpleaniosService.deleteClienteCumpleaniosByIdCliente(1L));
        verify(clienteCumpleaniosRepository, times(1)).findById(1L);
        verify(clienteCumpleaniosRepository, never()).deleteById(1L);
    }

    @Test
    void estaDeCumpleanios_WhenIsBirthday_ShouldReturnTrue() {
        // Arrange
        Long idCliente = 100L;
        LocalDate fecha = LocalDate.of(2023, 5, 15); // Mismo mes y día que la fecha de cumpleaños
        when(clienteCumpleaniosRepository.estaDeCumpleanios(idCliente, fecha)).thenReturn(true);

        // Act
        boolean result = clienteCumpleaniosService.estaDeCumpleanios(idCliente, fecha);

        // Assert
        assertTrue(result);
        verify(clienteCumpleaniosRepository, times(1)).estaDeCumpleanios(idCliente, fecha);
    }

    @Test
    void estaDeCumpleanios_WhenIsNotBirthday_ShouldReturnFalse() {
        // Arrange
        Long idCliente = 100L;
        LocalDate fecha = LocalDate.of(2023, 6, 15); // Diferente mes
        when(clienteCumpleaniosRepository.estaDeCumpleanios(idCliente, fecha)).thenReturn(false);

        // Act
        boolean result = clienteCumpleaniosService.estaDeCumpleanios(idCliente, fecha);

        // Assert
        assertFalse(result);
        verify(clienteCumpleaniosRepository, times(1)).estaDeCumpleanios(idCliente, fecha);
    }
}