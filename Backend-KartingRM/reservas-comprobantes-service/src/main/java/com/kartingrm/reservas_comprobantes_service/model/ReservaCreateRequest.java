package com.kartingrm.reservas_comprobantes_service.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaCreateRequest {

    private LocalDate fecha;
    private LocalTime horaInicio;
    private String estado;
    private Integer totalPersonas;
    private Long idPlan;
    private Long idReservante;
    private List<Long> idsIntegrantes;
    private Integer descuentoExtra;


    public ReservaCreateRequest() {}
    public ReservaCreateRequest(LocalDate fecha, LocalTime horaInicio, String estado, Integer totalPersonas, Long idPlan, Long idReservante, List<Long> idsIntegrantes, Integer descuentoExtra) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.estado = estado;
        this.totalPersonas = totalPersonas;
        this.idPlan = idPlan;
        this.idReservante = idReservante;
        this.idsIntegrantes = idsIntegrantes;
        this.descuentoExtra = descuentoExtra;
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

    public Integer getTotalPersonas() {
        return totalPersonas;
    }

    public void setTotalPersonas(Integer totalPersonas) {
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

    public List<Long> getIdsIntegrantes() {
        return idsIntegrantes;
    }

    public void setIdsIntegrantes(List<Long> idsIntegrantes) {
        this.idsIntegrantes = idsIntegrantes;
    }

    public Integer getDescuentoExtra() {
        return descuentoExtra;
    }

    public void setDescuentoExtra(Integer descuentoExtra) {
        this.descuentoExtra = descuentoExtra;
    }

    @Override
    public String toString() {
        return "ReservaCreateRequest{" +
                "fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", estado='" + estado + '\'' +
                ", totalPersonas=" + totalPersonas +
                ", idPlan=" + idPlan +
                ", idReservante=" + idReservante +
                ", idsIntegrantes=" + idsIntegrantes +
                ", descuentoExtra=" + descuentoExtra +
                '}';
    }
}
