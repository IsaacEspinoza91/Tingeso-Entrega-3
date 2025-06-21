package com.kartingrm.cliente_desc_frecu_service.dto;

import com.kartingrm.cliente_desc_frecu_service.modelbase.DescuentoClienteFrecuenteBase;

public class DescuentoClienteFrecuenteDTO extends DescuentoClienteFrecuenteBase {

    public DescuentoClienteFrecuenteDTO() {}

    public DescuentoClienteFrecuenteDTO(int minReservas, double porcentajeDescuento) {
        this.minReservas = minReservas;
        this.porcentajeDescuento = porcentajeDescuento;
    }

}
