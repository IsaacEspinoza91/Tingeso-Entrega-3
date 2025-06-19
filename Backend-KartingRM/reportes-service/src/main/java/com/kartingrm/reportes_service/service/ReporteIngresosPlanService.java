package com.kartingrm.reportes_service.service;

import com.kartingrm.reportes_service.DTO.ReporteIngresosPorPlanDTO;
import com.kartingrm.reportes_service.entity.ReporteIngresosPlan;
import com.kartingrm.reportes_service.repository.ReporteIngresosPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteIngresosPlanService {

    @Autowired
    private ReporteIngresosPlanRepository reportesPlanRepository;
    // formato fecha:   mes-anio
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy", new Locale("es", "ES"));


    public List<ReporteIngresosPorPlanDTO> generarReporteIngresosPorPlan(int mesInicio, int anioInicio, int mesFin, int anioFin) {
        // verificar fechas validas
        LocalDate inicio = LocalDate.of(anioInicio, mesInicio, 1);
        LocalDate fin = LocalDate.of(anioFin, mesFin, 1).plusMonths(1).minusDays(1);

        if (inicio.isAfter(fin)) throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");

        // Obtener reportes de la base de datos
        List<ReporteIngresosPlan> reportes = reportesPlanRepository.findReportesPlanEntreMeses(inicio, fin);

        // Llamado a funcion procesas datos de reporte para generar lista de obejtos ReporteIngresos
        return procesarDatosReporte(reportes, inicio, fin);
    }




    // Metodos privado de logica interna

    // Genera estructura de datos para generar lista de con los elementos de reporte
    private List<ReporteIngresosPorPlanDTO> procesarDatosReporte(List<ReporteIngresosPlan> reportes, LocalDate inicio, LocalDate fin) {
        // obtener lista con los nombres de planes sin repetir
        Set<String> planes = reportes.stream()
                .map(ReporteIngresosPlan::getDescripcionPlan)   // Se mapea funcion para obtener descripcion del plan
                .collect(Collectors.toSet());

        // generar la lista de meses segun el rango
        List<LocalDate> meses = new ArrayList<>();
        LocalDate fecha = inicio;
        while (!fecha.isAfter(fin)) {
            meses.add(fecha.withDayOfMonth(1));     // Se agrega el primer dia del mes
            fecha = fecha.plusMonths(1);    // se suma un mes a la fecha
        }

        // Diccionario para acumular totales por mes
        Map<String, Double> totalesPorMes = new LinkedHashMap<>();
        // Se agregan valores a totalesPorMes, elementso fecha y double 0.0
        meses.forEach(mes -> totalesPorMes.put(formatearMes(mes), 0.0));

        // Lista para retorno, se van acumulando los elementos ReporteIngresoPorPlanDTO
        List<ReporteIngresosPorPlanDTO> resultado = new ArrayList<>();

        // Iteracion entre todos los planes
        for (String plan : planes) {

            // Para cada plan se guardan los ingresos por mes
            Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
            // por cada elemento de meses, se agrega al diccionario ingresosPorMes el nombre y el double 0.0
            meses.forEach(mes -> ingresosPorMes.put(formatearMes(mes), 0.0));

            double totalPlan = 0.0;

            // Obtener reportes del plan especifico
            List<ReporteIngresosPlan> reportesPlan = reportes.stream()
                    .filter(r -> r.getDescripcionPlan().equals(plan))
                    .collect(Collectors.toList());

            // Procesar cada reporte del plan
            for (ReporteIngresosPlan reporte : reportesPlan) {
                String mesKey = formatearMes(reporte.getMes());
                double ingresos = reporte.getIngresos();

                // Suma los ingresos para obtener el total del plan (ultima valor de la fila de plan)
                ingresosPorMes.put(mesKey, ingresosPorMes.get(mesKey) + ingresos);
                totalPlan += ingresos;
                totalesPorMes.put(mesKey, totalesPorMes.get(mesKey) + ingresos);
            }

            resultado.add(new ReporteIngresosPorPlanDTO(
                    plan,
                    ingresosPorMes,
                    totalPlan,
                    false
            ));
        }

        // Ordenar los planes segun cantidad de vueltas (de mayor a menor)
        resultado.sort((a, b) -> {
            int vueltasA = extraerVueltas(a.getDescripcionPlan());
            int vueltasB = extraerVueltas(b.getDescripcionPlan());
            return Integer.compare(vueltasB, vueltasA); // Orden descendente par mostrar desde el front
        });

        // Calcular total general
        double totalGeneral = totalesPorMes.values().stream().mapToDouble(Double::doubleValue).sum();

        // Agregar a lista el elemeneto final de total general, este obtiene las sumas de las columnas, y de la ultima final (total general)
        resultado.add(new ReporteIngresosPorPlanDTO(
                "TOTAL GENERAL",
                totalesPorMes,
                totalGeneral,
                true
        ));

        return resultado;
    }

    // Permite obtener la cantidad de vueltas de un string con el formato de descripcion de plan.
    //  Ejemplo, si "20 vueltas o max 20 min" , retorna 20
    private int extraerVueltas(String descripcionPlan) {
        try {
            String[] partes = descripcionPlan.split(" ");
            return Integer.parseInt(partes[0]);     // Retorna el primer elemento de la lista
        } catch (Exception e) {
            return 0; // Otros planes sin el formato
        }
    }


    private String formatearMes(LocalDate fecha) {
        return fecha.format(monthFormatter);
    }


    // Actualiza los ingresos en registro segun mes en tabla reporte, considera caso cuando se marca como pagado
    // y como no pagado el comprobante (bool esSuma)
    @Transactional
    public void actualizarIngresosPlan(Long idPlan, String descripcionPlan, LocalDate fechaReserva, Double monto, boolean esSuma) {
        LocalDate primerDiaMes = fechaReserva.withDayOfMonth(1);

        if (esSuma) {
            sumarIngresos(idPlan, descripcionPlan, primerDiaMes, monto);
        } else {
            restarIngresos(idPlan, primerDiaMes, monto);
        }
    }

    // Suma ingresos a registro segun mes en tabla reporte
    private void sumarIngresos(Long idPlan, String descripcionPlan, LocalDate mes, Double monto) {
        var reporteExistente = reportesPlanRepository.findByIdPlanAndMes(idPlan, mes);

        if (reporteExistente.isPresent()) {
            reportesPlanRepository.sumarIngresos(idPlan, mes, monto);
        } else {
            ReporteIngresosPlan nuevoReporte = new ReporteIngresosPlan(
                    idPlan,
                    descripcionPlan,
                    mes,
                    monto
            );
            reportesPlanRepository.save(nuevoReporte);
        }
    }

    // Resta ingresos a registro segun mes en tabla reporte
    private void restarIngresos(Long idPlan, LocalDate mes, Double monto) {
        var reporteExistente = reportesPlanRepository.findByIdPlanAndMes(idPlan, mes);

        if (reporteExistente.isPresent()) {
            reportesPlanRepository.restarIngresos(idPlan, mes, monto);
        }
    }
}