package com.kartingrm.descuento_grupo_service.dto;

import com.kartingrm.descuento_grupo_service.modelbase.DescuentoGrupoBase;

public class DescuentoGrupoDTO extends DescuentoGrupoBase {

    public DescuentoGrupoDTO() {}

    public DescuentoGrupoDTO(int minPersonas, int maxPersonas, double porcentajeDescuento) {
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.porcentajeDescuento = porcentajeDescuento;
    }
}
