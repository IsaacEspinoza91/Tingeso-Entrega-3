package com.kartingrm.diasespecialesservice.modelbase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
public abstract class DiaFeriadoBase {

    @Column(name = "nombre")
    protected String nombre;

    @Column(name = "fecha", nullable = false)
    protected LocalDate fecha;


    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
