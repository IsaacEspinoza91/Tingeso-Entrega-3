package com.kartingrm.reportes_service.service;

import com.kartingrm.reportes_service.dto.IngresosMensualesDTO;
import com.kartingrm.reportes_service.dto.ReporteIngresosPorPlanDTO;
import com.kartingrm.reportes_service.entity.ReporteIngresosPlan;
import com.kartingrm.reportes_service.modelbase.ReportesIngresosServiceBase;
import com.kartingrm.reportes_service.repository.ReporteIngresosPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteIngresosPlanService extends ReportesIngresosServiceBase {

    private final ReporteIngresosPlanRepository reportesPlanRepository;
    public ReporteIngresosPlanService(ReporteIngresosPlanRepository reportesPlanRepository) {
        this.reportesPlanRepository = reportesPlanRepository;
    }


    public List<ReporteIngresosPorPlanDTO> generarReporteIngresosPorPlan(int mesInicio, int anioInicio, int mesFin, int anioFin) {
        LocalDate inicio = LocalDate.of(anioInicio, mesInicio, 1);
        LocalDate fin = LocalDate.of(anioFin, mesFin, 1).plusMonths(1).minusDays(1);

        if (inicio.isAfter(fin)) throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");

        // Obtener reportes de la base de datos
        List<ReporteIngresosPlan> reportes = reportesPlanRepository.findReportesPlanEntreMeses(inicio, fin);

        return procesarDatosReporte(reportes, inicio, fin);
    }




    // Metodos privado de logica interna

    // Genera estructura de datos para generar lista de con los elementos de reporte
    private List<ReporteIngresosPorPlanDTO> procesarDatosReporte(List<ReporteIngresosPlan> reportes, LocalDate inicio, LocalDate fin) {
        // obtener lista con los nombres de planes sin repetir
        Set<String> planes = reportes.stream()
                .map(ReporteIngresosPlan::getDescripcionPlan)   // Se mapea funcion para obtener descripcion del plan
                .collect(Collectors.toSet());

        List<LocalDate> meses = generarListaMeses(inicio, fin);

        Map<String, Double> totalesPorMes = inicializarTotalesPorMes(meses);

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

            resultado.add(new ReporteIngresosPorPlanDTO(plan, ingresosPorMes, totalPlan, false));
        }

        // Ordenar los planes segun cantidad de vueltas (de mayor a menor)
        resultado.sort((a, b) -> {
            int vueltasA = extraerVueltas(a.getDescripcionPlan());
            int vueltasB = extraerVueltas(b.getDescripcionPlan());
            return Integer.compare(vueltasB, vueltasA); // Orden descendente para mostrar desde el front
        });

        double totalGeneral = totalesPorMes.values().stream().mapToDouble(Double::doubleValue).sum();

        // Agregar a lista el elemeneto final de total general
        resultado.add(new ReporteIngresosPorPlanDTO("TOTAL GENERAL", totalesPorMes, totalGeneral, true));

        return resultado;
    }

    // Permite obtener la cantidad de vueltas de un string con el formato de descripcion de plan.
    //  Ejemplo, si "20 vueltas o max 20 min" , retorna 20
    public int extraerVueltas(String descripcionPlan) {
        try {
            String[] partes = descripcionPlan.split(" ");
            return Integer.parseInt(partes[0]);     // primer elemento de la lista
        } catch (Exception e) {
            return 0;
        }
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
            ReporteIngresosPlan nuevoReporte = new ReporteIngresosPlan(idPlan, descripcionPlan, mes, monto);
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

    public IngresosMensualesDTO obtenerIngresosMensuales() {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaMesAnterior = fechaActual.minusMonths(1);

        return reportesPlanRepository.sumIngresosMensuales(fechaActual, fechaMesAnterior);
    }

}