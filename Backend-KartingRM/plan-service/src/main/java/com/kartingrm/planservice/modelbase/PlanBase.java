package com.kartingrm.planservice.modelbase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

// No es entidad, pero sus atributos son mapeados a la clase hija entidad
@MappedSuperclass
public abstract class PlanBase {

    @Column(nullable = false, length = 100)
    protected String descripcion;

    @Column(name = "duracion_total", nullable = false)
    protected int duracionTotal;

    @Column(name = "precio_regular", nullable = false, precision = 10, scale = 2)
    protected Double precioRegular;

    @Column(name = "precio_fin_semana", nullable = false, precision = 10, scale = 2)
    protected Double precioFinSemana;

    @Column(name = "precio_feriado", nullable = false, precision = 10, scale = 2)
    protected Double precioFeriado;

    // Getters y setters
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
