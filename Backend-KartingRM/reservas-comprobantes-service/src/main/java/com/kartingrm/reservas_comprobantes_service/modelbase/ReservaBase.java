package com.kartingrm.reservas_comprobantes_service.modelbase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalTime;

@MappedSuperclass
public abstract class ReservaBase {

    @Column(name = "fecha", nullable = false)
    protected LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    protected LocalTime horaInicio;

    @Column(name = "hora_fin")
    protected LocalTime horaFin;

    @Column(name = "estado", nullable = false)
    protected String estado;

    @Column(name = "total_personas", nullable = false)
    protected int totalPersonas;

    @Column(name = "id_plan", nullable = false)
    protected Long idPlan;

    @Column(name = "id_reservante", nullable = false)
    protected Long idReservante;



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
