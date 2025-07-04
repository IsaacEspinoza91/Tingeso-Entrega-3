package com.kartingrm.rack_semanal_service.entity;

import com.kartingrm.rack_semanal_service.modelbase.RackReservaBase;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rack_reservas")
public class RackReserva extends RackReservaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructores
    public RackReserva() {}

    public RackReserva(Long id, Long idReserva, Long idCliente, String nombreReservante, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.id = id;
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.nombreReservante = nombreReservante;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}