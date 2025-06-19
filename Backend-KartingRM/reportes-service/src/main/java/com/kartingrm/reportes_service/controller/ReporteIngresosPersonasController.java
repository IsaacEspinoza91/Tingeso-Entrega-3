package com.kartingrm.reportes_service.controller;

import com.kartingrm.reportes_service.DTO.ReporteIngresosPorPersonasDTO;
import com.kartingrm.reportes_service.service.ReporteIngresosPersonasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes-service/segun-rango-personas")
public class ReporteIngresosPersonasController {

    @Autowired
    private ReporteIngresosPersonasService reporteService;

    // Metodo para actualizar rango, dependiendo de si se ejecuta desde un metodo crearComprobante
    //  se indica que se suman los valores. Si se ejecuta desde el metodo de actualizar como no pagado
    //  el comprobante, se indica que restar los valores
    @PostMapping("/actualizar")
    public ResponseEntity<Void> actualizarIngresos(
            @RequestParam String rangoPersonas,
            @RequestParam String fechaReserva,
            @RequestParam Double monto,
            @RequestParam boolean esSuma) {

        LocalDate fecha = LocalDate.parse(fechaReserva);
        reporteService.actualizarIngresos(rangoPersonas, fecha, monto, esSuma);

        return ResponseEntity.ok().build();
    }


    // GET para obtener el reporte
    @GetMapping
    public ResponseEntity<List<ReporteIngresosPorPersonasDTO>> getIngresosPorPersonas(
            @RequestParam("mes_inicio") int mesInicio,
            @RequestParam("anio_inicio") int anioInicio,
            @RequestParam("mes_fin") int mesFin,
            @RequestParam("anio_fin") int anioFin) {

        // Validar parametros para fechas correctas
        if (mesInicio < 1 || mesInicio > 12 || mesFin < 1 || mesFin > 12) {
            return ResponseEntity.badRequest().build();
        }

        // Lista de registros de reporte
        List<ReporteIngresosPorPersonasDTO> reporte = reporteService
                .generarReporteIngresosPorPersonas(mesInicio, anioInicio, mesFin, anioFin);

        return ResponseEntity.ok(reporte);
    }
}
