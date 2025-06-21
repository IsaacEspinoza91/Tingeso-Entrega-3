package com.kartingrm.reservas_comprobantes_service.entity;

import com.kartingrm.reservas_comprobantes_service.model.ReservaRequest;
import com.kartingrm.reservas_comprobantes_service.modelbase.ReservaBase;

import javax.persistence.*;

@Entity
@Table(name = "reserva")
public class Reserva extends ReservaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructor
    public Reserva() {
        // Vacío para contrucción por partes
    }

    public Reserva(ReservaRequest reservaRequest) {
        this.fecha = reservaRequest.getFecha();
        this.horaInicio = reservaRequest.getHoraInicio();
        this.estado = reservaRequest.getEstado();
        this.totalPersonas = reservaRequest.getTotalPersonas();
        this.idPlan = reservaRequest.getIdPlan();
        this.idReservante = reservaRequest.getIdReservante();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", estado='" + estado + '\'' +
                ", totalPersonas=" + totalPersonas +
                ", idPlan=" + idPlan +
                ", idReservante=" + idReservante +
                '}';
    }
}
