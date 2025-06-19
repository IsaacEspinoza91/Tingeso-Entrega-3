package com.kartingrm.diasespecialesservice.entity;

import com.kartingrm.diasespecialesservice.modelbase.DiaFeriadoBase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dia_feriado", indexes = {
        @Index(name = "idx_dia_feriado_fecha", columnList = "fecha")
})
public class DiaFeriado extends DiaFeriadoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructor
    public DiaFeriado() {}

    public DiaFeriado(Long id, String nombre, LocalDate fecha) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
