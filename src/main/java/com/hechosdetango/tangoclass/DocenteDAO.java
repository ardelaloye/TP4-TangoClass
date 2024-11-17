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

public class DocenteDAO {

    // Constructor de la clase
    public DocenteDAO() {}

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

    // Devuelve todos los docentes activos
    public List<Docente> obtenerDocentesActivos() {
        List<Docente> listaDocentes = new ArrayList<>();
        if (!conexionAbierta()) { return listaDocentes; }

        String query = "SELECT * FROM Docente WHERE DocEstado='Activo'";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Docente docente = new Docente();
                docente.setIdDocente(rs.getInt("IDDocente"));
                docente.setDocApellido(rs.getString("DocApellido"));
                docente.setDocNombre(rs.getString("DocNombre"));
                docente.setDocDNI(rs.getString("DocDNI"));
                docente.setDocTelefono(rs.getString("DocTelefono"));
                docente.setDocEmail(rs.getString("DocEmail"));
                docente.setDocEstado(rs.getString("DocEstado"));
                listaDocentes.add(docente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaDocentes;
    }

    // Dar de alta un nuevo docente
    public boolean agregarDocente(Docente docente) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "INSERT INTO Docente (DocApellido, DocNombre, DocDNI, DocFechaNacimiento, DocDomicilio, DocTelefono, DocEmail, DocEstado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, docente.getDocApellido());
            pstmt.setString(2, docente.getDocNombre());
            pstmt.setString(3, docente.getDocDNI());
            pstmt.setString(6, docente.getDocTelefono());
            pstmt.setString(7, docente.getDocEmail());
            pstmt.setString(8, docente.getDocEstado());
            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar los datos de un docente
    public boolean editarDocente(Docente docente) {
        if (!Conexion.conectar()) {
            return false;
        }

        String query = "UPDATE Docente SET DocApellido = ?, DocNombre = ?, DocDNI = ?, DocTelefono = ?, DocEmail = ?, DocEstado = ? WHERE IDDocente = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, docente.getDocApellido());
            pstmt.setString(2, docente.getDocNombre());
            pstmt.setString(3, docente.getDocDNI());
            pstmt.setString(4, docente.getDocTelefono());
            pstmt.setString(5, docente.getDocEmail());
            pstmt.setString(6, docente.getDocEstado());
            pstmt.setInt(7, docente.getIdDocente());

            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Eliminar un docente
    public boolean eliminarDocente(int idDocente) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "UPDATE Docente SET DocEstado = ? WHERE IDDocente = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, "Eliminado");
            pstmt.setInt(2, idDocente);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
