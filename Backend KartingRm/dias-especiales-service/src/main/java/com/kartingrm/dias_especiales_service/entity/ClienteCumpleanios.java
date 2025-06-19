package com.kartingrm.dias_especiales_service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "cliente_cumpleanios")
public class ClienteCumpleanios {

    @Id
    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "fecha")
    private LocalDate fecha;

    // Constructor
    public ClienteCumpleanios() {}

    public ClienteCumpleanios(Long idCliente, LocalDate fecha) {
        this.idCliente = idCliente;
        this.fecha = fecha;
    }


    // Getters y seeters
    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
