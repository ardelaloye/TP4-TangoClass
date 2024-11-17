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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClaseDAO {

    // Constructor de la clase
    public ClaseDAO() {}

    private boolean conexionAbierta() {
        try {
            // Si la conexión no está abierta, intentamos abrirla
            if (Conexion.getConexion() == null || Conexion.getConexion().isClosed()) {
                if (!Conexion.conectar()) {
                    System.out.println("No es posible conectarse a la base de datos");
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Devuelve todas las clases activas
    public List<Clase> obtenerClasesActivas() {
        List<Clase> listaClases = new ArrayList<>();
        if (!conexionAbierta()) { return listaClases; }

        String query = "SELECT * FROM Clase WHERE ClaEstado = 'Activo'";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Clase clase = new Clase();
                clase.setIdClase(rs.getInt("IDClase"));
                clase.setIdTaller(rs.getInt("IDTaller"));
                clase.setClaFechaClase(rs.getDate("ClaFechaClase"));
                clase.setClaTema(rs.getString("ClaTema"));
                clase.setClaObservaciones(rs.getString("ClaObservaciones"));
                clase.setClaEstado(rs.getString("ClaEstado"));

                // Obtener el nombre del taller usando el idTaller
                String talNombre = TallerDAO.getTalNombrePorId(clase.getIdTaller());
                //System.out.println("Clase: " + clase.getClaTema() + " - Taller: " + talNombre);

                // Agregar la clase a la lista
                listaClases.add(clase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaClases;
    }


    // Agregar una nueva clase
    public boolean agregarClase(Clase clase) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "INSERT INTO Clase (IDTaller, ClaFechaClase, ClaTema, ClaObservaciones, ClaEstado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, clase.getIdTaller());
            pstmt.setDate(2, new java.sql.Date(clase.getClaFechaClase().getTime()));
            pstmt.setString(3, clase.getClaTema());
            pstmt.setString(4, clase.getClaObservaciones());
            pstmt.setString(5, clase.getClaEstado());
            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar una clase
    public boolean editarClase(Clase clase) {
        if (!Conexion.conectar()) {
            return false;
        }

        String query = "UPDATE Clase SET IDTaller = ?, ClaFechaClase = ?, ClaTema = ?, ClaObservaciones = ?, ClaEstado = ? WHERE IDClase = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, clase.getIdTaller());
            pstmt.setDate(2, new java.sql.Date(clase.getClaFechaClase().getTime()));
            pstmt.setString(3, clase.getClaTema());
            pstmt.setString(4, clase.getClaObservaciones());
            pstmt.setString(5, clase.getClaEstado());
            pstmt.setInt(6, clase.getIdClase()); // ID de la clase a editar
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Eliminar una clase (cambia el estado a "Eliminada")
    public boolean eliminarClase(int idClase) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "UPDATE Clase SET ClaEstado = ? WHERE IDClase = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, "Eliminada");
            pstmt.setInt(2, idClase);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
