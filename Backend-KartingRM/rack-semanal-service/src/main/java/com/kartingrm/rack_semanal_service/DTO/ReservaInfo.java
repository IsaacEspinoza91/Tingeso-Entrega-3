package com.kartingrm.rack_semanal_service.DTO;

public class ReservaInfo {
    private Long codigoReserva;
    private String nombreReservante;
    private String horaInicio;
    private String horaFin;

    // Constructores, getters y setters
    public ReservaInfo() {
    }

    public ReservaInfo(Long codigoReserva, String nombreReservante, String horaInicio, String horaFin) {
        this.codigoReserva = codigoReserva;
        this.nombreReservante = nombreReservante;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y setters
    public Long getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(Long codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public String getNombreReservante() {
        return nombreReservante;
    }

    public void setNombreReservante(String nombreReservante) {
        this.nombreReservante = nombreReservante;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}