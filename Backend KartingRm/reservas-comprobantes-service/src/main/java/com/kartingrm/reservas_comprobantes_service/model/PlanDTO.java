package com.kartingrm.reservas_comprobantes_service.model;


public class PlanDTO {

    private Long id;
    private String descripcion;
    private int duracionTotal;
    private Double precioRegular;
    private Double precioFinSemana;
    private Double precioFeriado;

    // Constructor
    public PlanDTO() {}

    public PlanDTO(String descripcion, int duracionTotal, Double precioRegular, Double precioFinSemana, Double precioFeriado) {
        this.descripcion = descripcion;
        this.duracionTotal = duracionTotal;
        this.precioRegular = precioRegular;
        this.precioFinSemana = precioFinSemana;
        this.precioFeriado = precioFeriado;
    }

    public PlanDTO(Long id, String descripcion, int duracionTotal, Double precioRegular, Double precioFinSemana, Double precioFeriado) {
        this.id = id;
        this.descripcion = descripcion;
        this.duracionTotal = duracionTotal;
        this.precioRegular = precioRegular;
        this.precioFinSemana = precioFinSemana;
        this.precioFeriado = precioFeriado;
    }

    // getters y setters


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


    @Override
    public String toString() {
        return "PlanDTO{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", duracionTotal=" + duracionTotal +
                ", precioRegular=" + precioRegular +
                ", precioFinSemana=" + precioFinSemana +
                ", precioFeriado=" + precioFeriado +
                '}';
    }
}
