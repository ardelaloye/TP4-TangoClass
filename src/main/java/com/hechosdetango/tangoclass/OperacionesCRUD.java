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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperacionesCRUD {
    // En esta clase agruparemos la mayoría de operaciones de Alta y Edición de registros de la base de datos
    private static Connection conexion;
    public OperacionesCRUD() {
        this.conexion = Conexion.getConexion();
    }

    //
    // *********************************  OPERACIONES SOBRE ALUMNOS
    //

    // Agregar un nuevo alumno
    public boolean agregarAlumno(Alumno alumno) {
        String query = "INSERT INTO Alumno (AluApellido, AluNombre, AluDNI, AluFechaNacimiento, AluDomicilio, AluTelefono, AluEmail, AluEstado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
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

    // Actualizar el estado de un alumno
    public boolean actualizarEstadoAlumno(int idAlumno, String nuevoEstado) {
        String query = "UPDATE Alumno SET AluEstado = ? WHERE IDAlumno = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idAlumno);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar datos de un alumno
    public static boolean editarAlumno(Alumno alumno) {
        String query = "UPDATE Alumnos SET AluApellido = ?, AluNombre = ?, AluDNI = ?, AluFechaNacimiento = ?, AluDomicilio = ?, AluTelefono = ?, AluEmail = ?, AluEstado = ? WHERE IDAlumno = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
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
        }
        return false;
    }

    // Obtenemos los datos de todos los alumnos
    public static List<Alumno> obtenerAlumnos() {
        List<Alumno> listaAlumnos = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM Alumno";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("IDAlumno"));
                alumno.setAluApellido(rs.getString("AluApellido"));
                alumno.setAluNombre(rs.getString("AluNombre"));
                alumno.setAluDNI(rs.getString("AluDNI"));
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

    public static List<Alumno> obtenerAlumnosActivos() {
        List<Alumno> listaAlumnos = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM Alumno WHERE AluEstado='Activo'";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("IDAlumno"));
                alumno.setAluApellido(rs.getString("AluApellido"));
                alumno.setAluNombre(rs.getString("AluNombre"));
                alumno.setAluDNI(rs.getString("AluDNI"));
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

    public static String obtenerNombreAlumno(int idAlumno) {
        String nombreCompleto = null;
        Connection conexion = Conexion.getConexion();
        String query = "SELECT AluNombre, AluApellido FROM Alumno WHERE IDAlumno = ?";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("AluNombre");
                String apellido = rs.getString("AluApellido");
                nombreCompleto = nombre + " " + apellido;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreCompleto;
    }

    //
    // *********************************  OPERACIONES CON DOCENTES
    //

    // Agregar un nuevo docente
    public boolean agregarDocente(Docente docente) {
        String query = "INSERT INTO Docente (DocApellido, DocNombre, DocDNI, DocTelefono, DocEmail, DocEstado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, docente.getDocApellido());
            pstmt.setString(2, docente.getDocNombre());
            pstmt.setString(3, docente.getDocDNI());
            pstmt.setString(4, docente.getDocTelefono());
            pstmt.setString(5, docente.getDocEmail());
            pstmt.setString(6, docente.getDocEstado());
            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar el estado de un docente
    public boolean actualizarEstadoDocente(int idDocente, String nuevoEstado) {
        String query = "UPDATE Docente SET DocEstado = ? WHERE IDDocente = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idDocente);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar datos de un docente
    public static boolean editarDocente(Docente docente) {
        String query = "UPDATE Docente SET DocApellido = ?, DocNombre = ?, DocDNI = ?, DocTelefono = ?, DocEmail = ?, DocEstado = ? WHERE IDDocente = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, docente.getDocApellido());
            pstmt.setString(2, docente.getDocNombre());
            pstmt.setString(3, docente.getDocDNI());
            pstmt.setString(4, docente.getDocTelefono());
            pstmt.setString(5, docente.getDocEmail());
            pstmt.setString(6, docente.getDocEstado());
            pstmt.setInt(7, docente.getIdDocente());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String obtenerNombreDocente(int idDocente) {
        String nombreCompleto = null;
        Connection conexion = Conexion.getConexion();
        String query = "SELECT DocNombre, DocApellido FROM Docente WHERE IDDocente = ?";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setInt(1, idDocente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("DocNombre");
                String apellido = rs.getString("DocApellido");
                nombreCompleto = nombre + " " + apellido;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombreCompleto;
    }


    // Obtener los datos de todos los docentes
    public static List<Docente> obtenerDocentes() {
        List<Docente> listaDocentes = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM Docente";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
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

    // Obtener los datos de todos los docentes que están en estado 'Activo'
    public static List<Docente> obtenerDocentesActivos() {
        List<Docente> listaDocentesActivos = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM Docente WHERE DocEstado = 'Activo'";

        try (PreparedStatement pstmt = conexion.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Docente docente = new Docente();
                docente.setIdDocente(rs.getInt("IDDocente"));
                docente.setDocApellido(rs.getString("DocApellido"));
                docente.setDocNombre(rs.getString("DocNombre"));
                docente.setDocDNI(rs.getString("DocDNI"));
                docente.setDocTelefono(rs.getString("DocTelefono"));
                docente.setDocEmail(rs.getString("DocEmail"));
                docente.setDocEstado(rs.getString("DocEstado"));
                listaDocentesActivos.add(docente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaDocentesActivos;
    }


    //
    // *********************************  TALLERES
    //

    // Agregar un nuevo taller
    public static boolean agregarTaller(Taller taller) {
        String query = "INSERT INTO Taller (TalNombre, TalFechaInicio, TalFechaCierre, TalCupoMaximo, IDDocente, TalComisionDocente, TalEstado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
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

    // Actualizar el estado de un taller
    public static boolean actualizarEstadoTaller(int idTaller, String nuevoEstado) {
        String query = "UPDATE Taller SET talEstado = ? WHERE IDTaller = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idTaller);
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

     // Actualizar datos de un taller existente
    public static boolean editarTaller(Taller taller) {
        String query = "UPDATE Taller SET TalNombre = ?, TalFechaInicio = ?, TalFechaCierre = ?, TalCupoMaximo = ?, IDDocente = ?, TalComisionDocente = ?, TalEstado = ? WHERE IDTaller = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, taller.getTalNombre());
            pstmt.setDate(2, new java.sql.Date(taller.getTalFechaInicio().getTime()));
            pstmt.setDate(3, new java.sql.Date(taller.getTalFechaCierre().getTime()));
            pstmt.setInt(4, taller.getTalCupoMaximo());
            pstmt.setInt(5, taller.getIdDocente());
            pstmt.setDouble(6, taller.getTalComisionDocente());
            pstmt.setString(7, taller.getTalEstado());
            pstmt.setInt(8, taller.getIdTaller());
            int filasActualizadas = pstmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Devuelve el nombre de un taller determinado
    public static String obtenerNombreTaller(int idTaller) {
        String nombreTaller = null;
        Connection conexion = Conexion.getConexion();
        String query = "SELECT TalNombre FROM Taller WHERE IDTaller = ?";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setInt(1, idTaller);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombreTaller = rs.getString("TalNombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombreTaller;
    }

    // Obtener todos los talleres
    public static List<Taller> obtenerTalleres() {
        List<Taller> listaTallers = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM Taller";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Taller taller = new Taller();
                taller.setIdTaller(rs.getInt("idTaller"));
                taller.setTalNombre(rs.getString("talNombre"));
                taller.setTalFechaInicio(rs.getDate("talFechaInicio"));
                taller.setTalFechaCierre(rs.getDate("talFechaCierre"));
                taller.setTalCupoMaximo(rs.getInt("talCupoMaximo"));
                taller.setIdDocente(rs.getInt("idDocente"));
                taller.setTalComisionDocente(rs.getDouble("talComisionDocente"));
                taller.setTalEstado(rs.getString("talEstado"));
                listaTallers.add(taller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTallers;
    }

    // Obtener datos de todos los talleres que están en estado 'Activo'
    public static List<Taller> obtenerTalleresActivos() {
        List<Taller> listaTallers = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM Taller WHERE TalEstado='Activo'";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Taller taller = new Taller();
                taller.setIdTaller(rs.getInt("idTaller"));
                taller.setTalNombre(rs.getString("talNombre"));
                taller.setTalFechaInicio(rs.getDate("talFechaInicio"));
                taller.setTalFechaCierre(rs.getDate("talFechaCierre"));
                taller.setTalCupoMaximo(rs.getInt("talCupoMaximo"));
                taller.setIdDocente(rs.getInt("idDocente"));
                taller.setTalComisionDocente(rs.getDouble("talComisionDocente"));
                taller.setTalEstado(rs.getString("talEstado"));
                listaTallers.add(taller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTallers;
    }


    //
    // *********************************  VALORES (COSTOS) HISTORICOS DE LOS TALLERES
    //

    // Guarda un registro en ValoresTaller
    public static boolean guardarValoresTaller(ValoresTaller valoresTaller) {
        String query = "INSERT INTO ValoresTaller (IDTaller, ValFechaDesde, ValFechaHasta, ValImporte) VALUES (?, ?, ?, ?)";
        Connection conexion = Conexion.getConexion(); // Método para obtener la conexión
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setInt(1, valoresTaller.getIdTaller());
            pstmt.setDate(2, new java.sql.Date(valoresTaller.getValFechaDesde().getTime()));
            pstmt.setDate(3, new java.sql.Date(valoresTaller.getValFechaHasta().getTime()));
            pstmt.setDouble(4, valoresTaller.getValImporte());

            int filasInsertadas = pstmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Devuelve una lista con los valores historicos de un taller determinado
    public static List<ValoresTaller> obtenerValoresTaller(int idTaller) {
        List<ValoresTaller> valoresTallerList = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM ValoresTaller WHERE idTaller = ?";

        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setInt(1, idTaller);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ValoresTaller valoresTaller = new ValoresTaller();
                valoresTaller.setIdValoresTaller(rs.getInt("idValoresTaller"));
                valoresTaller.setIdTaller(rs.getInt("idTaller"));
                valoresTaller.setValFechaDesde(rs.getDate("valFechaDesde"));
                valoresTaller.setValFechaHasta(rs.getDate("valFechaHasta"));
                valoresTaller.setValImporte(rs.getDouble("valImporte"));
                valoresTallerList.add(valoresTaller);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valoresTallerList;
    }

    // Devuelve el valor de un taller determinado en una fecha especificada
    public static Double obtenerValorTaller(int idTaller, Date fecha) {
        Double importe = null;
        Connection conexion = Conexion.getConexion();
        String query = "SELECT ValImporte FROM ValoresTaller WHERE idTaller = ? AND ? BETWEEN ValFechaDesde AND ValFechaHasta";

        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setInt(1, idTaller);
            pstmt.setDate(2, new java.sql.Date(fecha.getTime()));

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                importe = rs.getDouble("ValImporte");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return importe;
    }


    //
    // *********************************  INSCRIPCIONES
    //
    public static List<Inscripcion> obtenerInscripciones() {
        List<Inscripcion> listaInscripciones = new ArrayList<>();
        Connection conexion = Conexion.getConexion();
        String query = "SELECT * FROM Inscripcion";

        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
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

    public boolean agregarInscripcion(Inscripcion inscripcion) {
        Connection conexion = Conexion.getConexion();
        String query = "INSERT INTO Inscripcion (IDAlumno, IDTaller, InsFechaInscripcion, InsFechaBaja, InsEstado) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, inscripcion.getIdAlumno());
            stmt.setInt(2, inscripcion.getIdTaller());
            stmt.setDate(3, new java.sql.Date(inscripcion.getInsFechaInscripcion().getTime()));
            stmt.setDate(4, inscripcion.getInsFechaBaja() != null ?
                    new java.sql.Date(inscripcion.getInsFechaBaja().getTime()) : null);
            stmt.setString(5, inscripcion.getInsEstado());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para editar una inscripción existente
    public boolean editarInscripcion(Inscripcion inscripcion) {
        Connection conexion = Conexion.getConexion();
        String query = "UPDATE Inscripcion SET IDAlumno = ?, IDTaller = ?, InsFechaInscripcion = ?, " +
                "InsFechaBaja = ?, InsEstado = ? WHERE IDInscripcion = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, inscripcion.getIdAlumno());
            stmt.setInt(2, inscripcion.getIdTaller());
            stmt.setDate(3, new java.sql.Date(inscripcion.getInsFechaInscripcion().getTime()));
            stmt.setDate(4, inscripcion.getInsFechaBaja() != null ?
                    new java.sql.Date(inscripcion.getInsFechaBaja().getTime()) : null);
            stmt.setString(5, inscripcion.getInsEstado());
            stmt.setInt(6, inscripcion.getIdInscripcion());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
