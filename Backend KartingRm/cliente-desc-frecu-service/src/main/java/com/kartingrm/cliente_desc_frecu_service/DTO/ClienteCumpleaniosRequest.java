package com.kartingrm.cliente_desc_frecu_service.DTO;

import java.time.LocalDate;

public class ClienteCumpleaniosRequest {

    private Long idCliente;
    private LocalDate fecha;


    public ClienteCumpleaniosRequest() {}
    public ClienteCumpleaniosRequest(Long idCliente, LocalDate fecha) {
        this.idCliente = idCliente;
        this.fecha = fecha;
    }

    // Metodos
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
