package com.kartingrm.reportes_service.service;

import com.kartingrm.reportes_service.dto.ReporteIngresosPorPersonasDTO;
import com.kartingrm.reportes_service.entity.ReporteIngresosPersonas;
import com.kartingrm.reportes_service.repository.ReporteIngresosPersonasRepository;
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
class ReporteIngresosPersonasServiceTest {

    @Mock
    private ReporteIngresosPersonasRepository reportesPersonasRepository;

    @InjectMocks
    private ReporteIngresosPersonasService reporteService;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ReporteIngresosPersonas> reportes;

    @BeforeEach
    void setUp() {
        fechaInicio = LocalDate.of(2023, 1, 1);
        fechaFin = LocalDate.of(2023, 3, 31);

        reportes = List.of(
                new ReporteIngresosPersonas("1-2 personas", LocalDate.of(2023, 1, 1), 1000.0),
                new ReporteIngresosPersonas("1-2 personas", LocalDate.of(2023, 2, 1), 1500.0),
                new ReporteIngresosPersonas("3-5 personas", LocalDate.of(2023, 1, 1), 2000.0),
                new ReporteIngresosPersonas("6-10 personas", LocalDate.of(2023, 3, 1), 2500.0)
        );
    }

    @Test
    void generarReporteIngresosPorPersonas_ShouldReturnValidReport() {
        when(reportesPersonasRepository.findReportesPersonasEntreMeses(fechaInicio, fechaFin))
                .thenReturn(reportes);

        List<ReporteIngresosPorPersonasDTO> result = reporteService.generarReporteIngresosPorPersonas(
                1, 2023, 3, 2023);

        assertEquals(5, result.size()); // 4 rangos + total general
        assertEquals("1-2 personas", result.get(0).getRangoPersonas());
        assertEquals("3-5 personas", result.get(1).getRangoPersonas());
        assertEquals("6-10 personas", result.get(2).getRangoPersonas());
        assertEquals("11-15 personas", result.get(3).getRangoPersonas()); // Debe aparecer aunque no tenga datos
        assertEquals("TOTAL GENERAL", result.get(4).getRangoPersonas());

        // Verificar totales
        assertEquals(2500.0, result.get(2).getIngresosPorMes().get("marzo-2023"));
        assertEquals(7000.0, result.get(4).getTotal());
    }

    @Test
    void generarReporteIngresosPorPersonas_WithInvalidDates_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                reporteService.generarReporteIngresosPorPersonas(3, 2023, 1, 2023));
    }

    @Test
    void actualizarIngresos_WhenSuma_ShouldAddOrCreateRecord() {
        LocalDate fechaReserva = LocalDate.of(2023, 5, 15);
        Double monto = 500.0;

        when(reportesPersonasRepository.findByRangoPersonasAndMes("1-2 personas", fechaReserva.withDayOfMonth(1)))
                .thenReturn(Optional.empty());

        reporteService.actualizarIngresos("1-2 personas", fechaReserva, monto, true);

        verify(reportesPersonasRepository, times(1)).save(any(ReporteIngresosPersonas.class));
    }

    @Test
    void actualizarIngresos_WhenResta_ShouldSubtractIfExists() {
        LocalDate fechaReserva = LocalDate.of(2023, 5, 15);
        Double monto = 500.0;
        ReporteIngresosPersonas reporteExistente = new ReporteIngresosPersonas(
                "1-2 personas", fechaReserva.withDayOfMonth(1), 1000.0);

        when(reportesPersonasRepository.findByRangoPersonasAndMes("1-2 personas", fechaReserva.withDayOfMonth(1)))
                .thenReturn(Optional.of(reporteExistente));

        reporteService.actualizarIngresos("1-2 personas", fechaReserva, monto, false);

        verify(reportesPersonasRepository, times(1))
                .restarIngresos("1-2 personas", fechaReserva.withDayOfMonth(1), monto);
    }
}