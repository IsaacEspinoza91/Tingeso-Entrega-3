package com.kartingrm.cliente_desc_frecu_service.dto;

import com.kartingrm.cliente_desc_frecu_service.modelbase.ClienteBase;

import java.time.LocalDate;

public class ClienteDTO extends ClienteBase {

    public ClienteDTO(){}

    public ClienteDTO(String rut, String nombre, String apellido, String correo, String telefono, LocalDate fechaNacimiento, boolean activo) {
        this.rut = rut;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = activo;
    }
}
