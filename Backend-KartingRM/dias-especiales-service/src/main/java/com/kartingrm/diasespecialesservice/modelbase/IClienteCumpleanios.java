package com.kartingrm.diasespecialesservice.modelbase;

import java.time.LocalDate;

public interface IClienteCumpleanios {
    Long getIdCliente();
    LocalDate getFecha();

    void setIdCliente(Long idCliente);
    void setFecha(LocalDate fecha);
}