package com.kartingrm.cliente_desc_frecu_service.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ClienteReservaId implements Serializable {

    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "id_cliente")
    private Long idCliente;


    // Constructores
    public ClienteReservaId() {}

    public ClienteReservaId(Long idReserva, Long idCliente) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
    }

    // Getters y setters
    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteReservaId that = (ClienteReservaId) o;
        return Objects.equals(idReserva, that.idReserva) &&
                Objects.equals(idCliente, that.idCliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva, idCliente);
    }

    @Override
    public String toString() {
        return "ClienteReservaId{" +
                "idReserva=" + idReserva +
                ", idCliente=" + idCliente +
                '}';
    }
}
