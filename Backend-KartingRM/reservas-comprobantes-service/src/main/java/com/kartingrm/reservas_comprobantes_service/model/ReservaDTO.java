package com.kartingrm.reservas_comprobantes_service.model;

import com.kartingrm.reservas_comprobantes_service.entity.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private int totalPersonas;
    private PlanDTO plan;
    private ClienteDTO reservante;


    public ReservaDTO() {}

    public ReservaDTO(Reserva reserva, PlanDTO plan, ClienteDTO reservante) {
        this.id = reserva.getId();
        this.fecha = reserva.getFecha();
        this.horaInicio = reserva.getHoraInicio();
        this.horaFin = reserva.getHoraFin();
        this.estado = reserva.getEstado();
        this.totalPersonas = reserva.getTotalPersonas();
        this.plan = plan;
        this.reservante = reservante;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDTO plan) {
        this.plan = plan;
    }

    public ClienteDTO getReservante() {
        return reservante;
    }

    public void setReservante(ClienteDTO reservante) {
        this.reservante = reservante;
    }
}
