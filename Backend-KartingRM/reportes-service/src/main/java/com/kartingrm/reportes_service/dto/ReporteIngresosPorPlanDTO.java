package com.kartingrm.reportes_service.dto;

import com.kartingrm.reportes_service.modelbase.ReporteBase;

import java.util.Map;

public class ReporteIngresosPorPlanDTO extends ReporteBase {
    private String descripcionPlan;

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

}