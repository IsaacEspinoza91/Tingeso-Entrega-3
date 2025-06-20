package com.kartingrm.rack_semanal_service.modelbase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalTime;

@MappedSuperclass
public abstract class RackReservaBase {


    @Column(name = "id_cliente")
    protected Long idCliente;

    @Column(name = "nombre_cliente")
    protected String nombreReservante;

    @Column(name = "fecha")
    protected LocalDate fecha;

    @Column(name = "hora_inicio")
    protected LocalTime horaInicio;

    @Column(name = "hora_fin")
    protected LocalTime horaFin;


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
}
