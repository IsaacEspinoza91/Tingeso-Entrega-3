package com.kartingrm.reportes_service.service;

import com.kartingrm.reportes_service.dto.ReporteIngresosPorPersonasDTO;
import com.kartingrm.reportes_service.entity.ReporteIngresosPersonas;
import com.kartingrm.reportes_service.modelbase.ReportesIngresosServiceBase;
import com.kartingrm.reportes_service.repository.ReporteIngresosPersonasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteIngresosPersonasService extends ReportesIngresosServiceBase {

    // Constantes
    private static final List<String> RANGOS_PERSONAS = List.of("1-2 personas", "3-5 personas", "6-10 personas", "11-15 personas");

    private ReporteIngresosPersonasRepository reportesCantidadPersonasRepository;
    public ReporteIngresosPersonasService(ReporteIngresosPersonasRepository reportesCantidadPersonasRepository) {
        this.reportesCantidadPersonasRepository = reportesCantidadPersonasRepository;
    }



    public List<ReporteIngresosPorPersonasDTO> generarReporteIngresosPorPersonas(int mesInicio, int anioInicio, int mesFin, int anioFin) {
        LocalDate inicio = LocalDate.of(anioInicio, mesInicio, 1);
        LocalDate fin = LocalDate.of(anioFin, mesFin, 1).plusMonths(1).minusDays(1);

        if (inicio.isAfter(fin)) throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");

        // Obtener registros de reportes segun cantidad de personas
        List<ReporteIngresosPersonas> reportes = reportesCantidadPersonasRepository.findReportesPersonasEntreMeses(inicio, fin);

        return procesarDatosReporte(reportes, inicio, fin);
    }



    // Metodos privados, logica interna

    private List<ReporteIngresosPorPersonasDTO> procesarDatosReporte(List<ReporteIngresosPersonas> reportes, LocalDate inicio, LocalDate fin) {
        List<LocalDate> meses = generarListaMeses(inicio, fin);

        // Diccionario para acumular totales por mes. Nombre mes, total
        Map<String, Double> totalesPorMes = inicializarTotalesPorMes(meses);


        List<ReporteIngresosPorPersonasDTO> resultado = procesarRangos(RANGOS_PERSONAS, meses,  reportes, totalesPorMes);

        // calcular total general iterando por todos los totales por mes
        double totalGeneral = 0.0;
        for (Double valor : totalesPorMes.values()) totalGeneral += valor;

        // Agregar el total general de la ultima fila
        resultado.add(new ReporteIngresosPorPersonasDTO("TOTAL GENERAL", totalesPorMes, totalGeneral, true));
        return resultado;
    }



    // Genera lista de ReporteIngresosPorPersona sin obtener el total general
    private List<ReporteIngresosPorPersonasDTO> procesarRangos(List<String> ordenRangos, List<LocalDate> meses, List<ReporteIngresosPersonas> reportes, Map<String, Double> totalesPorMes){
        List<ReporteIngresosPorPersonasDTO> resultado = new ArrayList<>();

        for (String rango : ordenRangos) {
            // Diccionario para obtener ingresos por mes,  se formatea como clave mes y valor 0.0
            Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
            for (LocalDate mes : meses) ingresosPorMes.put(formatearMes(mes), 0.0);


            double totalRango = 0.0;

            // Filtrar reportes para este rango especifico
            List<ReporteIngresosPersonas> reportesRango = reportes.stream()
                    .filter(r -> r.getRangoPersonas().equals(rango))
                    .collect(Collectors.toList());

            // calcular ingresos para los elementos de un rango
            for (ReporteIngresosPersonas reporte : reportesRango) {
                String mesKey = formatearMes(reporte.getMes());
                double ingresos = reporte.getIngresos();

                ingresosPorMes.put(mesKey, ingresosPorMes.get(mesKey) + ingresos);
                totalRango += ingresos;
                totalesPorMes.put(mesKey, totalesPorMes.get(mesKey) + ingresos);
            }

            // Se agrega elemento del rango al resultado
            resultado.add(new ReporteIngresosPorPersonasDTO(rango, ingresosPorMes, totalRango, false));
        }
        return resultado;
    }


    // Actualizar ingresos de registro en la tabla de reporte
    @Transactional
    public void actualizarIngresos(String rangoPersonas, LocalDate fechaReserva, Double monto, boolean esSuma) {
        LocalDate primerDiaMes = fechaReserva.withDayOfMonth(1);

        if (esSuma) {
            sumarIngresos(rangoPersonas, primerDiaMes, monto);
        } else {
            restarIngresos(rangoPersonas, primerDiaMes, monto);
        }
    }

    // Suma ingreso a un registro segun su mes
    private void sumarIngresos(String rangoPersonas, LocalDate mes, Double monto) {
        // Verificar si existe ingresos para ese mes y rango
        var reporteExistente = reportesCantidadPersonasRepository.findByRangoPersonasAndMes(rangoPersonas, mes);

        if (reporteExistente.isPresent()) {
            // Actualizar el existente
            reportesCantidadPersonasRepository.sumarIngresos(rangoPersonas, mes, monto);
        } else {
            // crear objeto reporte segun ingresos
            ReporteIngresosPersonas nuevoReporte = new ReporteIngresosPersonas(
                    rangoPersonas,
                    mes,
                    monto
            );
            reportesCantidadPersonasRepository.save(nuevoReporte);
        }
    }

    // Resta ingreso a un registro segun su mes
    private void restarIngresos(String rangoPersonas, LocalDate mes, Double monto) {
        // Verificar si existe ingresos para ese mes y rango
        var reporteExistente = reportesCantidadPersonasRepository.findByRangoPersonasAndMes(rangoPersonas, mes);

        if (reporteExistente.isPresent()) {
            reportesCantidadPersonasRepository.restarIngresos(rangoPersonas, mes, monto);
        }
    }
}