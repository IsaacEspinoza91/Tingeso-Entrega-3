package com.kartingrm.cliente_desc_frecu_service.entity;

import com.kartingrm.cliente_desc_frecu_service.modelbase.DescuentoClienteFrecuenteBase;

import javax.persistence.*;

@Entity
@Table(name = "descuento_cliente_frecuente")
public class DescuentoClienteFrecuente extends DescuentoClienteFrecuenteBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
