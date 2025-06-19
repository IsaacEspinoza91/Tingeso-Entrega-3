package com.kartingrm.reservas_comprobantes_service.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ClienteReservaId implements Serializable {
    private Long idCliente;
    private Long idReserva;

    // Constructores
    public ClienteReservaId() {}

    public ClienteReservaId(Long idCliente, Long idReserva) {
        this.idCliente = idCliente;
        this.idReserva = idReserva;
    }

    // Getters y setters
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