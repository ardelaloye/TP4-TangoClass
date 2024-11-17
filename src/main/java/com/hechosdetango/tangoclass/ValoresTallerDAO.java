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

public class ValoresTallerDAO {

    // devuelve el valor del taller en una fecha específica
    public ValoresTaller obtenerValorTaller(int idTaller, Date fecha) {
        String sql = "SELECT * FROM ValoresTaller WHERE idTaller = ? AND ? BETWEEN valFechaDesde AND valFechaHasta";
        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(sql)) {
            stmt.setInt(1, idTaller);
            stmt.setDate(2, fecha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ValoresTaller valoresTaller = new ValoresTaller();
                    valoresTaller.setIdValoresTaller(rs.getInt("idValoresTaller"));
                    valoresTaller.setIdTaller(rs.getInt("idTaller"));
                    valoresTaller.setValFechaDesde(rs.getDate("valFechaDesde"));
                    valoresTaller.setValFechaHasta(rs.getDate("valFechaHasta"));
                    valoresTaller.setValImporte(rs.getDouble("valImporte"));
                    return valoresTaller;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // si no se encuentra el valor
    }

    // Devuelve todos los valores de un taller
    public List<ValoresTaller> obtenerValoresTaller(int idTaller) {
        String sql = "SELECT * FROM ValoresTaller WHERE idTaller = ?";
        List<ValoresTaller> valoresTallerList = new ArrayList<>();
        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(sql)) {
            stmt.setInt(1, idTaller);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ValoresTaller valoresTaller = new ValoresTaller();
                    valoresTaller.setIdValoresTaller(rs.getInt("idValoresTaller"));
                    valoresTaller.setIdTaller(rs.getInt("idTaller"));
                    valoresTaller.setValFechaDesde(rs.getDate("valFechaDesde"));
                    valoresTaller.setValFechaHasta(rs.getDate("valFechaHasta"));
                    valoresTaller.setValImporte(rs.getDouble("valImporte"));
                    valoresTallerList.add(valoresTaller);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valoresTallerList;
    }

    // agregar un nuevo valor de taller
    public boolean agregarValorTaller(ValoresTaller valoresTaller) {
        String sql = "INSERT INTO ValoresTaller (idTaller, valFechaDesde, valFechaHasta, valImporte) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(sql)) {
            stmt.setInt(1, valoresTaller.getIdTaller());
            stmt.setDate(2, new java.sql.Date(valoresTaller.getValFechaDesde().getTime()));
            stmt.setDate(3, new java.sql.Date(valoresTaller.getValFechaHasta().getTime()));
            stmt.setDouble(4, valoresTaller.getValImporte());

            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Actualizar un valor de taller existente
    public boolean actualizarValorTaller(ValoresTaller valoresTaller) {
        String sql = "UPDATE ValoresTaller SET valFechaDesde = ?, valFechaHasta = ?, valImporte = ? WHERE idValoresTaller = ?";
        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(valoresTaller.getValFechaDesde().getTime()));
            stmt.setDate(2, new java.sql.Date(valoresTaller.getValFechaHasta().getTime()));
            stmt.setDouble(3, valoresTaller.getValImporte());
            stmt.setInt(4, valoresTaller.getIdValoresTaller());

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Eliminar un valor de taller
    public boolean eliminarValorTaller(int idValoresTaller) {
        String sql = "DELETE FROM ValoresTaller WHERE idValoresTaller = ?";
        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(sql)) {
            stmt.setInt(1, idValoresTaller);

            int filasEliminadas = stmt.executeUpdate();
            return filasEliminadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
