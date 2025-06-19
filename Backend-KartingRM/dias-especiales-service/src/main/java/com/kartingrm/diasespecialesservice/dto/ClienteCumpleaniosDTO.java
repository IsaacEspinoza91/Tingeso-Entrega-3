package com.kartingrm.diasespecialesservice.dto;

import com.kartingrm.diasespecialesservice.modelbase.IClienteCumpleanios;

import java.time.LocalDate;

public class ClienteCumpleaniosDTO implements IClienteCumpleanios {
    private Long idCliente;
    private LocalDate fecha;

    // metodos de la interfaz
    @Override
    public Long getIdCliente() { return idCliente; }

    @Override
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    @Override
    public LocalDate getFecha() { return fecha; }

    @Override
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
