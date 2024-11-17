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

public class LiquidacionDAO {

    // Constructor vacío
    public LiquidacionDAO() {}

    // Verifica si la conexión está abierta, y la abre si no es así
    private boolean conexionAbierta() {
        try {
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

    // Obtener todas las liquidaciones
    public List<Liquidacion> obtenerTodasLiquidaciones() {
        List<Liquidacion> listaLiquidaciones = new ArrayList<>();
        if (!conexionAbierta()) {
            return listaLiquidaciones;
        }

        String query = "SELECT * FROM Liquidacion";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Liquidacion liquidacion = new Liquidacion();
                liquidacion.setIdLiquidacion(rs.getInt("idLiquidacion"));
                liquidacion.setIdTaller(rs.getInt("idTaller"));
                liquidacion.setLiqIngresosTaller(rs.getDouble("liqIngresosTaller"));
                liquidacion.setLiqComisionDocente(rs.getDouble("liqComisionDocente"));
                liquidacion.setLiqImporteLiquidar(rs.getDouble("liqImporteLiquidar"));
                liquidacion.setLiqFechaPago(rs.getDate("liqFechaPago"));
                liquidacion.setLiqEstado(rs.getString("liqEstado"));
                listaLiquidaciones.add(liquidacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaLiquidaciones;
    }

    public List<Liquidacion> obtenerLiquidacionesPorTaller(int idTaller) {
        List<Liquidacion> listaLiquidaciones = new ArrayList<>();
        if (!conexionAbierta()) {
            return listaLiquidaciones;
        }

        String query = "SELECT * FROM Liquidacion WHERE idTaller = ?";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            stmt.setInt(1, idTaller);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Liquidacion liquidacion = new Liquidacion();
                liquidacion.setIdLiquidacion(rs.getInt("idLiquidacion"));
                liquidacion.setIdTaller(rs.getInt("idTaller"));
                liquidacion.setLiqIngresosTaller(rs.getDouble("liqIngresosTaller"));
                liquidacion.setLiqComisionDocente(rs.getDouble("liqComisionDocente"));
                liquidacion.setLiqImporteLiquidar(rs.getDouble("liqImporteLiquidar"));
                liquidacion.setLiqFechaPago(rs.getDate("liqFechaPago"));
                liquidacion.setLiqEstado(rs.getString("liqEstado"));
                listaLiquidaciones.add(liquidacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaLiquidaciones;
    }

    // Agregar una nueva liquidación
    public static void agregarLiquidacion(Liquidacion liquidacion) throws SQLException {
        String query = "INSERT INTO Liquidacion (idLiquidacion, idTaller, liqIngresosTaller, liqComisionDocente, liqImporteLiquidar, liqFechaPago, liqEstado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, liquidacion.getIdLiquidacion());
            pstmt.setInt(2, liquidacion.getIdTaller());
            pstmt.setDouble(3, liquidacion.getLiqIngresosTaller());
            pstmt.setDouble(4, liquidacion.getLiqComisionDocente());
            pstmt.setDouble(5, liquidacion.getLiqImporteLiquidar());
            if (liquidacion.getLiqFechaPago() != null) {
                pstmt.setDate(6, new java.sql.Date(liquidacion.getLiqFechaPago().getTime()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            pstmt.setString(7, liquidacion.getLiqEstado());

            pstmt.executeUpdate();
        }
    }
}
