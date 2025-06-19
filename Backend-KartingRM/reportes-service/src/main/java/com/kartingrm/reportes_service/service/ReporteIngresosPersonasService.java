package com.kartingrm.reportes_service.service;

import com.kartingrm.reportes_service.DTO.ReporteIngresosPorPersonasDTO;
import com.kartingrm.reportes_service.entity.ReporteIngresosPersonas;
import com.kartingrm.reportes_service.repository.ReporteIngresosPersonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteIngresosPersonasService {

    @Autowired
    private ReporteIngresosPersonasRepository reportesCantidadPersonasRepository;
    // Permite crear el string para foramto de mes y anio
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy", new Locale("es", "ES"));



    public List<ReporteIngresosPorPersonasDTO> generarReporteIngresosPorPersonas(int mesInicio, int anioInicio, int mesFin, int anioFin) {
        // Validar fechas
        LocalDate inicio = LocalDate.of(anioInicio, mesInicio, 1);
        LocalDate fin = LocalDate.of(anioFin, mesFin, 1).plusMonths(1).minusDays(1);

        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        // Obtener registros de reportes segun cantidad de personas
        List<ReporteIngresosPersonas> reportes = reportesCantidadPersonasRepository.findReportesPersonasEntreMeses(inicio, fin);

        return procesarDatosReporte(reportes, inicio, fin);
    }




    // Metodos privados, logica interna

    private List<ReporteIngresosPorPersonasDTO> procesarDatosReporte(List<ReporteIngresosPersonas> reportes, LocalDate inicio, LocalDate fin) {
        // Lista con Strings de nombres de cantidad de personas
        List<String> ordenRangos = Arrays.asList("1-2 personas", "3-5 personas", "6-10 personas", "11-15 personas");

        // generar la lista de meses segun el rango
        List<LocalDate> meses = new ArrayList<>();
        LocalDate fecha = inicio;
        while (!fecha.isAfter(fin)) {
            meses.add(fecha.withDayOfMonth(1));
            fecha = fecha.plusMonths(1);
        }

        // Diccionario para acumular totales por mes
        Map<String, Double> totalesPorMes = new LinkedHashMap<>();
        for (LocalDate mes : meses) {
            totalesPorMes.put(formatearMes(mes), 0.0);
        }

        // Lista para generar resultado
        List<ReporteIngresosPorPersonasDTO> resultado = new ArrayList<>();

        // Iteracion sobre los rangos determinados en lista ordenRangos
        for (String rango : ordenRangos) {
            // Diccionario para obtener ingresos por mes,  se formatea como clave mes y valor 0.0
            Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
            for (LocalDate mes : meses) {
                ingresosPorMes.put(formatearMes(mes), 0.0);
            }

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
            resultado.add(new ReporteIngresosPorPersonasDTO(
                    rango,
                    ingresosPorMes,
                    totalRango,
                    false
            ));
        }

        // calcular total general iterando por todos los totales por mes
        double totalGeneral = 0.0;
        for (Double valor : totalesPorMes.values()) {
            totalGeneral += valor;
        }

        // Agregar el total general de la ultima fila
        resultado.add(new ReporteIngresosPorPersonasDTO(
                "TOTAL GENERAL",
                totalesPorMes,
                totalGeneral,
                true
        ));

        return resultado;
    }

    private String formatearMes(LocalDate fecha) {
        return fecha.format(monthFormatter);
    }


    // Actualizar ingresos de registro en la tabla de reporte, para agregar o quitar segun el atributo bool esSuma
    @Transactional
    public void actualizarIngresos(String rangoPersonas, LocalDate fechaReserva, Double monto, boolean esSuma) {
        LocalDate primerDiaMes = fechaReserva.withDayOfMonth(1);

        // Si es suma, se crea comprobante --> se agrega valor a reporte
        if (esSuma) {
            sumarIngresos(rangoPersonas, primerDiaMes, monto);
        } else {
            // Si no es suma, se elimina o cambia a estado no pagado el comprobante --> se resta valor a reporte
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