package com.kartingrm.reservas_comprobantes_service.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "total_personas", nullable = false)
    private int totalPersonas;

    @Column(name = "id_plan", nullable = false)
    private Long idPlan;

    @Column(name = "id_reservante", nullable = false)
    private Long idReservante;



    // Constructor
    public Reserva() {}

    public Reserva(Long id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado, int totalPersonas, Long idPlan, Long idReservante) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.totalPersonas = totalPersonas;
        this.idPlan = idPlan;
        this.idReservante = idReservante;
    }

    public Reserva(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado, int totalPersonas, Long idPlan, Long idReservante) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.totalPersonas = totalPersonas;
        this.idPlan = idPlan;
        this.idReservante = idReservante;
    }


    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTotalPersonas() {
        return totalPersonas;
    }

    public void setTotalPersonas(int totalPersonas) {
        this.totalPersonas = totalPersonas;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public Long getIdReservante() {
        return idReservante;
    }

    public void setIdReservante(Long idReservante) {
        this.idReservante = idReservante;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", estado='" + estado + '\'' +
                ", totalPersonas=" + totalPersonas +
                ", idPlan=" + idPlan +
                ", idReservante=" + idReservante +
                '}';
    }
}
