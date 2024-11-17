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

public class Cuota {
    private IntegerProperty idInscripcion;            // IDInscripcion (Foreign Key) (PK junto con cuoPeriodo)
    private IntegerProperty cuoPeriodo;               // Periodo de la cuota (PK junto con idInscripcion) - Formato: YYYYMM
    private ObjectProperty<Date> cuoFechaEmision;     // Fecha en la que se emitió la cuota
    private DoubleProperty cuoImporte;                // Importe de la cuota
    private ObjectProperty<Date> cuoFechaVencimiento; // Fecha de vencimiento de la cuota
    private ObjectProperty<Date> cuoFechaPago;        // Fecha en la que se pagó la cuota (si está paga)
    private SimpleStringProperty cuoEstado;           // Estado de la cuota (Pendiente, Pagada, Eliminada)
    private IntegerProperty idLiquidacion;            // ID de la Liquidación (Foreign Key a la tabla Liquidacion)

    // Constructor vacío
    public Cuota() {
        this.idInscripcion = new SimpleIntegerProperty();
        this.cuoPeriodo = new SimpleIntegerProperty();
        this.cuoFechaEmision = new SimpleObjectProperty<>();
        this.cuoImporte = new SimpleDoubleProperty();
        this.cuoFechaVencimiento = new SimpleObjectProperty<>();
        this.cuoFechaPago = new SimpleObjectProperty<>();
        this.cuoEstado = new SimpleStringProperty();
        this.idLiquidacion = new SimpleIntegerProperty(); // Inicialización de la propiedad
    }

    // Constructor con parámetros
    public Cuota(int idInscripcion, int cuoPeriodo, Date cuoFechaEmision, double cuoImporte,
                 Date cuoFechaVencimiento, Date cuoFechaPago, String cuoEstado, int idLiquidacion) {
        this();
        setIdInscripcion(idInscripcion);
        setCuoPeriodo(cuoPeriodo);
        setCuoFechaEmision(cuoFechaEmision);
        setCuoImporte(cuoImporte);
        setCuoFechaVencimiento(cuoFechaVencimiento);
        setCuoFechaPago(cuoFechaPago);
        setCuoEstado(cuoEstado);
        setIdLiquidacion(idLiquidacion);
    }

    // Getters y Setters
    public int getIdInscripcion() { return idInscripcion.get(); }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion.set(idInscripcion); }
    public IntegerProperty idInscripcionProperty() { return idInscripcion; }

    public int getCuoPeriodo() { return cuoPeriodo.get(); }
    public void setCuoPeriodo(int cuoPeriodo) { this.cuoPeriodo.set(cuoPeriodo); }
    public IntegerProperty cuoPeriodoProperty() { return cuoPeriodo; }

    public Date getCuoFechaEmision() { return cuoFechaEmision.get(); }
    public void setCuoFechaEmision(Date cuoFechaEmision) { this.cuoFechaEmision.set(cuoFechaEmision); }
    public ObjectProperty<Date> cuoFechaEmisionProperty() { return cuoFechaEmision; }

    public double getCuoImporte() { return cuoImporte.get(); }
    public void setCuoImporte(double cuoImporte) { this.cuoImporte.set(cuoImporte); }
    public DoubleProperty cuoImporteProperty() { return cuoImporte; }

    public Date getCuoFechaVencimiento() { return cuoFechaVencimiento.get(); }
    public void setCuoFechaVencimiento(Date cuoFechaVencimiento) { this.cuoFechaVencimiento.set(cuoFechaVencimiento); }
    public ObjectProperty<Date> cuoFechaVencimientoProperty() { return cuoFechaVencimiento; }

    public Date getCuoFechaPago() { return cuoFechaPago.get(); }
    public void setCuoFechaPago(Date cuoFechaPago) { this.cuoFechaPago.set(cuoFechaPago); }
    public ObjectProperty<Date> cuoFechaPagoProperty() { return cuoFechaPago; }

    public String getCuoEstado() { return cuoEstado.get(); }
    public void setCuoEstado(String cuoEstado) { this.cuoEstado.set(cuoEstado); }
    public StringProperty cuoEstadoProperty() { return cuoEstado; }

    public int getIdLiquidacion() { return idLiquidacion.get(); }
    public void setIdLiquidacion(int idLiquidacion) { this.idLiquidacion.set(idLiquidacion); }
    public IntegerProperty idLiquidacionProperty() { return idLiquidacion; }
}
