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

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class InscripcionesGestion {
    private TableView<Inscripcion> tablaInscripciones;

    public InscripcionesGestion(Stage primaryStage) {
        Stage stageInscripciones = new Stage();
        stageInscripciones.setTitle("TangoClass - Gestión de Inscripciones");

        // Creamos una tabla y la cargamos con los registros de inscripciones existentes
        tablaInscripciones = new TableView<>();
        inicializarTabla();
        cargarInscripciones();

        // Botones para el ABM de Inscripciones
        Button btnAgregar = new Button("Alta");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");

        HBox hbox = new HBox(10, btnAgregar, btnEditar, btnEliminar, btnVolver);
        VBox vbox = new VBox(10, hbox, tablaInscripciones);
        vbox.setSpacing(15);

        Scene scene = new Scene(vbox, 800, 600);
        stageInscripciones.setScene(scene);
        stageInscripciones.showAndWait();

        // Acciones de los botones
        btnAgregar.setOnAction(e -> agregarInscripcion(stageInscripciones));
        btnEditar.setOnAction(e -> editarInscripcion(stageInscripciones));
        btnEliminar.setOnAction(e -> eliminarInscripcion());
        btnVolver.setOnAction(e -> volverHome(stageInscripciones));
    }

    private void inicializarTabla() {
        TableColumn<Inscripcion, Integer> colIdInscripcion = new TableColumn<>("ID Inscripción");
        colIdInscripcion.setCellValueFactory(cellData -> cellData.getValue().idInscripcionProperty().asObject());

        TableColumn<Inscripcion, Integer> colIdAlumno = new TableColumn<>("ID Alumno");
        colIdAlumno.setCellValueFactory(cellData -> cellData.getValue().idAlumnoProperty().asObject());

        TableColumn<Inscripcion, String> colNombreAlumno = new TableColumn<>("Nombre Alumno");
        colNombreAlumno.setCellValueFactory(cellData -> {
            Inscripcion inscripcion = cellData.getValue();
            String nombreAlumno = OperacionesCRUD.obtenerNombreAlumno(inscripcion.getIdAlumno());
            return new SimpleStringProperty(nombreAlumno);
        });

        TableColumn<Inscripcion, Integer> colIdTaller = new TableColumn<>("ID Taller");
        colIdTaller.setCellValueFactory(cellData -> cellData.getValue().idTallerProperty().asObject());

        TableColumn<Inscripcion, String> colNombreTaller = new TableColumn<>("Nombre Taller");
        colNombreTaller.setCellValueFactory(cellData -> {
            Inscripcion inscripcion = cellData.getValue();
            String nombreTaller = OperacionesCRUD.obtenerNombreTaller(inscripcion.getIdTaller());
            return new SimpleStringProperty(nombreTaller);
        });

        TableColumn<Inscripcion, String> colFechaInscripcion = new TableColumn<>("Fecha de Inscripción");
        colFechaInscripcion.setCellValueFactory(cellData -> cellData.getValue().insFechaInscripcionProperty().asString());

        TableColumn<Inscripcion, String> colFechaBaja = new TableColumn<>("Fecha de Baja");
        colFechaBaja.setCellValueFactory(cellData -> cellData.getValue().insFechaBajaProperty().asString());

        TableColumn<Inscripcion, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().insEstadoProperty());

        // Añadimos columnas a la tabla
        tablaInscripciones.getColumns().addAll(colIdInscripcion, colIdAlumno, colNombreAlumno, colIdTaller, colNombreTaller, colFechaInscripcion, colFechaBaja, colEstado);

        // Cargamos datos
        ObservableList<Inscripcion> listaInscripciones = FXCollections.observableArrayList();
        tablaInscripciones.setItems(listaInscripciones);
    }

    private void cargarInscripciones() {
        // Cargamos los datos de las inscripciones desde la base de datos
        List<Inscripcion> listaInscripciones = OperacionesCRUD.obtenerInscripciones();
        ObservableList<Inscripcion> inscripcionesData = FXCollections.observableArrayList(listaInscripciones);
        tablaInscripciones.setItems(inscripcionesData);
    }

    private void agregarInscripcion(Stage stageInscripciones) {
        Inscripcion inscripcionSeleccionada = new Inscripcion();
        new InscripcionesForm(stageInscripciones, inscripcionSeleccionada);
        cargarInscripciones();
    }

    private void editarInscripcion(Stage stageInscripciones) {
        Inscripcion inscripcionSeleccionada = tablaInscripciones.getSelectionModel().getSelectedItem();
        if (inscripcionSeleccionada != null) {
            new InscripcionesForm(stageInscripciones, inscripcionSeleccionada);
        } else {
            mostrarAlerta("Selecciona una inscripción", "Debes seleccionar una inscripción para editarla.");
        }
    }

    private void eliminarInscripcion() {
        Inscripcion inscripcionSeleccionada = tablaInscripciones.getSelectionModel().getSelectedItem();
        if (inscripcionSeleccionada != null) {
            inscripcionSeleccionada.setInsEstado("Eliminada");
            tablaInscripciones.refresh();
        } else {
            mostrarAlerta("Selecciona una inscripción", "Debes seleccionar una inscripción para eliminarla.");
        }
    }

    private void volverHome(Stage stageInscripciones) {
        stageInscripciones.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
