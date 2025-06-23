package com.kartingrm.cliente_desc_frecu_service.modelbase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DescuentoClienteFrecuenteBase {

    @Column(name = "min_reservas", nullable = false)
    protected int minReservas;

    @Column(name = "max_reservas", nullable = false)
    protected int maxReservas;

    @Column(name = "porcentaje_descuento", nullable = false)
    protected double porcentajeDescuento;


    public int getMinReservas() {
        return minReservas;
    }

    public void setMinReservas(int minReservas) {
        this.minReservas = minReservas;
    }

    public int getMaxReservas() {
        return maxReservas;
    }

    public void setMaxReservas(int maxReservas) {
        this.maxReservas = maxReservas;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}
