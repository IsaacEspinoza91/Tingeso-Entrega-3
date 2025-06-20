package com.kartingrm.reportes_service.modelbase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class ReportesIngresosServiceBase {

    protected final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy", Locale.forLanguageTag("es-ES"));


    // Map de con elementos (nombre mes, total ganancias)
    protected Map<String, Double> inicializarTotalesPorMes(List<LocalDate> meses) {
        Map<String, Double> map = new LinkedHashMap<>();
        meses.forEach(mes -> map.put(formatearMes(mes), 0.0));
        return map;
    }

    protected String formatearMes(LocalDate fecha) {
        return fecha.format(monthFormatter);
    }

    // Lista de meses (primer dia del mes)
    protected List<LocalDate> generarListaMeses(LocalDate inicio, LocalDate fin) {
        List<LocalDate> meses = new ArrayList<>();
        LocalDate fecha = inicio;
        while (!fecha.isAfter(fin)) {
            meses.add(fecha.withDayOfMonth(1));
            fecha = fecha.plusMonths(1);
        }
        return meses;
    }

}
