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

public class Asistencia {
    private int idAsistencia;         // (Primary Key)
    private int idClase;              // IDClase (Foreign Key)
    private int idAlumno;             // IDAlumno (Foreign Key)
    private boolean asiPresente;      // Si el alumno estuvo presente = 'S', si no 'N'
    private boolean asiLlegoTarde;    // Si el alumno llegó tarde = 'S', si no 'N'
    private boolean asiSalioAntes;    // Si el alumno se retiró antes = 'S' si no 'N'

    // Constructor vacío
    public Asistencia() {}

    // Constructor con parámetros
    public Asistencia(int idAsistencia, int idClase, int idAlumno, boolean asiPresente,
                      boolean asiLlegoTarde, boolean asiSalioAntes) {
        this.idAsistencia = idAsistencia;
        this.idClase = idClase;
        this.idAlumno = idAlumno;
        this.asiPresente = asiPresente;
        this.asiLlegoTarde = asiLlegoTarde;
        this.asiSalioAntes = asiSalioAntes;
    }

    // En desarrollo: Getters y Setters ---------------------------->

}
