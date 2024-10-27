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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Date;

public class Inscripcion {
    private SimpleIntegerProperty idInscripcion;          // IDInscripcion (Primary Key)
    private SimpleIntegerProperty idAlumno;               // ID del alumno que se inscribe
    private SimpleIntegerProperty idTaller;               // ID del taller donde se inscribe el alumno
    private ObjectProperty<Date> insFechaInscripcion;   // Fecha en la que se realizó la inscripción
    private ObjectProperty<Date> insFechaBaja;          // Si el alumno se da de baja, la fecha de la misma se registra aquí
    private SimpleStringProperty insEstado;               // Estado de la inscripción (activa, cancelada, etc.)

    // Constructor vacío
    public Inscripcion() {
        this.idInscripcion = new SimpleIntegerProperty();
        this.idAlumno = new SimpleIntegerProperty();
        this.idTaller = new SimpleIntegerProperty();
        this.insFechaInscripcion = new SimpleObjectProperty<>();
        this.insFechaBaja = new SimpleObjectProperty<>();
        this.insEstado = new SimpleStringProperty();
    }

    // Constructor con parámetros
    public Inscripcion(int idInscripcion, int idAlumno, int idTaller, Date insFechaInscripcion,
                       Date insFechaBaja, String insEstado) {
        this();
        this.idInscripcion.set(idInscripcion);
        this.idAlumno.set(idAlumno);
        this.idTaller.set(idTaller);
        this.insFechaInscripcion.set(insFechaInscripcion);
        this.insFechaBaja.set(insFechaBaja);
        this.insEstado.set(insEstado);
    }

    // Getters y Setters para cada atributo ------------->
    public int getIdInscripcion() {return idInscripcion.get(); }
    public void setIdInscripcion(int idInscripcion) {this.idInscripcion.set(idInscripcion);}
    public SimpleIntegerProperty idInscripcionProperty() {return idInscripcion;}


    public int getIdAlumno() {return idAlumno.get();}
    public void setIdAlumno(int idAlumno) {this.idAlumno.set(idAlumno);}
    public SimpleIntegerProperty idAlumnoProperty() {return idAlumno;}


    public int getIdTaller() {return idTaller.get();}
    public void setIdTaller(int idTaller) {this.idTaller.set(idTaller);}
    public SimpleIntegerProperty idTallerProperty() {return idTaller;}


    public Date getInsFechaInscripcion() {return insFechaInscripcion.get();}
    public void setInsFechaInscripcion(Date insFechaInscripcion) {this.insFechaInscripcion.set(insFechaInscripcion);}
    public ObjectProperty<Date> insFechaInscripcionProperty() {return insFechaInscripcion;}


    public Date getInsFechaBaja() {return insFechaBaja.get();}
    public void setInsFechaBaja(Date insFechaBaja) {this.insFechaBaja.set(insFechaBaja);}
    public ObjectProperty<Date> insFechaBajaProperty() {return insFechaBaja;}


    public String getInsEstado() {return insEstado.get();}
    public void setInsEstado(String insEstado) {this.insEstado.set(insEstado);}
    public SimpleStringProperty insEstadoProperty() {return insEstado;}
}
