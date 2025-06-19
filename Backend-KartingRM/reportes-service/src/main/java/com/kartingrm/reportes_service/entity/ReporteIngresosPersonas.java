package com.kartingrm.reportes_service.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reportes_ingresos_personas")
public class ReporteIngresosPersonas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rango_personas", nullable = false)
    private String rangoPersonas;

    @Column(name = "mes", nullable = false)
    private LocalDate mes; // primer día del mes

    @Column(name = "ingresos", nullable = false)
    private Double ingresos;


    // Constructores
    public ReporteIngresosPersonas() {
    }
    public ReporteIngresosPersonas(String rangoPersonas, LocalDate mes, Double ingresos) {
        this.rangoPersonas = rangoPersonas;
        this.mes = mes;
        this.ingresos = ingresos;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRangoPersonas() {
        return rangoPersonas;
    }

    public void setRangoPersonas(String rangoPersonas) {
        this.rangoPersonas = rangoPersonas;
    }

    public LocalDate getMes() {
        return mes;
    }

    public void setMes(LocalDate mes) {
        this.mes = mes;
    }

    public Double getIngresos() {
        return ingresos;
    }

    public void setIngresos(Double ingresos) {
        this.ingresos = ingresos;
    }
}