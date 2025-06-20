package com.kartingrm.rack_semanal_service.dto;

public class ReservaSemanalResponse {
    private String fechaInicioSemana;
    private String fechaFinSemana;
    private ReservasPorDia reservasPorDia;

    public ReservaSemanalResponse() {
        // Constructor vacio para construccion por partes en service
    }

    public String getFechaInicioSemana() {
        return fechaInicioSemana;
    }

    public void setFechaInicioSemana(String fechaInicioSemana) {
        this.fechaInicioSemana = fechaInicioSemana;
    }

    public String getFechaFinSemana() {
        return fechaFinSemana;
    }

    public void setFechaFinSemana(String fechaFinSemana) {
        this.fechaFinSemana = fechaFinSemana;
    }

    public ReservasPorDia getReservasPorDia() {
        return reservasPorDia;
    }

    public void setReservasPorDia(ReservasPorDia reservasPorDia) {
        this.reservasPorDia = reservasPorDia;
    }
}