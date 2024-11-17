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

public class Liquidacion {
    private IntegerProperty idLiquidacion;
    private IntegerProperty idTaller;
    private DoubleProperty liqIngresosTaller;
    private DoubleProperty liqComisionDocente;
    private DoubleProperty liqImporteLiquidar;
    private ObjectProperty<Date> liqFechaPago;
    private StringProperty liqEstado;

    // Constructor
    public Liquidacion() {
        this.idLiquidacion = new SimpleIntegerProperty();
        this.idTaller = new SimpleIntegerProperty();
        this.liqIngresosTaller = new SimpleDoubleProperty();
        this.liqComisionDocente = new SimpleDoubleProperty();
        this.liqImporteLiquidar = new SimpleDoubleProperty();
        this.liqFechaPago = new SimpleObjectProperty<>();
        this.liqEstado = new SimpleStringProperty();
    }

    // Getters y Setters para las propiedades

    public IntegerProperty idLiquidacionProperty() {return idLiquidacion;}
    public int getIdLiquidacion() {return idLiquidacion.get();}
    public void setIdLiquidacion(int idLiquidacion) {this.idLiquidacion.set(idLiquidacion);}


    public IntegerProperty idTallerProperty() {return idTaller;}
    public int getIdTaller() {return idTaller.get();}
    public void setIdTaller(int idTaller) {this.idTaller.set(idTaller);}


    public DoubleProperty liqIngresosTallerProperty() {return liqIngresosTaller;}
    public double getLiqIngresosTaller() {return liqIngresosTaller.get();}
    public void setLiqIngresosTaller(double liqIngresosTaller) {this.liqIngresosTaller.set(liqIngresosTaller);}


    public DoubleProperty liqComisionDocenteProperty() {return liqComisionDocente;}
    public double getLiqComisionDocente() {return liqComisionDocente.get();}
    public void setLiqComisionDocente(double liqComisionDocente) {this.liqComisionDocente.set(liqComisionDocente);}


    public DoubleProperty liqImporteLiquidarProperty() {return liqImporteLiquidar;}
    public double getLiqImporteLiquidar() {return liqImporteLiquidar.get();}
    public void setLiqImporteLiquidar(double liqImporteLiquidar) {this.liqImporteLiquidar.set(liqImporteLiquidar);}

    public ObjectProperty<Date> liqFechaPagoProperty() {return liqFechaPago;}
    public Date getLiqFechaPago() {return liqFechaPago.get();}
    public void setLiqFechaPago(Date liqFechaPago) {this.liqFechaPago.set(liqFechaPago);}


    public StringProperty liqEstadoProperty() {return liqEstado;}
    public String getLiqEstado() {return liqEstado.get();}
    public void setLiqEstado(String liqEstado) {this.liqEstado.set(liqEstado);}
}
