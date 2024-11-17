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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TallerDAO {

    // Constructor de la clase
    public TallerDAO() {}

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

    // devuelve todos los talleres activos
    public List<Taller> obtenerTalleresActivos() {
        List<Taller> listaTalleres = new ArrayList<>();
        if (!conexionAbierta()) { return listaTalleres; }

        String query = "SELECT * FROM Taller WHERE TalEstado='Activo'";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Taller taller = new Taller();
                taller.setIdTaller(rs.getInt("IDTaller"));
                taller.setTalNombre(rs.getString("TalNombre"));
                taller.setTalFechaInicio(rs.getDate("TalFechaInicio"));
                taller.setTalFechaCierre(rs.getDate("TalFechaCierre"));
                taller.setTalCupoMaximo(rs.getInt("TalCupoMaximo"));
                taller.setIdDocente(rs.getInt("IDDocente"));
                taller.setTalComisionDocente(rs.getDouble("TalComisionDocente"));
                taller.setTalEstado(rs.getString("TalEstado"));
                listaTalleres.add(taller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTalleres;
    }

    public static Taller obtenerTallerPorId(int idTaller) {
        Taller taller = null;
        String query = "SELECT * FROM Taller WHERE IDTaller = ?";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            stmt.setInt(1, idTaller);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                taller = new Taller();
                taller.setIdTaller(rs.getInt("IDTaller"));
                taller.setTalNombre(rs.getString("TalNombre"));
                taller.setTalFechaInicio(rs.getDate("TalFechaInicio"));
                taller.setTalFechaCierre(rs.getDate("TalFechaCierre"));
                taller.setTalCupoMaximo(rs.getInt("TalCupoMaximo"));
                taller.setIdDocente(rs.getInt("IDDocente"));
                taller.setTalComisionDocente(rs.getDouble("TalComisionDocente"));
                taller.setTalEstado(rs.getString("TalEstado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taller;
    }


    // Dar de alta un nuevo taller
    public boolean agregarTaller(Taller taller) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "INSERT INTO Taller (TalNombre, TalFechaInicio, TalFechaCierre, TalCupoMaximo, IDDocente, TalComisionDocente, TalEstado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, taller.getTalNombre());
            pstmt.setDate(2, new java.sql.Date(taller.getTalFechaInicio().getTime()));
            pstmt.setDate(3, new java.sql.Date(taller.getTalFechaCierre().getTime()));
            pstmt.setInt(4, taller.getTalCupoMaximo());
            pstmt.setInt(5, taller.getIdDocente());
            pstmt.setDouble(6, taller.getTalComisionDocente());
            pstmt.setString(7, taller.getTalEstado());
            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar los datos de un taller
    public boolean editarTaller(Taller taller) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "UPDATE Taller SET TalNombre = ?, TalFechaInicio = ?, TalFechaCierre = ?, TalCupoMaximo = ?, IDDocente = ?, TalComisionDocente = ?, TalEstado = ? WHERE IDTaller = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, taller.getTalNombre());
            pstmt.setDate(2, new java.sql.Date(taller.getTalFechaInicio().getTime()));
            pstmt.setDate(3, new java.sql.Date(taller.getTalFechaCierre().getTime()));
            pstmt.setInt(4, taller.getTalCupoMaximo());
            pstmt.setInt(5, taller.getIdDocente());
            pstmt.setDouble(6, taller.getTalComisionDocente());
            pstmt.setString(7, taller.getTalEstado());
            pstmt.setInt(8, taller.getIdTaller());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Eliminar un taller (cambia estado a 'Eliminado')
    public boolean eliminarTaller(int idTaller) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "UPDATE Taller SET TalEstado = ? WHERE IDTaller = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, "Eliminado");
            pstmt.setInt(2, idTaller);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Devuelve el nombre de un taller a partir de su ID
    public static String getTalNombrePorId(int idTaller) {
        String talNombre = "";
        String query = "SELECT TalNombre FROM Taller WHERE IDTaller = ?";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            stmt.setInt(1, idTaller);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                talNombre = rs.getString("TalNombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return talNombre;
    }
}
