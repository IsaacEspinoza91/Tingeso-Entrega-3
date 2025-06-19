package com.kartingrm.reservas_comprobantes_service.entity;

import javax.persistence.*;

@Entity
@Table(name = "cliente_reserva")
@IdClass(ClienteReservaId.class)
public class ClienteReserva {

    @Id
    @Column(name = "id_cliente")
    private Long idCliente;

    @Id
    @Column(name = "id_reserva")
    private Long idReserva;

    // Constructores, getters y setters
    public ClienteReserva() {}

    public ClienteReserva(Long idCliente, Long idReserva) {
        this.idCliente = idCliente;
        this.idReserva = idReserva;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }
}