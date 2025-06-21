package com.kartingrm.cliente_desc_frecu_service.entity;

import com.kartingrm.cliente_desc_frecu_service.modelbase.ClienteReservaBase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente_reserva")
public class ClienteReserva extends ClienteReservaBase {

    @EmbeddedId
    private ClienteReservaId id;

    // Constructor
    public ClienteReserva() {}

    public ClienteReserva(ClienteReservaId id, LocalDate fecha, String estado) {
        this.id = id;
        setFecha(fecha);
        setEstado(estado);
    }

    // Getters y setters
    public ClienteReservaId getId() {
        return id;
    }

    public void setId(ClienteReservaId id) {
        this.id = id;
    }

    @Override
    @Column(name = "fecha", nullable = false)
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    @Column(name = "estado", nullable = false)
    public String getEstado() {
        return estado;
    }

    @Override
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
