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

public class CuotaDAO {

    // Constructor de la clase
    public CuotaDAO() {}

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

    // Obtener todas las cuotas activas
    public List<Cuota> obtenerCuotasActivas() {
        List<Cuota> listaCuotas = new ArrayList<>();
        if (!conexionAbierta()) {
            return listaCuotas;
        }

        //String query = "SELECT * FROM Cuota WHERE CuoEstado <> 'Eliminado'";
        String query = "SELECT * FROM Cuota WHERE CuoEstado = 'Impaga'";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cuota cuota = new Cuota();
                cuota.setIdInscripcion(rs.getInt("IDInscripcion"));
                cuota.setCuoPeriodo(rs.getInt("CuoPeriodo"));
                cuota.setCuoFechaEmision(rs.getDate("CuoFechaEmision"));
                cuota.setCuoImporte(rs.getDouble("CuoImporte"));
                cuota.setCuoFechaVencimiento(rs.getDate("CuoFechaVencimiento"));
                cuota.setCuoFechaPago(rs.getDate("CuoFechaPago"));
                cuota.setCuoEstado(rs.getString("CuoEstado"));
                listaCuotas.add(cuota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaCuotas;
    }

    public List<Integer> obtenerPeriodos() {
        List<Integer> listaPeriodos = new ArrayList<>();
        if (!conexionAbierta()) { return listaPeriodos; }

        String query = "SELECT DISTINCT cuoPeriodo FROM Cuota ORDER BY cuoPeriodo DESC";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int periodo = rs.getInt("cuoPeriodo");
                listaPeriodos.add(periodo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaPeriodos;
    }


    public List<Cuota> obtenerCuotasPorPeriodo(int periodo) {
        List<Cuota> listaCuotas = new ArrayList<>();
        if (!conexionAbierta()) { return listaCuotas; }

        String query = "SELECT * FROM Cuota WHERE cuoPeriodo = ? AND CuoEstado = 'Impaga' ORDER BY cuoFechaVencimiento";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            stmt.setInt(1, periodo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cuota cuota = new Cuota();
                cuota.setIdInscripcion(rs.getInt("idInscripcion"));
                cuota.setCuoPeriodo(rs.getInt("cuoPeriodo"));
                cuota.setCuoFechaEmision(rs.getDate("cuoFechaEmision"));
                cuota.setCuoImporte(rs.getDouble("cuoImporte"));
                cuota.setCuoFechaVencimiento(rs.getDate("cuoFechaVencimiento"));
                cuota.setCuoFechaPago(rs.getDate("cuoFechaPago"));
                cuota.setCuoEstado(rs.getString("cuoEstado"));
                listaCuotas.add(cuota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaCuotas;
    }



    // Agregar una nueva cuota
    public static void agregarCuota(Cuota cuota) throws SQLException {
        // Primero verificamos si la cuota ya existe
        String checkQuery = "SELECT COUNT(*) FROM Cuota WHERE idInscripcion = ? AND cuoPeriodo = ?";
        try (PreparedStatement checkStmt = Conexion.getConexion().prepareStatement(checkQuery)) {
            checkStmt.setInt(1, cuota.getIdInscripcion());
            checkStmt.setInt(2, cuota.getCuoPeriodo());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                //// Si la cuota ya existe, realiza una actualización
                // ------ Ver esta parte: si ya está paga no deberia modificar nada.
                //String updateQuery = "UPDATE Cuota SET cuoFechaEmision = ?, cuoImporte = ?, cuoFechaVencimiento = ?, cuoFechaPago = ?, cuoEstado = ? " +
                //        "WHERE idInscripcion = ? AND cuoPeriodo = ?";
                //try (PreparedStatement updateStmt = Conexion.getConexion().prepareStatement(updateQuery)) {
                //    updateStmt.setDate(1, new java.sql.Date(cuota.getCuoFechaEmision().getTime()));
                //    updateStmt.setDouble(2, cuota.getCuoImporte());
                //    updateStmt.setDate(3, new java.sql.Date(cuota.getCuoFechaVencimiento().getTime()));
                //    if (cuota.getCuoFechaPago() != null) {
                //        updateStmt.setDate(4, new java.sql.Date(cuota.getCuoFechaPago().getTime()));
                //    } else {
                //        updateStmt.setNull(4, java.sql.Types.DATE);
                //    }
                //    updateStmt.setString(5, cuota.getCuoEstado());
                //    updateStmt.setInt(6, cuota.getIdInscripcion());
                //    updateStmt.setInt(7, cuota.getCuoPeriodo());
                //    updateStmt.executeUpdate();
                //}
                return;
            }
        }

        // Si no existe damos de alta una nueva cuota
        String query = "INSERT INTO Cuota (idInscripcion, cuoPeriodo, cuoFechaEmision, cuoImporte, cuoFechaVencimiento, cuoFechaPago, cuoEstado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setInt(1, cuota.getIdInscripcion());
            pstmt.setInt(2, cuota.getCuoPeriodo());
            pstmt.setDate(3, new java.sql.Date(cuota.getCuoFechaEmision().getTime()));
            pstmt.setDouble(4, cuota.getCuoImporte());
            pstmt.setDate(5, new java.sql.Date(cuota.getCuoFechaVencimiento().getTime()));

            if (cuota.getCuoFechaPago() != null) {
                pstmt.setDate(6, new java.sql.Date(cuota.getCuoFechaPago().getTime()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            pstmt.setString(7, cuota.getCuoEstado());

            pstmt.executeUpdate();
        }
    }

    // Editar los datos de una cuota
    // ----------> no deberiamos tener que usar este metodo ....
    public boolean editarCuota(Cuota cuota) {
        if (!conexionAbierta()) {
            return false;
        }
        String query = "UPDATE Cuota SET CuoFechaEmision = ?, CuoImporte = ?, CuoFechaVencimiento = ?, CuoFechaPago = ?, CuoEstado = ? WHERE IDInscripcion = ? AND CuoPeriodo = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setDate(1, new java.sql.Date(cuota.getCuoFechaEmision().getTime()));
            pstmt.setDouble(2, cuota.getCuoImporte());
            pstmt.setDate(3, new java.sql.Date(cuota.getCuoFechaVencimiento().getTime()));
            pstmt.setDate(4, cuota.getCuoFechaPago() != null ? new java.sql.Date(cuota.getCuoFechaPago().getTime()) : null);
            pstmt.setString(5, cuota.getCuoEstado());
            pstmt.setInt(6, cuota.getIdInscripcion());
            pstmt.setInt(7, cuota.getCuoPeriodo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Método para eliminar una cuota
    // -----> Se usaria solo si se dio de alta la cuota de un alumno que luego anuncia que deja las clases
    public boolean eliminarCuota(int idInscripcion, String cuoPeriodo) {
        if (!conexionAbierta()) {
            return false;
        }
        String query = "UPDATE Cuota SET CuoEstado = ? WHERE IDInscripcion = ? AND CuoPeriodo = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, "Eliminada"); // Eliminación lógica
            pstmt.setInt(2, idInscripcion);
            pstmt.setString(3, cuoPeriodo);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCuota(Cuota cuota) throws SQLException {
        if (!conexionAbierta()) {
            return false;
        }
        String query = "UPDATE Cuota SET cuoEstado = ?, cuoFechaPago = ? WHERE idInscripcion = ? AND cuoPeriodo = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, cuota.getCuoEstado());
            pstmt.setDate(2, new java.sql.Date(cuota.getCuoFechaPago().getTime()));
            pstmt.setInt(3, cuota.getIdInscripcion());
            pstmt.setInt(4, cuota.getCuoPeriodo());
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cuota> obtenerCuotasPorTaller(int idTaller) {
        List<Cuota> listaCuotas = new ArrayList<>();
        if (!conexionAbierta()) {
            return listaCuotas;
        }

        // Primero, obtenemos las inscripciones correspondientes al taller
        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        List<Inscripcion> inscripciones = inscripcionDAO.obtenerInscripcionesPorTaller(idTaller);

        // Ahora, filtramos las cuotas que correspondan a las inscripciones encontradas
        for (Inscripcion inscripcion : inscripciones) {
            String query = "SELECT * FROM Cuota WHERE IDInscripcion = ? AND CuoEstado = 'Impaga' ORDER BY CuoFechaVencimiento";

            try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
                stmt.setInt(1, inscripcion.getIdInscripcion());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Cuota cuota = new Cuota();
                    cuota.setIdInscripcion(rs.getInt("IDInscripcion"));
                    cuota.setCuoPeriodo(rs.getInt("CuoPeriodo"));
                    cuota.setCuoFechaEmision(rs.getDate("CuoFechaEmision"));
                    cuota.setCuoImporte(rs.getDouble("CuoImporte"));
                    cuota.setCuoFechaVencimiento(rs.getDate("CuoFechaVencimiento"));
                    cuota.setCuoFechaPago(rs.getDate("CuoFechaPago"));
                    cuota.setCuoEstado(rs.getString("CuoEstado"));
                    listaCuotas.add(cuota);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaCuotas;
    }

    public List<Cuota> obtenerCuotasPagasNoLiquidadas(int idTaller) {
        List<Cuota> listaCuotas = new ArrayList<>();
        if (!conexionAbierta()) {
            return listaCuotas;
        }

        // Primero, obtenemos las inscripciones correspondientes al taller
        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        List<Inscripcion> inscripciones = inscripcionDAO.obtenerInscripcionesPorTaller(idTaller);

        // Ahora, filtramos las cuotas que correspondan a las inscripciones encontradas
        for (Inscripcion inscripcion : inscripciones) {
            String query = "SELECT * FROM Cuota WHERE IDInscripcion = ? AND CuoEstado = 'Pagada' AND IdLiquidacion IS NULL ORDER BY CuoFechaVencimiento";

            try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
                stmt.setInt(1, inscripcion.getIdInscripcion());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Cuota cuota = new Cuota();
                    cuota.setIdInscripcion(rs.getInt("IDInscripcion"));
                    cuota.setCuoPeriodo(rs.getInt("CuoPeriodo"));
                    cuota.setCuoFechaEmision(rs.getDate("CuoFechaEmision"));
                    cuota.setCuoImporte(rs.getDouble("CuoImporte"));
                    cuota.setCuoFechaVencimiento(rs.getDate("CuoFechaVencimiento"));
                    cuota.setCuoFechaPago(rs.getDate("CuoFechaPago"));
                    cuota.setCuoEstado(rs.getString("CuoEstado"));
                    cuota.setIdLiquidacion(rs.getInt("IdLiquidacion")); // Si es NULL, esto será 0 o no se asignará valor
                    listaCuotas.add(cuota);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaCuotas;
    }



}