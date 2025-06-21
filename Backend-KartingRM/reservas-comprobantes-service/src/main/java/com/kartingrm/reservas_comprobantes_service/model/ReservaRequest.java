package com.kartingrm.reservas_comprobantes_service.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaRequest {

    private LocalDate fecha;
    private LocalTime horaInicio;
    private String estado;
    private int totalPersonas;
    private Long idPlan;
    private Long idReservante;


    public ReservaRequest() {
        // Vacío para contracción por partes
    }



    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTotalPersonas() {
        return totalPersonas;
    }

    public void setTotalPersonas(int totalPersonas) {
        this.totalPersonas = totalPersonas;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public Long getIdReservante() {
        return idReservante;
    }

    public void setIdReservante(Long idReservante) {
        this.idReservante = idReservante;
    }
}