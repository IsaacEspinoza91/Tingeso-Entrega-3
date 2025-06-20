package com.kartingrm.rack_semanal_service.service;

import com.kartingrm.rack_semanal_service.dto.ReservaInfo;
import com.kartingrm.rack_semanal_service.dto.ReservaSemanalResponse;
import com.kartingrm.rack_semanal_service.entity.RackReserva;
import com.kartingrm.rack_semanal_service.repository.RackReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RackSemanalServiceTest {

    @Mock
    private RackReservaRepository reservaRepository;

    @InjectMocks
    private RackSemanalService rackSemanalService;

    private RackReserva rackReserva;
    private LocalDate today;
    private LocalDate monday;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
        monday = today.with(DayOfWeek.MONDAY);

        rackReserva = new RackReserva();
        rackReserva.setIdReserva(1L);
        rackReserva.setFecha(monday);
        rackReserva.setHoraInicio(LocalTime.of(10, 0));
        rackReserva.setHoraFin(LocalTime.of(11, 0));
        rackReserva.setNombreReservante("John Doe");
    }

    @Test
    void obtenerReservasSemana_WithZeroOffset_ShouldReturnCurrentWeek() {
        when(reservaRepository.findByFechaBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(rackReserva));

        ReservaSemanalResponse response = rackSemanalService.obtenerReservasSemana(0);

        assertEquals(monday.toString(), response.getFechaInicioSemana());
        assertEquals(monday.plusDays(6).toString(), response.getFechaFinSemana());
        assertNotNull(response.getReservasPorDia().getLunes());
        assertEquals(1, response.getReservasPorDia().getLunes().size());
    }

    @Test
    void obtenerReservasSemana_WithPositiveOffset_ShouldReturnFutureWeek() {
        when(reservaRepository.findByFechaBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        ReservaSemanalResponse response = rackSemanalService.obtenerReservasSemana(1);

        assertEquals(monday.plusWeeks(1).toString(), response.getFechaInicioSemana());
        assertEquals(monday.plusWeeks(1).plusDays(6).toString(), response.getFechaFinSemana());
    }

    @Test
    void obtenerReservasSemana_WithNegativeOffset_ShouldReturnPastWeek() {
        when(reservaRepository.findByFechaBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        ReservaSemanalResponse response = rackSemanalService.obtenerReservasSemana(-1);

        assertEquals(monday.minusWeeks(1).toString(), response.getFechaInicioSemana());
        assertEquals(monday.minusWeeks(1).plusDays(6).toString(), response.getFechaFinSemana());
    }

    @Test
    void convertirReservas_WithNullInput_ShouldReturnEmptyList() {
        List<ReservaInfo> result = rackSemanalService.convertirReservas(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void convertirReservas_WithValidReservas_ShouldReturnFormattedInfo() {
        List<ReservaInfo> result = rackSemanalService.convertirReservas(List.of(rackReserva));

        assertEquals(1, result.size());
        assertEquals("10:00:00", result.get(0).getHoraInicio());
        assertEquals("11:00:00", result.get(0).getHoraFin());
        assertEquals("John Doe", result.get(0).getNombreReservante());
    }
}