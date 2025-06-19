package com.kartingrm.diasespecialesservice.dto;

import com.kartingrm.diasespecialesservice.modelbase.DiaFeriadoBase;

import java.time.LocalDate;

public class DiaFeriadoDTO extends DiaFeriadoBase {

    public DiaFeriadoDTO() {}

    public DiaFeriadoDTO(String nombre, LocalDate fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

}
