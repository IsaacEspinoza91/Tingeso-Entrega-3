package com.kartingrm.cliente_desc_frecu_service.dto;

import com.kartingrm.cliente_desc_frecu_service.entity.ClienteReservaId;
import com.kartingrm.cliente_desc_frecu_service.modelbase.ClienteReservaBase;

import java.time.LocalDate;

public class ClienteReservaDTO extends ClienteReservaBase {

    private ClienteReservaId id;

    public ClienteReservaDTO() {
        // Constructor vacío para crear objeto por partes
    }



    // Getters y setters
    public ClienteReservaId getId() {
        return id;
    }

    public void setId(ClienteReservaId id) {
        this.id = id;
    }

    @Override
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String getEstado() {
        return estado;
    }

    @Override
    public void setEstado(String estado) {
        this.estado = estado;
    }

}
