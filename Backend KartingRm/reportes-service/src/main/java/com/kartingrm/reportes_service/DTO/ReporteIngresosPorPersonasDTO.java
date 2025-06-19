package com.kartingrm.reportes_service.DTO;

import java.util.Map;

public class ReporteIngresosPorPersonasDTO {
    private String rangoPersonas;
    private Map<String, Double> ingresosPorMes;
    private Double total;
    private boolean esTotalGeneral;

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

    public Map<String, Double> getIngresosPorMes() {
        return ingresosPorMes;
    }

    public void setIngresosPorMes(Map<String, Double> ingresosPorMes) {
        this.ingresosPorMes = ingresosPorMes;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public boolean isEsTotalGeneral() {
        return esTotalGeneral;
    }

    public void setEsTotalGeneral(boolean esTotalGeneral) {
        this.esTotalGeneral = esTotalGeneral;
    }
}