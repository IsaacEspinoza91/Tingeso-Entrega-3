package com.kartingrm.planservice.dto;

import com.kartingrm.planservice.modelbase.PlanBase;

public class PlanDTO extends PlanBase {

    public PlanDTO() {}

    public PlanDTO(String descripcion, int duracionTotal, Double precioRegular, Double precioFinSemana, Double precioFeriado) {
        this.descripcion = descripcion;
        this.duracionTotal = duracionTotal;
        this.precioRegular = precioRegular;
        this.precioFinSemana = precioFinSemana;
        this.precioFeriado = precioFeriado;
    }
}