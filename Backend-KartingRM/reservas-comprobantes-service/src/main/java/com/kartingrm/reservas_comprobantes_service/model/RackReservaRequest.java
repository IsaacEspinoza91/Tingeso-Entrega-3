package com.kartingrm.reservas_comprobantes_service.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RackReservaRequest {

    private Long idReserva;
    private Long idCliente;
    private String nombreReservante;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // Constructor
    public RackReservaRequest() {}
    public RackReservaRequest(Long idReserva, Long idCliente, String nombreReservante, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.nombreReservante = nombreReservante;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }


    // Getters y Setters
    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreReservante() {
        return nombreReservante;
    }

    public void setNombreReservante(String nombreReservante) {
        this.nombreReservante = nombreReservante;
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

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return "RackReservaRequest{" +
                "idReserva=" + idReserva +
                ", idCliente=" + idCliente +
                ", nombreReservante='" + nombreReservante + '\'' +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
}