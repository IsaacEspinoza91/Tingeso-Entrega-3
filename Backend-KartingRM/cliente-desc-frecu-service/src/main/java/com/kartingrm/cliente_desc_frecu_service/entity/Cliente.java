package com.kartingrm.cliente_desc_frecu_service.entity;

import com.kartingrm.cliente_desc_frecu_service.modelbase.ClienteBase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
public class Cliente extends ClienteBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Contructores
    public Cliente() {
    }

    public Cliente(String rut, String nombre, String apellido, String correo, String telefono, LocalDate fechaNacimiento, boolean activo) {
        this.rut = rut;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = activo;
    }


    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
