package com.kartingrm.dias_especiales_service.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dia_feriado", indexes = {
        @Index(name = "idx_dia_feriado_fecha", columnList = "fecha")
})
public class DiaFeriado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;


    // Constructor
    public DiaFeriado() {}

    public DiaFeriado(String nombre, LocalDate fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

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
