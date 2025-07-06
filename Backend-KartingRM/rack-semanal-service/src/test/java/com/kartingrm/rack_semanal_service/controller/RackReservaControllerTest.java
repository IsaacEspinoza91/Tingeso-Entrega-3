package com.kartingrm.rack_semanal_service.controller;

import com.kartingrm.rack_semanal_service.dto.RackReservaDTO;
import com.kartingrm.rack_semanal_service.entity.RackReserva;
import com.kartingrm.rack_semanal_service.service.RackReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RackReservaControllerTest {

    @Mock
    private RackReservaService rackReservaService;

    @InjectMocks
    private RackReservaController rackReservaController;

    private RackReserva rackReserva;
    private RackReservaDTO rackReservaDTO;

    @BeforeEach
    void setUp() {
        rackReserva = new RackReserva();
        rackReserva.setIdReserva(1L);
        rackReserva.setFecha(LocalDate.now());
        rackReserva.setHoraInicio(LocalTime.of(10, 0));
        rackReserva.setHoraFin(LocalTime.of(11, 0));
        rackReserva.setNombreReservante("John Doe");

        rackReservaDTO = new RackReservaDTO();
        rackReservaDTO.setFecha(LocalDate.now());
        rackReservaDTO.setHoraInicio(LocalTime.of(10, 0));
        rackReservaDTO.setHoraFin(LocalTime.of(11, 0));
        rackReservaDTO.setNombreReservante("John Doe");
    }

    @Test
    void getRackReservas_ShouldReturnAllReservas() {
        when(rackReservaService.getRackReservas()).thenReturn(List.of(rackReserva));

        ResponseEntity<List<RackReserva>> response = rackReservaController.getRackReservas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getRackReservaById_ShouldReturnReserva() {
        when(rackReservaService.getRackReservaById(1L)).thenReturn(rackReserva);

        ResponseEntity<RackReserva> response = rackReservaController.getRackReservaById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rackReserva, response.getBody());
    }

    @Test
    void updateRackReserva_ShouldReturnUpdatedReserva() {
        when(rackReservaService.updateRackReserva(anyLong(), any(RackReservaDTO.class))).thenReturn(rackReserva);

        ResponseEntity<RackReserva> response = rackReservaController.updateRackReserva(1L, rackReservaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rackReserva, response.getBody());
    }

    @Test
    void deleteRackReserva_ShouldReturnNoContent() {
        when(rackReservaService.deleteRackReservaById(1L)).thenReturn(true);

        ResponseEntity<Void> response = rackReservaController.deleteRackReserva(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}