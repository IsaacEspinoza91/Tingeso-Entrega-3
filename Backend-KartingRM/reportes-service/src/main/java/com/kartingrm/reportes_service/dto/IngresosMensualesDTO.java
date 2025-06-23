package com.kartingrm.reportes_service.dto;


public class IngresosMensualesDTO {
    private Double ingresosMesActual;
    private Double ingresosMesAnterior;

    // Constructor necesario para la consulta JPA
    public IngresosMensualesDTO(Double ingresosMesActual, Double ingresosMesAnterior) {
        this.ingresosMesActual = ingresosMesActual;
        this.ingresosMesAnterior = ingresosMesAnterior;
    }

    // Getters y Setters
    public Double getIngresosMesActual() {
        return ingresosMesActual;
    }

    public void setIngresosMesActual(Double ingresosMesActual) {
        this.ingresosMesActual = ingresosMesActual;
    }

    public Double getIngresosMesAnterior() {
        return ingresosMesAnterior;
    }

    public void setIngresosMesAnterior(Double ingresosMesAnterior) {
        this.ingresosMesAnterior = ingresosMesAnterior;
    }
}