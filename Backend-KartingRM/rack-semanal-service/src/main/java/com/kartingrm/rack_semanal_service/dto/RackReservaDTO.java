package com.kartingrm.rack_semanal_service.dto;

import com.kartingrm.rack_semanal_service.entity.RackReserva;

import java.time.LocalDate;
import java.time.LocalTime;

public class RackReservaDTO extends RackReserva {

    public RackReservaDTO() {}

    public RackReservaDTO(Long idCliente, String nombreReservante,  LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.idCliente = idCliente;
        this.nombreReservante = nombreReservante;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }


}
