package com.kartingrm.cliente_desc_frecu_service.modelbase;

import javax.persistence.Column;
import java.time.LocalDate;

public abstract class ClienteBase {

    @Column(name = "rut", nullable = true, length = 12, unique = true)
    protected String rut;

    @Column(name = "nombre", length = 50)
    protected String nombre;

    @Column(name = "apellido", length = 50)
    protected String apellido;

    @Column(name = "correo", length = 50, unique = true)
    protected String correo;

    @Column(name = "telefono", length = 12)
    protected String telefono;

    @Column(name = "fecha_nacimiento", nullable = false)
    protected LocalDate fechaNacimiento;

    @Column(name = "activo")
    protected boolean activo = true;  // En caso de omitir este valor, es default true


    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
