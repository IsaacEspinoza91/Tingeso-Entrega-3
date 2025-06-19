package com.kartingrm.reservas_comprobantes_service.model;

import com.kartingrm.reservas_comprobantes_service.entity.ClienteReservaId;

import java.time.LocalDate;

public class ClienteReservaRequest {

    private ClienteReservaId id;
    private LocalDate fecha;
    private String estado;


    // Constructor
    public ClienteReservaRequest() {}
    public ClienteReservaRequest(ClienteReservaId id, LocalDate fecha, String estado) {
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
}
