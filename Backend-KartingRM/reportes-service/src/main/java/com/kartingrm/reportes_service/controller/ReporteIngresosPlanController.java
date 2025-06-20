package com.kartingrm.reportes_service.controller;

import com.kartingrm.reportes_service.dto.ReporteIngresosPorPlanDTO;
import com.kartingrm.reportes_service.service.ReporteIngresosPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes-service/segun-plan")
public class ReporteIngresosPlanController {

    private ReporteIngresosPlanService reporteService;
    public ReporteIngresosPlanController(ReporteIngresosPlanService reporteService) {
        this.reporteService = reporteService;
    }

    // Metodo para actuazliar registros de reportes. Si esSuma es true se crea o suma el monto al registro del mes.
    // si esSuma es false, se resta el valor al registro del mes
    @PostMapping("/actualizar")
    public ResponseEntity<Void> actualizarIngresosPlan(
            @RequestParam Long idPlan,
            @RequestParam String descripcionPlan,
            @RequestParam String fechaReserva,
            @RequestParam Double monto,
            @RequestParam boolean esSuma) {

        LocalDate fecha = LocalDate.parse(fechaReserva);
        reporteService.actualizarIngresosPlan(idPlan, descripcionPlan, fecha, monto, esSuma);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReporteIngresosPorPlanDTO>> getIngresosPorVueltas(
            @RequestParam("mes_inicio") int mesInicio,
            @RequestParam("anio_inicio") int anioInicio,
            @RequestParam("mes_fin") int mesFin,
            @RequestParam("anio_fin") int anioFin) {

        // Validar meses y anios correctos
        if (mesInicio < 1 || mesInicio > 12 || mesFin < 1 || mesFin > 12) {
            return ResponseEntity.badRequest().build();
        }

        List<ReporteIngresosPorPlanDTO> reporte = reporteService
                .generarReporteIngresosPorPlan(mesInicio, anioInicio, mesFin, anioFin);

        return ResponseEntity.ok(reporte);
    }
}