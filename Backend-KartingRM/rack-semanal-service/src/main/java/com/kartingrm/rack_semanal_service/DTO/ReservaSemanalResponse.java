package com.kartingrm.rack_semanal_service.DTO;

public class ReservaSemanalResponse {
    private String fechaInicioSemana;
    private String fechaFinSemana;
    private ReservasPorDia reservasPorDia;

    // Constructores, getters y setters
    public ReservaSemanalResponse() {
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