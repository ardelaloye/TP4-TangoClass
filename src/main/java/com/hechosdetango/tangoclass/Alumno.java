/* ********************************************************************************************************
        Universidad Siglo 21
        Seminario de Práctica de Informática
        Trabajo Práctico 3

        Proyecto: T A N G O C L A S S
        Alumno:   Ariel Delaloye
        Legajo:   VINF011381
        Octubre de 2024
******************************************************************************************************** */
package com.hechosdetango.tangoclass;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Alumno {
    private int idAlumno; // IDAlumno (Primary Key)
    private SimpleStringProperty aluApellido;
    private SimpleStringProperty aluNombre;
    private SimpleStringProperty aluDNI;
    private ObjectProperty<Date> aluFechaNacimiento;
    private SimpleStringProperty aluDomicilio;
    private SimpleStringProperty aluTelefono;
    private SimpleStringProperty aluEmail;
    private SimpleStringProperty aluEstado;

    // Constructor vacío
    public Alumno() {
        this.aluApellido = new SimpleStringProperty();
        this.aluNombre = new SimpleStringProperty();
        this.aluDNI = new SimpleStringProperty();
        this.aluFechaNacimiento = new SimpleObjectProperty<>();
        this.aluDomicilio = new SimpleStringProperty();
        this.aluTelefono = new SimpleStringProperty();
        this.aluEmail = new SimpleStringProperty();
        this.aluEstado = new SimpleStringProperty();
    }

    // Getters y Setters
    public int getIdAlumno() {
        return idAlumno;
    }
    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }


    public String getAluApellido() {
        return aluApellido.get();
    }
    public void setAluApellido(String aluApellido) {
        this.aluApellido.set(aluApellido);
    }
    public StringProperty aluApellidoProperty() {
        return aluApellido;
    }

    public String getAluNombre() {
        return aluNombre.get();
    }
    public void setAluNombre(String aluNombre) {
        this.aluNombre.set(aluNombre);
    }
    public StringProperty aluNombreProperty() {
        return aluNombre;
    }

    public String getAluDNI() {
        return aluDNI.get();
    }
    public void setAluDNI(String aluDNI) {
        this.aluDNI.set(aluDNI);
    }
    public StringProperty aluDNIProperty() {
        return aluDNI;
    }

    public Date getAluFechaNacimiento() {
        return aluFechaNacimiento.get();
    }
    public void setAluFechaNacimiento(Date aluFechaNacimiento) {
        this.aluFechaNacimiento.set(aluFechaNacimiento);
    }
    public ObjectProperty<Date> aluFechaNacimientoProperty() {
        return aluFechaNacimiento;
    }

    public String getAluDomicilio() {
        return aluDomicilio.get();
    }
    public void setAluDomicilio(String aluDomicilio) {
        this.aluDomicilio.set(aluDomicilio);
    }
    public StringProperty aluDomicilioProperty() {
        return aluDomicilio;
    }

    public String getAluTelefono() {
        return aluTelefono.get();
    }
    public void setAluTelefono(String aluTelefono) {
        this.aluTelefono.set(aluTelefono);
    }
    public StringProperty aluTelefonoProperty() {
        return aluTelefono;
    }

    public String getAluEmail() {
        return aluEmail.get();
    }
    public void setAluEmail(String aluEmail) {
        this.aluEmail.set(aluEmail);
    }
    public StringProperty aluEmailProperty() {
        return aluEmail;
    }

    public String getAluEstado() {
        return aluEstado.get();
    }
    public void setAluEstado(String aluEstado) {
        this.aluEstado.set(aluEstado);
    }
    public StringProperty aluEstadoProperty() {
        return aluEstado;
    }

}
