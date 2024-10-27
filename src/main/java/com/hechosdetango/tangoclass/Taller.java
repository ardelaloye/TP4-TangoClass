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

import javafx.beans.property.*;
import java.util.Date;

public class Taller {
    private int idTaller;                // IDTaller (Primary Key)
    private StringProperty talNombre;
    private ObjectProperty<Date> talFechaInicio;
    private ObjectProperty<Date> talFechaCierre;
    private IntegerProperty talCupoMaximo;
    private IntegerProperty idDocente;          // ID del docente que impartirá el taller
    private DoubleProperty talComisionDocente;
    private StringProperty talEstado;

    // Constructor vacío
    public Taller() {
        this.talNombre = new SimpleStringProperty();
        this.talFechaInicio = new SimpleObjectProperty<>();
        this.talFechaCierre = new SimpleObjectProperty<>();
        this.talCupoMaximo = new SimpleIntegerProperty();
        this.idDocente = new SimpleIntegerProperty();
        this.talComisionDocente = new SimpleDoubleProperty();
        this.talEstado = new SimpleStringProperty();
    }

    // Getters y Setters ---->
    public int getIdTaller() {return idTaller;}
    public void setIdTaller(int idTaller) {this.idTaller = idTaller;}

    public String getTalNombre() {return talNombre.get();}
    public void setTalNombre(String talNombre) {this.talNombre.set(talNombre);}
    public StringProperty talNombreProperty() {return talNombre;}

    public Date getTalFechaInicio() {return talFechaInicio.get();}
    public void setTalFechaInicio(Date talFechaInicio) {this.talFechaInicio.set(talFechaInicio);}
    public ObjectProperty<Date> talFechaInicioProperty() {return talFechaInicio;}

    public Date getTalFechaCierre() {return talFechaCierre.get();}
    public void setTalFechaCierre(Date talFechaCierre) {this.talFechaCierre.set(talFechaCierre);}
    public ObjectProperty<Date> talFechaCierreProperty() {return talFechaCierre;}

    public int getTalCupoMaximo() {return talCupoMaximo.get();}
    public void setTalCupoMaximo(int talCupoMaximo) {this.talCupoMaximo.set(talCupoMaximo);}
    public IntegerProperty talCupoMaximoProperty() {return talCupoMaximo;}

    public int getIdDocente() {return idDocente.get();}
    public void setIdDocente(int idDocente) {this.idDocente.set(idDocente);}
    public IntegerProperty idDocenteProperty() {return idDocente;}

    public double getTalComisionDocente() {return talComisionDocente.get();}
    public void setTalComisionDocente(double talComisionDocente) {this.talComisionDocente.set(talComisionDocente);}
    public DoubleProperty talComisionDocenteProperty() {return talComisionDocente;}

    public String getTalEstado() {return talEstado.get();}
    public void setTalEstado(String talEstado) {this.talEstado.set(talEstado);}
    public StringProperty talEstadoProperty() {return talEstado;}
}
