package com.kartingrm.reservas_comprobantes_service.entity;

import javax.persistence.*;

@Entity
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double total;

    private boolean pagado;

    @Column(name = "id_reserva",nullable = false)
    private Long idReserva;


    // Constructores
    public Comprobante() {}

    public Comprobante(Long id, Double total, boolean pagado, Long idReserva) {
        this.id = id;
        this.total = total;
        this.pagado = pagado;
        this.idReserva = idReserva;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }


    @Override
    public String toString() {
        return "Comprobante{" +
                "id=" + id +
                ", total=" + total +
                ", pagado=" + pagado +
                ", idReserva=" + idReserva +
                '}';
    }
}
