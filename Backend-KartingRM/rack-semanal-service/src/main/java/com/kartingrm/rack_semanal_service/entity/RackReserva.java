package com.kartingrm.rack_semanal_service.entity;

import com.kartingrm.rack_semanal_service.modelbase.RackReservaBase;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rack_reservas")
public class RackReserva extends RackReservaBase {
    @Id
    @Column(name = "id_reserva")
    private Long idReserva;

    // Constructores
    public RackReserva() {}

    public RackReserva(Long idReserva, Long idCliente, String nombreReservante, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.nombreReservante = nombreReservante;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y setters
    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

}