package com.kartingrm.descuento_grupo_service.entity;

import javax.persistence.*;

@Entity
@Table(name = "descuento_grupo", indexes = {
        @Index(name = "idx_min_max_personas", columnList = "min_personas, max_personas")
})
public class DescuentoGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_personas", nullable = false)
    private int minPersonas;

    @Column(name = "max_personas")
    private int maxPersonas;

    @Column(name = "porcentaje_descuento", scale = 3, nullable = false)
    private double porcentajeDescuento;

    // Constructores
    public DescuentoGrupo() {
    }

    public DescuentoGrupo(Long id, int minPersonas, int maxPersonas, double porcentajeDescuento) {
        this.id = id;
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public DescuentoGrupo(int minPersonas, int maxPersonas, double porcentajeDescuento) {
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
