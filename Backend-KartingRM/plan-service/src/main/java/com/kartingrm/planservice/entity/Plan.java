package com.kartingrm.planservice.entity;

import com.kartingrm.planservice.modelbase.PlanBase;

import javax.persistence.*;

@Entity
@Table(name = "plan")
public class Plan extends PlanBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

}
