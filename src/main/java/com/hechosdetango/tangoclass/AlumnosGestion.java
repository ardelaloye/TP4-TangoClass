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

public class AlumnosGestion {
    private TableView<Alumno> tablaAlumnos;
    public AlumnosGestion(Stage primaryStage) {
        Stage stageAlumnos = new Stage();
        stageAlumnos.setTitle("TangoClass - Gestión de Alumnos");

        // Creamos una tabla y la cargamos con los registros existentes
        tablaAlumnos = new TableView<>();
        inicializarTabla();
        cargarAlumnos();

        // Botones para el ABM de Alumnos
        Button btnAgregar = new Button("Alta");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");

        HBox hbox = new HBox(10, btnAgregar, btnEditar, btnEliminar, btnVolver);
        VBox vbox = new VBox(10, hbox, tablaAlumnos);
        vbox.setSpacing(15);

        Scene scene = new Scene(vbox, 800, 600);
        stageAlumnos.setScene(scene);
        stageAlumnos.show();

        // Acciones de los botones
        btnAgregar.setOnAction(e -> agregarAlumno(stageAlumnos));
        btnEditar.setOnAction(e -> editarAlumno(stageAlumnos));
        btnEliminar.setOnAction(e -> eliminarAlumno());
        btnVolver.setOnAction(e -> volverHome(stageAlumnos));
    }

    private void inicializarTabla() {
        TableColumn<Alumno, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cellData -> cellData.getValue().aluApellidoProperty());

        TableColumn<Alumno, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().aluNombreProperty());

        TableColumn<Alumno, String> colDNI = new TableColumn<>("DNI");
        colDNI.setCellValueFactory(cellData -> cellData.getValue().aluDNIProperty());

        TableColumn<Alumno, String> colFechaNacimiento = new TableColumn<>("Fecha de Nacimiento");
        colFechaNacimiento.setCellValueFactory(cellData -> cellData.getValue().aluFechaNacimientoProperty().asString());

        TableColumn<Alumno, String> colDomicilio = new TableColumn<>("Domicilio");
        colDomicilio.setCellValueFactory(cellData -> cellData.getValue().aluDomicilioProperty());

        TableColumn<Alumno, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().aluTelefonoProperty());

        TableColumn<Alumno, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> cellData.getValue().aluEmailProperty());

        TableColumn<Alumno, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().aluEstadoProperty());

        // Añadimos columnas a la tabla
        tablaAlumnos.getColumns().addAll(colApellido, colNombre, colDNI, colFechaNacimiento, colDomicilio, colTelefono, colEmail, colEstado);

        // Cargamos datos
        ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();
        tablaAlumnos.setItems(listaAlumnos);
    }

    private void cargarAlumnos() {
        // Cargamos los datos de los alumnos desde la base de datos
        List<Alumno> listaAlumnos = OperacionesCRUD.obtenerAlumnosActivos();
        ObservableList<Alumno> alumnosData = FXCollections.observableArrayList(listaAlumnos);
        tablaAlumnos.setItems(alumnosData);
    }

    private void agregarAlumno(Stage stageAlumnos) {
        // Permite agregar un nuevo alumno
        Alumno alumnoSeleccionado = new Alumno();
        new AlumnosForm(stageAlumnos, alumnoSeleccionado);
        cargarAlumnos();
    }

    private void editarAlumno(Stage stageAlumnos) {
        // Permite editar los datos de un alumno
        Alumno alumnoSeleccionado = tablaAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            new AlumnosForm(stageAlumnos, alumnoSeleccionado);
        } else {
            mostrarAlerta("Selecciona un alumno", "Debes seleccionar un alumno para editarlo.");
        }
    }

    private void eliminarAlumno() {
        // Permite eliminar un alumno (cambiamos su estado a 'Eliminado')
        Alumno alumnoSeleccionado = tablaAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            alumnoSeleccionado.setAluEstado("Eliminado");
            //OperacionesCRUD.eliminarAlumno(alumnoSeleccionado.getIdAlumno());
            tablaAlumnos.refresh();
        } else {
            mostrarAlerta("Selecciona un alumno", "Debes seleccionar un alumno para eliminarlo.");
        }
    }

    private void volverHome(Stage stageAlumnos) {
        stageAlumnos.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
