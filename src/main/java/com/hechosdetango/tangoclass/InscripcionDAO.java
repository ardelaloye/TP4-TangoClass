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

public class InscripcionDAO {

    // Constructor de la clase
    public InscripcionDAO() {
    }

    private static boolean conexionAbierta() {
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

    // Devuelve todas las inscripciones activas
    public List<Inscripcion> obtenerInscripcionesActivas() {
        List<Inscripcion> listaInscripciones = new ArrayList<>();
        if (!conexionAbierta()) {
            return listaInscripciones;
        }

        String query = "SELECT * FROM Inscripcion WHERE InsEstado='Activo'";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setIdInscripcion(rs.getInt("IDInscripcion"));
                inscripcion.setIdAlumno(rs.getInt("IDAlumno"));
                inscripcion.setIdTaller(rs.getInt("IDTaller"));
                inscripcion.setInsFechaInscripcion(rs.getDate("InsFechaInscripcion"));
                inscripcion.setInsFechaBaja(rs.getDate("InsFechaBaja"));
                inscripcion.setInsEstado(rs.getString("InsEstado"));
                listaInscripciones.add(inscripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaInscripciones;
    }

    // Método para obtener una inscripción a partir de su ID
    public Inscripcion obtenerInscripcionPorId(int idInscripcion) {
        if (!conexionAbierta()) {
            return null;
        }

        String query = "SELECT * FROM Inscripcion WHERE IDInscripcion = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, idInscripcion);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setIdInscripcion(rs.getInt("IDInscripcion"));
                inscripcion.setIdAlumno(rs.getInt("IDAlumno"));
                inscripcion.setIdTaller(rs.getInt("IDTaller"));
                inscripcion.setInsFechaInscripcion(rs.getDate("InsFechaInscripcion"));
                inscripcion.setInsFechaBaja(rs.getDate("InsFechaBaja"));
                inscripcion.setInsEstado(rs.getString("InsEstado"));
                return inscripcion;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // alta de  una nueva inscripción
    public boolean agregarInscripcion(Inscripcion inscripcion) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "INSERT INTO Inscripcion (IDAlumno, IDTaller, InsFechaInscripcion, InsFechaBaja, InsEstado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, inscripcion.getIdAlumno());
            pstmt.setInt(2, inscripcion.getIdTaller());
            pstmt.setDate(3, new java.sql.Date(inscripcion.getInsFechaInscripcion().getTime()));
            if (inscripcion.getInsFechaBaja() != null) {
                pstmt.setDate(4, new java.sql.Date(inscripcion.getInsFechaBaja().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            pstmt.setString(5, inscripcion.getInsEstado());
            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar una inscripción
    public static boolean editarInscripcion(Inscripcion inscripcion) {
        if (!Conexion.conectar()) {
            return false;
        }

        String query = "UPDATE Inscripcion SET IDAlumno = ?, IDTaller = ?, InsFechaInscripcion = ?, InsFechaBaja = ?, InsEstado = ? WHERE IDInscripcion = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, inscripcion.getIdAlumno());
            pstmt.setInt(2, inscripcion.getIdTaller());
            pstmt.setDate(3, new java.sql.Date(inscripcion.getInsFechaInscripcion().getTime()));
            if (inscripcion.getInsFechaBaja() != null) {
                pstmt.setDate(4, new java.sql.Date(inscripcion.getInsFechaBaja().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            pstmt.setString(5, inscripcion.getInsEstado());
            pstmt.setInt(6, inscripcion.getIdInscripcion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Eliminar una inscripción (solo cambiamos su estado a "Eliminado")
    public boolean eliminarInscripcion(int idInscripcion) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "UPDATE Inscripcion SET InsEstado = ? WHERE IDInscripcion = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, "Eliminado");
            pstmt.setInt(2, idInscripcion);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerIdTallerInscripcion(int idInscripcion) {
        if (!conexionAbierta()) {
            return -1;
        }

        String query = "SELECT IDTaller FROM Inscripcion WHERE IDInscripcion = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, idInscripcion);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("IDTaller");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int obtenerIdAlumnoInscripcion(int idInscripcion) {
        if (!conexionAbierta()) {
            return -1;
        }

        String query = "SELECT IDAlumno FROM Inscripcion WHERE IDInscripcion = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, idInscripcion);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("IDAlumno");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Inscripcion> obtenerInscripcionesPorTaller(int idTaller) {
        List<Inscripcion> listaInscripciones = new ArrayList<>();
        if (!conexionAbierta()) {
            return listaInscripciones;
        }

        String query = "SELECT * FROM Inscripcion WHERE IDTaller = ? AND InsEstado = 'Activo'";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            stmt.setInt(1, idTaller);  // Establecemos el ID del taller como parámetro de la consulta
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setIdInscripcion(rs.getInt("IDInscripcion"));
                inscripcion.setIdAlumno(rs.getInt("IDAlumno"));
                inscripcion.setIdTaller(rs.getInt("IDTaller"));
                inscripcion.setInsFechaInscripcion(rs.getDate("InsFechaInscripcion"));
                inscripcion.setInsFechaBaja(rs.getDate("InsFechaBaja"));
                inscripcion.setInsEstado(rs.getString("InsEstado"));
                listaInscripciones.add(inscripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaInscripciones;
    }


}
