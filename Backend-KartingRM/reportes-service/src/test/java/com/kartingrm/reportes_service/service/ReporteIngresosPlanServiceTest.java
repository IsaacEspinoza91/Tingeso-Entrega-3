package com.kartingrm.reportes_service.service;

import com.kartingrm.reportes_service.dto.ReporteIngresosPorPlanDTO;
import com.kartingrm.reportes_service.entity.ReporteIngresosPlan;
import com.kartingrm.reportes_service.repository.ReporteIngresosPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteIngresosPlanServiceTest {

    @Mock
    private ReporteIngresosPlanRepository reportesPlanRepository;

    @InjectMocks
    private ReporteIngresosPlanService reporteService;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ReporteIngresosPlan> reportes;

    @BeforeEach
    void setUp() {
        fechaInicio = LocalDate.of(2023, 1, 1);
        fechaFin = LocalDate.of(2023, 3, 31);

        reportes = List.of(
                new ReporteIngresosPlan(1L, "20 vueltas", LocalDate.of(2023, 1, 1), 1000.0),
                new ReporteIngresosPlan(1L, "20 vueltas", LocalDate.of(2023, 2, 1), 1500.0),
                new ReporteIngresosPlan(2L, "30 vueltas", LocalDate.of(2023, 1, 1), 2000.0),
                new ReporteIngresosPlan(2L, "30 vueltas", LocalDate.of(2023, 3, 1), 2500.0)
        );
    }

    @Test
    void generarReporteIngresosPorPlan_ShouldReturnValidReport() {
        when(reportesPlanRepository.findReportesPlanEntreMeses(fechaInicio, fechaFin))
                .thenReturn(reportes);

        List<ReporteIngresosPorPlanDTO> result = reporteService.generarReporteIngresosPorPlan(
                1, 2023, 3, 2023);

        assertEquals(3, result.size()); // 2 planes + total general
        assertEquals("30 vueltas", result.get(0).getDescripcionPlan()); // Ordenado por vueltas descendente
        assertEquals("20 vueltas", result.get(1).getDescripcionPlan());
        assertEquals("TOTAL GENERAL", result.get(2).getDescripcionPlan());

        // Verificar totales
        assertEquals(2500.0, result.get(0).getIngresosPorMes().get("marzo-2023"));
        assertEquals(7000.0, result.get(2).getTotal());
    }

    @Test
    void generarReporteIngresosPorPlan_WithInvalidDates_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                reporteService.generarReporteIngresosPorPlan(3, 2023, 1, 2023));
    }

    @Test
    void actualizarIngresosPlan_WhenSuma_ShouldAddOrCreateRecord() {
        LocalDate fechaReserva = LocalDate.of(2023, 5, 15);
        Double monto = 500.0;

        when(reportesPlanRepository.findByIdPlanAndMes(1L, fechaReserva.withDayOfMonth(1)))
                .thenReturn(Optional.empty());

        reporteService.actualizarIngresosPlan(1L, "20 vueltas", fechaReserva, monto, true);

        verify(reportesPlanRepository, times(1)).save(any(ReporteIngresosPlan.class));
    }

    @Test
    void actualizarIngresosPlan_WhenResta_ShouldSubtractIfExists() {
        LocalDate fechaReserva = LocalDate.of(2023, 5, 15);
        Double monto = 500.0;
        ReporteIngresosPlan reporteExistente = new ReporteIngresosPlan(1L, "20 vueltas",
                fechaReserva.withDayOfMonth(1), 1000.0);

        when(reportesPlanRepository.findByIdPlanAndMes(1L, fechaReserva.withDayOfMonth(1)))
                .thenReturn(Optional.of(reporteExistente));

        reporteService.actualizarIngresosPlan(1L, "20 vueltas", fechaReserva, monto, false);

        verify(reportesPlanRepository, times(1)).restarIngresos(1L, fechaReserva.withDayOfMonth(1), monto);
    }

    @Test
    void extraerVueltas_ShouldReturnCorrectNumber() {
        assertEquals(20, reporteService.extraerVueltas("20 vueltas o max 20 min"));
        assertEquals(0, reporteService.extraerVueltas("Plan especial"));
    }
}