package com.kartingrm.reservas_comprobantes_service.model;

public class ReservasDiariasDTO {
    private Long reservasHoy;
    private Long reservasAyer;

    // Constructor necesario para la consulta JPA
    public ReservasDiariasDTO(Long reservasHoy, Long reservasAyer) {
        this.reservasHoy = reservasHoy != null ? reservasHoy : 0L;
        this.reservasAyer = reservasAyer != null ? reservasAyer : 0L;
    }

    // Getters y Setters
    public Long getReservasHoy() {
        return reservasHoy;
    }

    public void setReservasHoy(Long reservasHoy) {
        this.reservasHoy = reservasHoy;
    }

    public Long getReservasAyer() {
        return reservasAyer;
    }

    public void setReservasAyer(Long reservasAyer) {
        this.reservasAyer = reservasAyer;
    }
}