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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Conexion a la base de datos -La BD se encuentra en internet, con acceso remoto-
    private static final String URL = "jdbc:mysql://sql10.freemysqlhosting.net:3306/sql10739862";
    private static final String USUARIO = "sql10739862";
    private static final String CONTRASENA = "8hFhBE4nwh";

    private static Connection conexion;

    // Método para conectar con la base de datos
    public static boolean conectar() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Para cerrar la conexión si es necesario
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Para obtener la conexión
    public static Connection getConexion() {
        return conexion;
    }
}
