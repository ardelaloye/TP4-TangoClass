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

public class ValoresTaller {
    private IntegerProperty idValoresTaller; // Primary Key
    private IntegerProperty idTaller;
    private ObjectProperty<Date> valFechaDesde;
    private ObjectProperty<Date> valFechaHasta;
    private DoubleProperty valImporte;

    // Constructor vacío
    public ValoresTaller() {
        this.idValoresTaller = new SimpleIntegerProperty();
        this.idTaller = new SimpleIntegerProperty();
        this.valFechaDesde = new SimpleObjectProperty<>();
        this.valFechaHasta = new SimpleObjectProperty<>();
        this.valImporte = new SimpleDoubleProperty();
    }

    // Getters y Setters ---------->
    public int getIdValoresTaller() {return idValoresTaller.get();}
    public void setIdValoresTaller(int idValoresTaller) {this.idValoresTaller.set(idValoresTaller);}
    public IntegerProperty idValoresTallerProperty() {return idValoresTaller;}

    public int getIdTaller() {return idTaller.get();}
    public void setIdTaller(int idTaller) {this.idTaller.set(idTaller);}
    public IntegerProperty idTallerProperty() {return idTaller;}

    public Date getValFechaDesde() {return valFechaDesde.get();}
    public void setValFechaDesde(Date valFechaDesde) {this.valFechaDesde.set(valFechaDesde);}
    public ObjectProperty<Date> valFechaDesdeProperty() {return valFechaDesde;}


    public Date getValFechaHasta() {return valFechaHasta.get();}
    public void setValFechaHasta(Date valFechaHasta) {this.valFechaHasta.set(valFechaHasta);}
    public ObjectProperty<Date> valFechaHastaProperty() {return valFechaHasta;}

    public double getValImporte() {return valImporte.get();}
    public void setValImporte(double valImporte) {this.valImporte.set(valImporte);}
    public DoubleProperty valImporteProperty() {return valImporte;}

    public double obtenerCostoEnFecha(int idTaller, Date fecha) {
        // Verificamos si la fecha pasada por parametro está dentro del rango.
        // Si es así devolvemos el costo/valor del taller en esa fecha
        if (fecha != null && fecha.compareTo(getValFechaDesde()) >= 0 && fecha.compareTo(getValFechaHasta()) <= 0
                && getIdTaller() == idTaller) {
            return getValImporte();
        }
        return -1; // -1 indica que no se encontró un valor válido
    }
}
