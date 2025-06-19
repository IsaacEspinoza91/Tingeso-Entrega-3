package com.kartingrm.reportes_service.DTO;

import java.util.Map;

public class ReporteIngresosPorPlanDTO {
    private String descripcionPlan;
    private Map<String, Double> ingresosPorMes;
    private Double total;
    private boolean esTotalGeneral;

    // Constructores
    public ReporteIngresosPorPlanDTO() {
    }

    public ReporteIngresosPorPlanDTO(String descripcionPlan, Map<String, Double> ingresosPorMes, Double total, boolean esTotalGeneral) {
        this.descripcionPlan = descripcionPlan;
        this.ingresosPorMes = ingresosPorMes;
        this.total = total;
        this.esTotalGeneral = esTotalGeneral;
    }

    // getters y Setters
    public String getDescripcionPlan() {
        return descripcionPlan;
    }

    public void setDescripcionPlan(String descripcionPlan) {
        this.descripcionPlan = descripcionPlan;
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