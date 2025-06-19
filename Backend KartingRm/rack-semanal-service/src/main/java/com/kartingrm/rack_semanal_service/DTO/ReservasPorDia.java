package com.kartingrm.rack_semanal_service.DTO;

import java.util.List;

public class ReservasPorDia {
    private List<ReservaInfo> lunes;
    private List<ReservaInfo> martes;
    private List<ReservaInfo> miercoles;
    private List<ReservaInfo> jueves;
    private List<ReservaInfo> viernes;
    private List<ReservaInfo> sabado;
    private List<ReservaInfo> domingo;

    // constructor
    public ReservasPorDia() {}

    public ReservasPorDia(List<ReservaInfo> lunes, List<ReservaInfo> martes, List<ReservaInfo> miercoles, List<ReservaInfo> jueves, List<ReservaInfo> viernes, List<ReservaInfo> sabado, List<ReservaInfo> domingo) {
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
    }

    // Getters y setters
    public List<ReservaInfo> getLunes() {
        return lunes;
    }

    public void setLunes(List<ReservaInfo> lunes) {
        this.lunes = lunes;
    }

    public List<ReservaInfo> getMartes() {
        return martes;
    }

    public void setMartes(List<ReservaInfo> martes) {
        this.martes = martes;
    }

    public List<ReservaInfo> getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(List<ReservaInfo> miercoles) {
        this.miercoles = miercoles;
    }

    public List<ReservaInfo> getJueves() {
        return jueves;
    }

    public void setJueves(List<ReservaInfo> jueves) {
        this.jueves = jueves;
    }

    public List<ReservaInfo> getViernes() {
        return viernes;
    }

    public void setViernes(List<ReservaInfo> viernes) {
        this.viernes = viernes;
    }

    public List<ReservaInfo> getSabado() {
        return sabado;
    }

    public void setSabado(List<ReservaInfo> sabado) {
        this.sabado = sabado;
    }

    public List<ReservaInfo> getDomingo() {
        return domingo;
    }

    public void setDomingo(List<ReservaInfo> domingo) {
        this.domingo = domingo;
    }
}