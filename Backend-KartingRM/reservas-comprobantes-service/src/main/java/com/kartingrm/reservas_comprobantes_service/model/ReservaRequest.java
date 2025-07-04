package com.kartingrm.reservas_comprobantes_service.model;

import com.kartingrm.reservas_comprobantes_service.modelbase.ReservaBase;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaRequest extends ReservaBase {

    public ReservaRequest() {
        // Vacío para contracción por partes
    }

    public ReservaRequest(LocalDate fecha, LocalTime horaInicio, String estado, int totalPersonas, Long idPlan, Long idReservante) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.estado = estado;
        this.totalPersonas = totalPersonas;
        this.idPlan = idPlan;
        this.idReservante = idReservante;
    }

    @Override
    public String toString() {
        return "ReservaRequest{" +
                "idReservante=" + idReservante +
                ", idPlan=" + idPlan +
                ", totalPersonas=" + totalPersonas +
                ", estado='" + estado + '\'' +
                ", horaFin=" + horaFin +
                ", horaInicio=" + horaInicio +
                ", fecha=" + fecha +
                '}';
    }
}