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

public class AlumnoDAO {

    // Constructor de la clase
    public AlumnoDAO() {}

    private static boolean conexionAbierta() {
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

    // Metodo para obtener todos los alumnos activos
    public List<Alumno> obtenerAlumnosActivos() {
        List<Alumno> listaAlumnos = new ArrayList<>();
        if (!conexionAbierta()) {return listaAlumnos;}

        String query = "SELECT * FROM Alumno WHERE AluEstado='Activo'";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("IDAlumno"));
                alumno.setAluApellido(rs.getString("AluApellido"));
                alumno.setAluNombre(rs.getString("AluNombre"));
                alumno.setAluDNI(rs.getString("AluDNI"));
                alumno.setAluFechaNacimiento(rs.getDate("AluFechaNacimiento"));
                alumno.setAluDomicilio(rs.getString("AluDomicilio"));
                alumno.setAluTelefono(rs.getString("AluTelefono"));
                alumno.setAluEmail(rs.getString("AluEmail"));
                alumno.setAluEstado(rs.getString("AluEstado"));
                listaAlumnos.add(alumno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAlumnos;
    }

    // Devuelve datos de un alumno a partir de su  ID
    public static Alumno obtenerAlumnoPorId(int idAlumno) {
        Alumno alumno = null;
        if (!conexionAbierta()) {
            return alumno;
        }

        String query = "SELECT * FROM Alumno WHERE IDAlumno = ?";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("IDAlumno"));
                alumno.setAluApellido(rs.getString("AluApellido"));
                alumno.setAluNombre(rs.getString("AluNombre"));
                alumno.setAluDNI(rs.getString("AluDNI"));
                alumno.setAluFechaNacimiento(rs.getDate("AluFechaNacimiento"));
                alumno.setAluDomicilio(rs.getString("AluDomicilio"));
                alumno.setAluTelefono(rs.getString("AluTelefono"));
                alumno.setAluEmail(rs.getString("AluEmail"));
                alumno.setAluEstado(rs.getString("AluEstado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumno;
    }

    // Devuelve el nombre y el apellido de un alumno concatenados, a partir de su ID
    public static String getAluNomApPorId(int idAlumno) {
        String aluNomAp = "";
        String query = "SELECT AluNombre, AluApellido FROM Alumno WHERE IDAlumno = ?";

        try (PreparedStatement stmt = Conexion.getConexion().prepareStatement(query)) {
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Concatenar nombre y apellido
                aluNomAp = rs.getString("AluNombre") + " " + rs.getString("AluApellido");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aluNomAp;
    }

    // Dar de alta un nuevo alumno
    public boolean agregarAlumno(Alumno alumno) {
        if (!conexionAbierta()) {
            return false;
        }
        String query = "INSERT INTO Alumno (AluApellido, AluNombre, AluDNI, AluFechaNacimiento, AluDomicilio, AluTelefono, AluEmail, AluEstado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, alumno.getAluApellido());
            pstmt.setString(2, alumno.getAluNombre());
            pstmt.setString(3, alumno.getAluDNI());
            pstmt.setDate(4, new java.sql.Date(alumno.getAluFechaNacimiento().getTime()));
            pstmt.setString(5, alumno.getAluDomicilio());
            pstmt.setString(6, alumno.getAluTelefono());
            pstmt.setString(7, alumno.getAluEmail());
            pstmt.setString(8, alumno.getAluEstado());
            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar los datos de un alumno
    public static boolean editarAlumno(Alumno alumno) {
        if (!Conexion.conectar()) {
            return false;
        }

        String query = "UPDATE Alumno SET AluApellido = ?, AluNombre = ?, AluDNI = ?, AluFechaNacimiento = ?, AluDomicilio = ?, AluTelefono = ?, AluEmail = ?, AluEstado = ? WHERE IDAlumno = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, alumno.getAluApellido());
            pstmt.setString(2, alumno.getAluNombre());
            pstmt.setString(3, alumno.getAluDNI());
            pstmt.setDate(4, new java.sql.Date(alumno.getAluFechaNacimiento().getTime()));
            pstmt.setString(5, alumno.getAluDomicilio());
            pstmt.setString(6, alumno.getAluTelefono());
            pstmt.setString(7, alumno.getAluEmail());
            pstmt.setString(8, alumno.getAluEstado());
            pstmt.setInt(9, alumno.getIdAlumno()); // ID del alumno a editar
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Eliminar un alumno
    // La eliminacion no es física sino logica. Cambiamos su estado a 'Eliminado'
    public boolean eliminarAlumno(int idAlumno) {
        if (!conexionAbierta()) {
            return false;
        }

        String query = "UPDATE Alumno SET AluEstado = ? WHERE IDAlumno = ?";
        try (PreparedStatement pstmt = Conexion.getConexion().prepareStatement(query)) {
            pstmt.setString(1, "Eliminado");
            pstmt.setInt(2, idAlumno);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
