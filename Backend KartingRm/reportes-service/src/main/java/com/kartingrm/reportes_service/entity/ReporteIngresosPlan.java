package com.kartingrm.reportes_service.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reportes_ingresos_plan")
public class ReporteIngresosPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_plan", nullable = false)
    private Long idPlan;

    @Column(name = "descripcion_plan", nullable = false)
    private String descripcionPlan;

    @Column(name = "mes", nullable = false)
    private LocalDate mes; // primer dia del mes

    @Column(name = "ingresos", nullable = false)
    private Double ingresos;

    // Constructores
    public ReporteIngresosPlan() {
    }

    public ReporteIngresosPlan(Long idPlan, String descripcionPlan, LocalDate mes, Double ingresos) {
        this.idPlan = idPlan;
        this.descripcionPlan = descripcionPlan;
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

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public String getDescripcionPlan() {
        return descripcionPlan;
    }

    public void setDescripcionPlan(String descripcionPlan) {
        this.descripcionPlan = descripcionPlan;
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