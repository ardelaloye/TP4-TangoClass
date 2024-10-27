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

public class DocentesGestion {

    private TableView<Docente> tablaDocentes;

    public DocentesGestion(Stage primaryStage) {
        Stage stageDocentes = new Stage();
        stageDocentes.setTitle("TangoClass - Gestión de Docentes");

        // Creamos una tabla y la cargamos con los registros de docentes existentes
        tablaDocentes = new TableView<>();
        inicializarTabla();
        cargarDocentes();

        // Botones para el ABM de docentes
        Button btnAgregar = new Button("Alta");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");

        HBox hbox = new HBox(10, btnAgregar, btnEditar, btnEliminar, btnVolver);
        VBox vbox = new VBox(10, hbox, tablaDocentes);
        vbox.setSpacing(15);

        Scene scene = new Scene(vbox, 800, 600);
        stageDocentes.setScene(scene);
        stageDocentes.show();

        // Acciones de los botones
        btnAgregar.setOnAction(e -> agregarDocente(stageDocentes));
        btnEditar.setOnAction(e -> editarDocente(stageDocentes));
        btnEliminar.setOnAction(e -> eliminarDocente());
        btnVolver.setOnAction(e -> volverHome(stageDocentes));
    }

    private void inicializarTabla() {
        TableColumn<Docente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cellData -> cellData.getValue().docApellidoProperty());

        TableColumn<Docente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().docNombreProperty());

        TableColumn<Docente, String> colDNI = new TableColumn<>("DNI");
        colDNI.setCellValueFactory(cellData -> cellData.getValue().docDNIProperty());

        TableColumn<Docente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().docTelefonoProperty());

        TableColumn<Docente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> cellData.getValue().docEmailProperty());

        TableColumn<Docente, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().docEstadoProperty());

        // Añadimos las columnas a la tabla
        tablaDocentes.getColumns().addAll(colApellido, colNombre, colDNI, colTelefono, colEmail, colEstado);

        // Cargamos los datos
        ObservableList<Docente> listaDocentes = FXCollections.observableArrayList();
        tablaDocentes.setItems(listaDocentes);
    }

    private void cargarDocentes() {
        // Cargamos los datos de los docentes desde la base de datos
        List<Docente> listaDocentes = OperacionesCRUD.obtenerDocentesActivos();
        ObservableList<Docente> docentesData = FXCollections.observableArrayList(listaDocentes);
        tablaDocentes.setItems(docentesData);
    }

    private void agregarDocente(Stage stageDocentes) {
        // Permite agregar un nuevo docente
        Docente docenteSeleccionado = new Docente();
        new DocentesForm(stageDocentes, docenteSeleccionado);
        cargarDocentes();
    }

    private void editarDocente(Stage stageDocentes) {
        // Permite editar los datos de un docente ya existente
        Docente docenteSeleccionado = tablaDocentes.getSelectionModel().getSelectedItem();
        if (docenteSeleccionado != null) {
            new DocentesForm(stageDocentes, docenteSeleccionado);
        } else {
            mostrarAlerta("Selecciona un docente", "Debes seleccionar un docente para editarlo.");
        }
    }

    private void eliminarDocente() {
        // Permite eliminar un docente (solo le cambiamos su estado a 'Eliminado')
        Docente docenteSeleccionado = tablaDocentes.getSelectionModel().getSelectedItem();
        if (docenteSeleccionado != null) {
            docenteSeleccionado.setDocEstado("Eliminado");
            tablaDocentes.refresh();
        } else {
            mostrarAlerta("Selecciona un docente", "Debes seleccionar un docente para eliminarlo.");
        }
    }

    private void volverHome(Stage stageDocentes) {
        stageDocentes.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
