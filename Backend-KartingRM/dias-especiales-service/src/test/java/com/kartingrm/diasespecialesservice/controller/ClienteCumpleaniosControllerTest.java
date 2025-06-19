package com.kartingrm.diasespecialesservice.controller;

import com.kartingrm.diasespecialesservice.dto.ClienteCumpleaniosDTO;
import com.kartingrm.diasespecialesservice.entity.ClienteCumpleanios;
import com.kartingrm.diasespecialesservice.service.ClienteCumpleaniosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteCumpleaniosControllerTest {

    @Mock
    private ClienteCumpleaniosService clienteCumpleaniosService;

    @InjectMocks
    private ClienteCumpleaniosController clienteCumpleaniosController;

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
        when(clienteCumpleaniosService.getClientesCumpleanios()).thenReturn(Arrays.asList(clienteCumpleanios));

        ResponseEntity<List<ClienteCumpleanios>> response = clienteCumpleaniosController.getClientesCumpleanios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(clienteCumpleanios, response.getBody().get(0));
    }

    @Test
    void getClienteCumpleaniosByIdCliente_ShouldReturnCliente() {
        when(clienteCumpleaniosService.getClienteCumpleaniosByIdCliente(1L)).thenReturn(clienteCumpleanios);

        ResponseEntity<ClienteCumpleanios> response = clienteCumpleaniosController.getClienteCumpleaniosByIdCliente(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteCumpleanios, response.getBody());
    }

    @Test
    void createClienteCumpleanios_ShouldReturnCreatedCliente() {
        when(clienteCumpleaniosService.createClienteCumpleanios(any(ClienteCumpleaniosDTO.class))).thenReturn(clienteCumpleanios);

        ResponseEntity<ClienteCumpleanios> response = clienteCumpleaniosController.createClienteCumpleanios(clienteCumpleaniosDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteCumpleanios, response.getBody());
    }

    @Test
    void updateClienteCumpleaniosByIdCliente_ShouldReturnUpdatedCliente() {
        when(clienteCumpleaniosService.updateClienteCumpleaniosByIdCliente(anyLong(), any(ClienteCumpleaniosDTO.class)))
                .thenReturn(clienteCumpleanios);

        ResponseEntity<ClienteCumpleanios> response = clienteCumpleaniosController
                .updateClienteCumpleaniosByIdCliente(1L, clienteCumpleaniosDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteCumpleanios, response.getBody());
    }

    @Test
    void deleteClienteCumpleaniosByIdCliente_ShouldReturnNoContent() {
        when(clienteCumpleaniosService.deleteClienteCumpleaniosByIdCliente(1L)).thenReturn(true);

        ResponseEntity<Void> response = clienteCumpleaniosController.deleteClienteCumpleaniosByIdCliente(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteCumpleaniosService, times(1)).deleteClienteCumpleaniosByIdCliente(1L);
    }

    @Test
    void verificarCumpleanios_WhenIsBirthday_ShouldReturnTrue() {
        when(clienteCumpleaniosService.estaDeCumpleanios(anyLong(), any(LocalDate.class))).thenReturn(true);
        LocalDate fecha = LocalDate.of(2023, 5, 15);

        ResponseEntity<Boolean> response = clienteCumpleaniosController.verificarCumpleanios(100L, fecha);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}