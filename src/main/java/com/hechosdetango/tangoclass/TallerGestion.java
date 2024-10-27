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

import java.util.Date;
import java.util.List;

public class TallerGestion {

    private TableView<Taller> tablaTalleres;

    public TallerGestion(Stage primaryStage) {
        Stage stageTalleres = new Stage();
        stageTalleres.setTitle("Gestión de Talleres");

        tablaTalleres = new TableView<>();
        inicializarTabla();
        cargarTalleres();

        // Botones para el ABM
        Button btnAgregar = new Button("Alta");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");

        // Botón para mostrar valores del taller
        Button btnMostrarValores = new Button("Histórico de Costos");

        HBox hbox = new HBox(10, btnAgregar, btnEditar, btnEliminar, btnVolver);

        // Botón para ver los costos de los talleres (debajo del grid)
        VBox vbox = new VBox(10, hbox, tablaTalleres, btnMostrarValores);
        vbox.setSpacing(15);

        Scene scene = new Scene(vbox, 800, 600);
        stageTalleres.setScene(scene);
        stageTalleres.show();

        // Acciones de los botones
        btnAgregar.setOnAction(e -> agregarTaller(stageTalleres));
        btnEditar.setOnAction(e -> editarTaller(stageTalleres));
        btnEliminar.setOnAction(e -> eliminarTaller());
        btnVolver.setOnAction(e -> volverHome(stageTalleres));
        btnMostrarValores.setOnAction(e -> mostrarValoresTaller());
    }

    private void inicializarTabla() {
        TableColumn<Taller, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().talNombreProperty());

        TableColumn<Taller, String> colFechaInicio = new TableColumn<>("Fecha de Inicio");
        colFechaInicio.setCellValueFactory(cellData -> cellData.getValue().talFechaInicioProperty().asString());

        TableColumn<Taller, String> colFechaCierre = new TableColumn<>("Fecha de Cierre");
        colFechaCierre.setCellValueFactory(cellData -> cellData.getValue().talFechaCierreProperty().asString());

        TableColumn<Taller, Integer> colCupoMaximo = new TableColumn<>("Cupo Máximo");
        colCupoMaximo.setCellValueFactory(cellData -> cellData.getValue().talCupoMaximoProperty().asObject());

        TableColumn<Taller, Integer> colIdDocente = new TableColumn<>("ID Docente");
        colIdDocente.setCellValueFactory(cellData -> cellData.getValue().idDocenteProperty().asObject());

        TableColumn<Taller, String> colNombreDocente = new TableColumn<>("Nombre Docente");
        colNombreDocente.setCellValueFactory(cellData -> {
            Integer idDocente = cellData.getValue().getIdDocente();
            String nombreDocente = OperacionesCRUD.obtenerNombreDocente(idDocente);
            return new SimpleStringProperty(nombreDocente);
        });

        TableColumn<Taller, Double> colComisionDocente = new TableColumn<>("Comisión Docente");
        colComisionDocente.setCellValueFactory(cellData -> cellData.getValue().talComisionDocenteProperty().asObject());

        TableColumn<Taller, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().talEstadoProperty());

        tablaTalleres.getColumns().addAll(colNombre, colFechaInicio, colFechaCierre, colCupoMaximo, colIdDocente, colNombreDocente, colEstado);

        ObservableList<Taller> listaTalleres = FXCollections.observableArrayList();
        tablaTalleres.setItems(listaTalleres);
    }

    private void cargarTalleres() {
        // Datos de los talleres desde la base de datos
        List<Taller> listaTalleres = OperacionesCRUD.obtenerTalleresActivos();
        ObservableList<Taller> talleresData = FXCollections.observableArrayList(listaTalleres);
        tablaTalleres.setItems(talleresData);
    }

    private void agregarTaller(Stage stageTalleres) {
        // Agrega un nuevo taller
        Taller tallerSeleccionado = new Taller();
        new TallerForm(stageTalleres, tallerSeleccionado);
        cargarTalleres();
    }

    private void editarTaller(Stage stageTalleres) {
        // Editar un taller
        Taller tallerSeleccionado = tablaTalleres.getSelectionModel().getSelectedItem();
        if (tallerSeleccionado != null) {
            new TallerForm(stageTalleres, tallerSeleccionado);
        } else {
            mostrarAlerta("Selecciona un taller", "Debes seleccionar un taller para editarlo");
        }
    }

    private void eliminarTaller() {
        // Eliminamos el taller seleccionado (Cambiamos su estado a 'Eliminad')
        Taller tallerSeleccionado = tablaTalleres.getSelectionModel().getSelectedItem();
        if (tallerSeleccionado != null) {
            tallerSeleccionado.setTalEstado("Eliminado");
            tablaTalleres.refresh();
        } else {
            mostrarAlerta("Selecciona un taller", "Debes seleccionar un taller para eliminarlo.");
        }
    }

    private void volverHome(Stage stageTalleres) {
        stageTalleres.close();
    }

    private void mostrarValoresTaller() {
        Taller tallerSeleccionado = tablaTalleres.getSelectionModel().getSelectedItem();
        if (tallerSeleccionado != null) {
            List<ValoresTaller> valoresTaller = OperacionesCRUD.obtenerValoresTaller(tallerSeleccionado.getIdTaller());

            // mostramos los costos del taller en una nueva ventana
            Stage valoresStage = new Stage();
            valoresStage.setTitle("Costo del Taller: " + tallerSeleccionado.getTalNombre());

            TableView<ValoresTaller> tablaValores = new TableView<>();
            TableColumn<ValoresTaller, Date> colFechaDesde = new TableColumn<>("Fecha Desde");
            colFechaDesde.setCellValueFactory(cellData -> cellData.getValue().valFechaDesdeProperty());
            TableColumn<ValoresTaller, Date> colFechaHasta = new TableColumn<>("Fecha Hasta");
            colFechaHasta.setCellValueFactory(cellData -> cellData.getValue().valFechaHastaProperty());
            TableColumn<ValoresTaller, Double> colImporte = new TableColumn<>("Importe");
            colImporte.setCellValueFactory(cellData -> cellData.getValue().valImporteProperty().asObject());
            tablaValores.getColumns().addAll(colFechaDesde, colFechaHasta, colImporte);

            ObservableList<ValoresTaller> valoresData = FXCollections.observableArrayList(valoresTaller);
            tablaValores.setItems(valoresData);

            VBox vbox = new VBox(tablaValores);
            Scene scene = new Scene(vbox, 400, 300);
            valoresStage.setScene(scene);
            valoresStage.show();
        } else {
            mostrarAlerta("Selecciona un taller", "Debes seleccionar un taller para mostrar sus costos.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
