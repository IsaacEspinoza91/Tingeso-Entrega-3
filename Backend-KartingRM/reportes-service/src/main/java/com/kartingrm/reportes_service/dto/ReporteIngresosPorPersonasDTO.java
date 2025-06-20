package com.kartingrm.reportes_service.dto;

import com.kartingrm.reportes_service.modelbase.ReporteBase;

import java.util.Map;

public class ReporteIngresosPorPersonasDTO extends ReporteBase {
    private String rangoPersonas;

    // Constructores
    public ReporteIngresosPorPersonasDTO() {
    }

    public ReporteIngresosPorPersonasDTO(String rangoPersonas, Map<String, Double> ingresosPorMes, Double total, boolean esTotalGeneral) {
        this.rangoPersonas = rangoPersonas;
        this.ingresosPorMes = ingresosPorMes;
        this.total = total;
        this.esTotalGeneral = esTotalGeneral;
    }

    // Getters y Setters
    public String getRangoPersonas() {
        return rangoPersonas;
    }

    public void setRangoPersonas(String rangoPersonas) {
        this.rangoPersonas = rangoPersonas;
    }

}