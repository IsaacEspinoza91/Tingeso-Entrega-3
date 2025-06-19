package com.kartingrm.cliente_desc_frecu_service.entity;

import javax.persistence.*;

@Entity
@Table(name = "descuento_cliente_frecuente")
public class DescuentoClienteFrecuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_reservas", nullable = false)
    private int minReservas;

    @Column(name = "max_reservas", nullable = false)
    private int maxReservas;

    @Column(name = "porcentaje_descuento", nullable = false)
    private double porcentajeDescuento;


    // Contructor
    public DescuentoClienteFrecuente() {}

    public DescuentoClienteFrecuente(Long id, int minReservas, double porcentajeDescuento) {
        this.id = id;
        this.minReservas = minReservas;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public DescuentoClienteFrecuente(int minReservas, double porcentajeDescuento) {
        this.minReservas = minReservas;
        this.porcentajeDescuento = porcentajeDescuento;
    }


    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMinReservas() {
        return minReservas;
    }

    public void setMinReservas(int minReservas) {
        this.minReservas = minReservas;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public int getMaxReservas() {
        return maxReservas;
    }

    public void setMaxReservas(int maxReservas) {
        this.maxReservas = maxReservas;
    }

    @Override
    public String toString() {
        return "DescuentoClienteFrecuente{" +
                "id=" + id +
                ", minReservas=" + minReservas +
                ", maxReservas=" + maxReservas +
                ", porcentajeDescuento=" + porcentajeDescuento +
                '}';
    }
}
