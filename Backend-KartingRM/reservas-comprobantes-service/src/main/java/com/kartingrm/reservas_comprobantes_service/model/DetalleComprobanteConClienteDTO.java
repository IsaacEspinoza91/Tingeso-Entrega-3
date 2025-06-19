package com.kartingrm.reservas_comprobantes_service.model;

import com.kartingrm.reservas_comprobantes_service.entity.DetalleComprobante;

import javax.persistence.Column;

public class DetalleComprobanteConClienteDTO {

    private Long id;
    private double tarifa;
    private double descuentoGrupo;
    private double porcentajeDescuentoGrupo;
    private double descuentoEspecial;
    private double porcentajeDescuentoEspecial;
    private double descuentoExtra = 0;
    private double montoFinal;
    private double montoIva;
    private double montoTotal;
    private boolean tieneDescuentoCumpleanios = false;
    private boolean tieneDescuentoClienteFrecuente = false;
    private ClienteDTO cliente;

    public DetalleComprobanteConClienteDTO() {}

    public DetalleComprobanteConClienteDTO(DetalleComprobante detalle, ClienteDTO cliente) {
        // Manytiene estructura de DetalleComprobante
        this.id = detalle.getId();
        this.tarifa = detalle.getTarifa();
        this.descuentoGrupo = detalle.getDescuentoGrupo();
        this.porcentajeDescuentoGrupo = detalle.getPorcentajeDescuentoGrupo();
        this.descuentoEspecial = detalle.getDescuentoEspecial();
        this.porcentajeDescuentoEspecial = detalle.getPorcentajeDescuentoEspecial();
        this.descuentoExtra = detalle.getDescuentoExtra();
        this.montoFinal = detalle.getMontoFinal();
        this.montoIva = detalle.getMontoIva();
        this.montoTotal = detalle.getMontoTotal();
        this.tieneDescuentoCumpleanios = detalle.isTieneDescuentoCumpleanios();
        this.tieneDescuentoClienteFrecuente = detalle.isTieneDescuentoClienteFrecuente();

        this.cliente = cliente;
    }


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

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }
}
