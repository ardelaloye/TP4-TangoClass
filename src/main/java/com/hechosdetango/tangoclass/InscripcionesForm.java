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

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InscripcionesForm {

    private Inscripcion inscripcion;
    private ComboBox<String> comboAlumno, comboTaller, comboEstado;
    private DatePicker datePickerFechaInscripcion, datePickerFechaBaja;
    private Button btnAceptar, btnCancelar;

    private Map<String, Integer> alumnosMap = new HashMap<>();
    private Map<String, Integer> talleresMap = new HashMap<>();

    // Para agregar una nueva inscripción
    public InscripcionesForm(Stage parentStage) {
        this.inscripcion = new Inscripcion();
        mostrarFormulario(parentStage, "Agregar Inscripción");
    }

    // Para editar una inscripción existente
    public InscripcionesForm(Stage parentStage, Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
        mostrarFormulario(parentStage, "Editar Inscripción");
    }

    private void mostrarFormulario(Stage parentStage, String titulo) {
        Stage formularioStage = new Stage();
        formularioStage.setTitle(titulo);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Combos para seleccionar Alumno, Taller y Estado
        comboAlumno = new ComboBox<>();
        comboTaller = new ComboBox<>();
        comboEstado = new ComboBox<>();

        // Cargar alumnos y talleres
        cargarAlumnos();
        cargarTalleres();

        // Si hay un alumno asociado, seleccionarlo
        if (inscripcion.getIdAlumno() != 0) {
            String nombreCompletoAlumno = OperacionesCRUD.obtenerNombreAlumno(inscripcion.getIdAlumno());
            comboAlumno.setValue(nombreCompletoAlumno);
        }

        // Si hay un taller asociado, seleccionarlo
        if (inscripcion.getIdTaller() != 0) {
            String nombreTaller = OperacionesCRUD.obtenerNombreTaller(inscripcion.getIdTaller());
            comboTaller.setValue(nombreTaller);
        }

        // Fecha en que el alumno se inscribio en el taller
        datePickerFechaInscripcion = new DatePicker();
        if (inscripcion.getInsFechaInscripcion() != null) {
            datePickerFechaInscripcion.setValue(new java.sql.Date(inscripcion.getInsFechaInscripcion().getTime()).toLocalDate());
        }

        // Fecha de baja (puede ser nula)
        datePickerFechaBaja = new DatePicker();
        if (inscripcion.getInsFechaBaja() != null) {
            datePickerFechaBaja.setValue(new java.sql.Date(inscripcion.getInsFechaBaja().getTime()).toLocalDate());
        }

        // Combo para el estado de la inscripción
        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Activa", "Baja");
        comboEstado.setValue(inscripcion.getInsEstado());

        btnAceptar = new Button("Aceptar");
        btnCancelar = new Button("Cancelar");

        // Añadimos los campos al grid
        grid.add(new Label("Alumno:"), 0, 0);
        grid.add(comboAlumno, 1, 0);
        grid.add(new Label("Taller:"), 0, 1);
        grid.add(comboTaller, 1, 1);
        grid.add(new Label("Fecha de Inscripción:"), 0, 2);
        grid.add(datePickerFechaInscripcion, 1, 2);
        grid.add(new Label("Fecha de Baja:"), 0, 3);
        grid.add(datePickerFechaBaja, 1, 3);
        grid.add(new Label("Estado:"), 0, 4);
        grid.add(comboEstado, 1, 4);

        // Botones para aceptar o cancelar
        grid.add(btnAceptar, 0, 5);
        grid.add(btnCancelar, 1, 5);

        // Acción del Botón "Aceptar"
        btnAceptar.setOnAction(e -> {
            if (validarFormulario()) {
                guardarInscripcion();
                formularioStage.close();
            } else {
                mostrarAlerta("Error", "Por favor, completa todos los campos.");
            }
        });

        // Acción del Botón "Cancelar"
        btnCancelar.setOnAction(e -> formularioStage.close());

        Scene scene = new Scene(grid, 400, 300);
        formularioStage.setScene(scene);
        formularioStage.initOwner(parentStage);
        formularioStage.show();
    }

    private boolean validarFormulario() {
        // Validamos que no haya campos vacíos
        return comboAlumno.getValue() != null && comboTaller.getValue() != null
                && datePickerFechaInscripcion.getValue() != null && comboEstado.getValue() != null;
    }

    private void guardarInscripcion() {
        inscripcion.setIdAlumno(alumnosMap.get(comboAlumno.getValue()));
        inscripcion.setIdTaller(talleresMap.get(comboTaller.getValue()));
        inscripcion.setInsEstado(comboEstado.getValue());

        Date fechaInscripcion = Date.valueOf(datePickerFechaInscripcion.getValue());
        inscripcion.setInsFechaInscripcion(fechaInscripcion);

        if (datePickerFechaBaja.getValue() != null) {
            Date fechaBaja = Date.valueOf(datePickerFechaBaja.getValue());
            inscripcion.setInsFechaBaja(fechaBaja);
        } else {
            inscripcion.setInsFechaBaja(null);
        }

        OperacionesCRUD operacion = new OperacionesCRUD();
        if (inscripcion.getIdInscripcion() == 0) {
            if (operacion.agregarInscripcion(inscripcion)) {
                System.out.println("Inscripción agregada correctamente.");
            } else {
                System.out.println("Error al agregar la Inscripción.");
            }
        } else {
            if (operacion.editarInscripcion(inscripcion)) {
                System.out.println("Inscripción modificada correctamente.");
            } else {
                System.out.println("Error al editar la Inscripción.");
            }
        }
    }

    private void cargarAlumnos() {
        List<Alumno> listaAlumnos = OperacionesCRUD.obtenerAlumnosActivos();
        for (Alumno alumno : listaAlumnos) {
            String nombreCompleto = alumno.getAluNombre() + " " + alumno.getAluApellido();
            comboAlumno.getItems().add(nombreCompleto);
            alumnosMap.put(nombreCompleto, alumno.getIdAlumno());
        }
    }

    private void cargarTalleres() {
        List<Taller> listaTalleres = OperacionesCRUD.obtenerTalleres();

        for (Taller taller : listaTalleres) {
            comboTaller.getItems().add(taller.getTalNombre());
            talleresMap.put(taller.getTalNombre(), taller.getIdTaller());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
