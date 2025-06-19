package com.kartingrm.cliente_desc_frecu_service.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente_reserva")
public class ClienteReserva {

    @EmbeddedId
    private ClienteReservaId id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "estado", nullable = false)
    private String estado;


    // Constructor
    public ClienteReserva() {}

    public ClienteReserva(ClienteReservaId id, LocalDate fecha, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y setters
    public ClienteReservaId getId() {
        return id;
    }

    public void setId(ClienteReservaId id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "ClienteReserva{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", estado=" + estado +
                '}';
    }
}
