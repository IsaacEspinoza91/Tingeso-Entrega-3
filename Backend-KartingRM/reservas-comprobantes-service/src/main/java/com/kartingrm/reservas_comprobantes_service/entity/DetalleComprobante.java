package com.kartingrm.reservas_comprobantes_service.entity;

import javax.persistence.*;

@Entity
@Table(name = "detalle_comprobante")
public class DetalleComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double tarifa;

    @Column(name = "descuento_grupo")
    private double descuentoGrupo;
    @Column(name = "porcentaje_desc_grupo")
    private double porcentajeDescuentoGrupo;        // Guarda el valor de porcentaje para mostrar en la boleta

    @Column(name = "descuento_especial")
    private double descuentoEspecial;               // Descuento cliente frecuente o cumpleanios
    @Column(name = "porcentaje_desc_especial")
    private double porcentajeDescuentoEspecial;

    @Column(name = "desc_extra")
    private double descuentoExtra = 0;              // Descuento manual extra, default 0
    @Column(name = "monto_final")
    private double montoFinal;             // Despues de aplicar tarifas y descuentos
    @Column(name = "monto_iva")
    private double montoIva;
    @Column(name = "monto_total")
    private double montoTotal;             // Total incluyendo iva

    @Column(name = "tiene_desc_cumple")
    private boolean tieneDescuentoCumpleanios = false;      // Por default ambos descuentos son falsos, despues se analiza si cambian
    @Column(name = "tiene_desc_cliente_frecuente")
    private boolean tieneDescuentoClienteFrecuente = false;

    @Column(name = "id_cliente")
    private long idCliente;

    @Column(name = "id_comprobante")
    private long idComprobante;


    // Constructor
    public DetalleComprobante() {}
    public DetalleComprobante(Long id, double tarifa, double descuentoGrupo, double porcentajeDescuentoGrupo,
                              double descuentoEspecial, double porcentajeDescuentoEspecial, double descuentoExtra,
                              double montoFinal, double montoIva, double montoTotal, boolean tieneDescuentoCumpleanios,
                              boolean tieneDescuentoClienteFrecuente, long idCliente, long idComprobante) {
        this.id = id;
        this.tarifa = tarifa;
        this.descuentoGrupo = descuentoGrupo;
        this.porcentajeDescuentoGrupo = porcentajeDescuentoGrupo;
        this.descuentoEspecial = descuentoEspecial;
        this.porcentajeDescuentoEspecial = porcentajeDescuentoEspecial;
        this.descuentoExtra = descuentoExtra;
        this.montoFinal = montoFinal;
        this.montoIva = montoIva;
        this.montoTotal = montoTotal;
        this.tieneDescuentoCumpleanios = tieneDescuentoCumpleanios;
        this.tieneDescuentoClienteFrecuente = tieneDescuentoClienteFrecuente;
        this.idCliente = idCliente;
        this.idComprobante = idComprobante;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public double getDescuentoGrupo() {
        return descuentoGrupo;
    }

    public void setDescuentoGrupo(double descuentoGrupo) {
        this.descuentoGrupo = descuentoGrupo;
    }

    public double getPorcentajeDescuentoGrupo() {
        return porcentajeDescuentoGrupo;
    }

    public void setPorcentajeDescuentoGrupo(double porcentajeDescuentoGrupo) {
        this.porcentajeDescuentoGrupo = porcentajeDescuentoGrupo;
    }

    public double getDescuentoEspecial() {
        return descuentoEspecial;
    }

    public void setDescuentoEspecial(double descuentoEspecial) {
        this.descuentoEspecial = descuentoEspecial;
    }

    public double getPorcentajeDescuentoEspecial() {
        return porcentajeDescuentoEspecial;
    }

    public void setPorcentajeDescuentoEspecial(double porcentajeDescuentoEspecial) {
        this.porcentajeDescuentoEspecial = porcentajeDescuentoEspecial;
    }

    public double getDescuentoExtra() {
        return descuentoExtra;
    }

    public void setDescuentoExtra(double descuentoExtra) {
        this.descuentoExtra = descuentoExtra;
    }

    public double getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public double getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(double montoIva) {
        this.montoIva = montoIva;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public boolean isTieneDescuentoCumpleanios() {
        return tieneDescuentoCumpleanios;
    }

    public void setTieneDescuentoCumpleanios(boolean tieneDescuentoCumpleanios) {
        this.tieneDescuentoCumpleanios = tieneDescuentoCumpleanios;
    }

    public boolean isTieneDescuentoClienteFrecuente() {
        return tieneDescuentoClienteFrecuente;
    }

    public void setTieneDescuentoClienteFrecuente(boolean tieneDescuentoClienteFrecuente) {
        this.tieneDescuentoClienteFrecuente = tieneDescuentoClienteFrecuente;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public long getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(long idComprobante) {
        this.idComprobante = idComprobante;
    }
}