/* ********************************************************************************************************
        Universidad Siglo 21
        Seminario de Práctica de Informática
        Trabajo Práctico 4

        Proyecto: T A N G O C L A S S
        Alumno:   Ariel Delaloye
        Legajo:   VINF011381
        Noviembre de 2024
******************************************************************************************************** */
package com.hechosdetango.tangoclass;

import javafx.beans.property.*;
import java.util.Date;

public class Clase {
    private int idClase; // Primary Key
    private SimpleIntegerProperty idTaller; // Foreign Key
    private ObjectProperty<Date> claFechaClase;
    private SimpleStringProperty claTema;
    private SimpleStringProperty claObservaciones;
    private SimpleStringProperty claEstado;

    // Constructor vacío
    public Clase() {
        this.idTaller = new SimpleIntegerProperty();
        this.claFechaClase = new SimpleObjectProperty<>();
        this.claTema = new SimpleStringProperty();
        this.claObservaciones = new SimpleStringProperty();
        this.claEstado = new SimpleStringProperty();
    }

    // Getters y Setters
    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getIdTaller() {
        return idTaller.get();
    }

    public void setIdTaller(int idTaller) {
        this.idTaller.set(idTaller);
    }

    public IntegerProperty idTallerProperty() {
        return idTaller;
    }

    public Date getClaFechaClase() {
        return claFechaClase.get();
    }

    public void setClaFechaClase(Date claFechaClase) {
        this.claFechaClase.set(claFechaClase);
    }

    public ObjectProperty<Date> claFechaClaseProperty() {
        return claFechaClase;
    }

    public String getClaTema() {
        return claTema.get();
    }

    public void setClaTema(String claTema) {
        this.claTema.set(claTema);
    }

    public StringProperty claTemaProperty() {
        return claTema;
    }

    public String getClaObservaciones() {
        return claObservaciones.get();
    }

    public void setClaObservaciones(String claObservaciones) {
        this.claObservaciones.set(claObservaciones);
    }

    public StringProperty claObservacionesProperty() {
        return claObservaciones;
    }

    public String getClaEstado() {
        return claEstado.get();
    }

    public void setClaEstado(String claEstado) {
        this.claEstado.set(claEstado);
    }

    public StringProperty claEstadoProperty() {
        return claEstado;
    }
}
