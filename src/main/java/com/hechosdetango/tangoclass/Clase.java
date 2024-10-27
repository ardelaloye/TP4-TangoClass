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

public class Clase {
    private int idClase;              // IDClase (Primary Key)
    private int idTaller;             // IDTaller (Foreign Key)
    private Date claFechaClase;       // Fecha de la clase
    private String claTema;           // Tema de la clase (Titulo)
    private String claObservaciones;  // Observaciones de la clase (Un detalle del tema por ejemplo)
    private String claEstado;         // Estado de la clase (Planificada, Realizada, Cancelada, Postergada)

    // Constructor vacío
    public Clase() {}

    // Constructor con parámetros
    public Clase(int idClase, int idTaller, Date claFechaClase, String claTema,
                 String claObservaciones, String claEstado) {
        this.idClase = idClase;
        this.idTaller = idTaller;
        this.claFechaClase = claFechaClase;
        this.claTema = claTema;
        this.claObservaciones = claObservaciones;
        this.claEstado = claEstado;
    }

    // Falta desarrollar: Getters y Setters ------->
}
