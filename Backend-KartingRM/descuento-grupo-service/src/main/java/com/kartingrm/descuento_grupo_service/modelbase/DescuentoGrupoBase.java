package com.kartingrm.descuento_grupo_service.modelbase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DescuentoGrupoBase {

    @Column(name = "min_personas", nullable = false)
    protected int minPersonas;

    @Column(name = "max_personas")
    protected int maxPersonas;

    @Column(name = "porcentaje_descuento", scale = 3, nullable = false)
    protected double porcentajeDescuento;


    // Getters y setters
    public int getMinPersonas() {
        return minPersonas;
    }

    public void setMinPersonas(int minPersonas) {
        this.minPersonas = minPersonas;
    }

    public int getMaxPersonas() {
        return maxPersonas;
    }

    public void setMaxPersonas(int maxPersonas) {
        this.maxPersonas = maxPersonas;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}
