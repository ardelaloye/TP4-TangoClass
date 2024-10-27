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

public class Liquidacion {
    private int idLiquidacion;            // IDLiquidacion (Primary Key)
    private int idTaller;                 // ID del taller que se liquida
    private double liqIngresosTaller;     // Ingresos generados por el taller en el periodo a liquidar
    private double liqComisionDocente;    // Comisión a pagarle al docente
    private double liqImporteLiquidar;    // Importe final a liquidar
    private Date liqFechaPago;            // Fecha en que se pago el importe de la liquidacion
    private String liqEstado;             // Estado de la liquidación (pendiente, pagada)

    // Constructor vacío
    public Liquidacion() {}

    // Constructor con parámetros
    public Liquidacion(int idLiquidacion, int idTaller, double liqIngresosTaller,
                       double liqComisionDocente, double liqImporteLiquidar, Date liqFechaPago,
                       String liqEstado) {
        this.idLiquidacion = idLiquidacion;
        this.idTaller = idTaller;
        this.liqIngresosTaller = liqIngresosTaller;
        this.liqComisionDocente = liqComisionDocente;
        this.liqImporteLiquidar = liqImporteLiquidar;
        this.liqFechaPago = liqFechaPago;
        this.liqEstado = liqEstado;
    }

    // Falta desarrollar: Getters y Setters ------->
}
