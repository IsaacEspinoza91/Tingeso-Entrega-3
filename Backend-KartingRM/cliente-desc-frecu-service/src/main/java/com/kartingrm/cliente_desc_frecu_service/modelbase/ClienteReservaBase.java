package com.kartingrm.cliente_desc_frecu_service.modelbase;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
public abstract class ClienteReservaBase {

    protected LocalDate fecha;
    protected String estado;

    public abstract LocalDate getFecha();
    public abstract void setFecha(LocalDate fecha);

    public abstract String getEstado();
    public abstract void setEstado(String estado);
}
