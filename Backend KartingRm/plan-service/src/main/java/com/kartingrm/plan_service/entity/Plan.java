package com.kartingrm.plan_service.entity;

import javax.persistence.*;

@Entity
@Table(name = "plan")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descripcion;

    @Column(name = "duracion_total", nullable = false)
    private int duracionTotal;

    @Column(name = "precio_regular", nullable = false, precision = 10, scale = 2)
    private Double precioRegular;

    @Column(name = "precio_fin_semana", nullable = false, precision = 10, scale = 2)
    private Double precioFinSemana;

    @Column(name = "precio_feriado", nullable = false, precision = 10, scale = 2)
    private Double precioFeriado;

    public Plan() {
    }

    public Plan(Long id, String descripcion, int duracionTotal, Double precioRegular, Double precioFinSemana, Double precioFeriado) {
        this.id = id;
        this.descripcion = descripcion;
        this.duracionTotal = duracionTotal;
        this.precioRegular = precioRegular;
        this.precioFinSemana = precioFinSemana;
        this.precioFeriado = precioFeriado;
    }

    public Plan(String descripcion, int duracionTotal, Double precioRegular, Double precioFinSemana, Double precioFeriado) {
        this.descripcion = descripcion;
        this.duracionTotal = duracionTotal;
        this.precioRegular = precioRegular;
        this.precioFinSemana = precioFinSemana;
        this.precioFeriado = precioFeriado;
    }

    // Metodos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionTotal() {
        return duracionTotal;
    }

    public void setDuracionTotal(int duracionTotal) {
        this.duracionTotal = duracionTotal;
    }

    public Double getPrecioRegular() {
        return precioRegular;
    }

    public void setPrecioRegular(Double precioRegular) {
        this.precioRegular = precioRegular;
    }

    public Double getPrecioFinSemana() {
        return precioFinSemana;
    }

    public void setPrecioFinSemana(Double precioFinSemana) {
        this.precioFinSemana = precioFinSemana;
    }

    public Double getPrecioFeriado() {
        return precioFeriado;
    }

    public void setPrecioFeriado(Double precioFeriado) {
        this.precioFeriado = precioFeriado;
    }
}
