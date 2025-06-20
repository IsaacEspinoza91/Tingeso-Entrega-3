package com.kartingrm.rack_semanal_service.controller;

import com.kartingrm.rack_semanal_service.dto.ReservaSemanalResponse;
import com.kartingrm.rack_semanal_service.service.RackSemanalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RackSemanalControllerTest {

    @Mock
    private RackSemanalService rackSemanalService;

    @InjectMocks
    private RackSemanalController rackSemanalController;

    private ReservaSemanalResponse response;

    @BeforeEach
    void setUp() {
        response = new ReservaSemanalResponse();
        response.setFechaInicioSemana("2023-01-01");
        response.setFechaFinSemana("2023-01-07");
    }

    @Test
    void obtenerRackSemanal_ShouldReturnWeeklyReservations() {
        when(rackSemanalService.obtenerReservasSemana(0)).thenReturn(response);

        ReservaSemanalResponse result = rackSemanalController.obtenerRackSemanal(0);

        assertNotNull(result);
        assertEquals("2023-01-01", result.getFechaInicioSemana());
        assertEquals("2023-01-07", result.getFechaFinSemana());
    }

    @Test
    void obtenerRackSemanal_WithDifferentWeekOffset_ShouldCallServiceWithCorrectOffset() {
        when(rackSemanalService.obtenerReservasSemana(2)).thenReturn(response);

        rackSemanalController.obtenerRackSemanal(2);

        verify(rackSemanalService, times(1)).obtenerReservasSemana(2);
    }
}