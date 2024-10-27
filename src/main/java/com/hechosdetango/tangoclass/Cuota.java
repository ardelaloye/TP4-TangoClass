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

import java.util.Date;

public class Cuota {
    private int idInscripcion;        // IDInscripcion (Foreign Key) (PK junto con cuoPeriodo)
    private String cuoPeriodo;        // Periodo de la cuota (PK junto con idInscripcion) - Formato: YYYYMM
    private Date cuoFechaEmision;     // Fecha en la que se emitió la cuota
    private double cuoImporte;        // Importe de la cuota
    private Date cuoFechaVencimiento; // Fecha de vencimiento de la cuota
    private Date cuoFechaPago;        // Fecha en la que se pagó la cuota (si está paga)
    private String cuoEstado;         // Estado de la cuota (Pendiente, Pagada, Eliminada)

    // Constructor vacío
    public Cuota() {}

    // Constructor con parámetros
    public Cuota(int idInscripcion, String cuoPeriodo, Date cuoFechaEmision, double cuoImporte,
                 Date cuoFechaVencimiento, Date cuoFechaPago, String cuoEstado) {
        this.idInscripcion = idInscripcion;
        this.cuoPeriodo = cuoPeriodo;
        this.cuoFechaEmision = cuoFechaEmision;
        this.cuoImporte = cuoImporte;
        this.cuoFechaVencimiento = cuoFechaVencimiento;
        this.cuoFechaPago = cuoFechaPago;
        this.cuoEstado = cuoEstado;
    }

    // Falta desarrollar: Getters y Setters --------->
}
