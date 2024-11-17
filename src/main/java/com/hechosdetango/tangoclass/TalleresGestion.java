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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

public class TalleresGestion {
    private TableView<Taller> tablaTalleres;
    private TextField txtNombre, txtCupoMaximo, txtDocente, txtComisionDocente;
    private DatePicker dpFechaInicio, dpFechaCierre;
    private ComboBox<String> cmbEstado;
    private Button btnGuardar, btnCancelar, btnNuevo, btnEditar, btnEliminar, btnVolver, btnListar;

    private Taller tallerSeleccionado;
    private TallerDAO tallerDAO;
    private VBox vboxGrilla, vboxTaller;

    public TalleresGestion(Stage primaryStage) {
        Stage stageTalleres = new Stage();
        stageTalleres.setTitle("TangoClass - Gestión de Talleres");

        tallerDAO = new TallerDAO();
        tablaTalleres = new TableView<>();
        inicializarTabla();
        cargarTalleres();

        // Botones del ABM
        btnNuevo = new Button("Nuevo");
        btnEditar = new Button("Editar");
        btnEliminar = new Button("Eliminar");
        btnListar = new Button("Listar");
        btnVolver = new Button("Volver");

        // Grilla con los talleres existentes
        vboxGrilla = new VBox();
        HBox botonesGrilla = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnListar, btnVolver);
        botonesGrilla.setPadding(new Insets(0, 0, 10, 0));
        vboxGrilla.getChildren().addAll(botonesGrilla, tablaTalleres);

        // VBox para el formulario donde se ingresan los datos de un taller
        vboxTaller = new VBox();
        GridPane formulario = new GridPane();
        formulario.setVgap(10);
        formulario.setHgap(10);

        txtNombre = new TextField();
        txtCupoMaximo = new TextField();
        txtDocente = new TextField();
        txtComisionDocente = new TextField();
        dpFechaInicio = new DatePicker();
        dpFechaCierre = new DatePicker();
        cmbEstado = new ComboBox<>(FXCollections.observableArrayList("Activo", "Inactivo"));

        // Añadimos los campos al formulario
        formulario.add(new Label("Nombre del Taller:"), 0, 0);
        formulario.add(txtNombre, 1, 0);
        formulario.add(new Label("Cupo Máximo:"), 0, 1);
        formulario.add(txtCupoMaximo, 1, 1);
        formulario.add(new Label("ID Docente:"), 0, 2);
        formulario.add(txtDocente, 1, 2);
        formulario.add(new Label("Comisión Docente:"), 0, 3);
        formulario.add(txtComisionDocente, 1, 3);
        formulario.add(new Label("Fecha de Inicio:"), 0, 4);
        formulario.add(dpFechaInicio, 1, 4);
        formulario.add(new Label("Fecha de Cierre:"), 0, 5);
        formulario.add(dpFechaCierre, 1, 5);
        formulario.add(new Label("Estado:"), 0, 6);
        formulario.add(cmbEstado, 1, 6);

        // Cnfirmar o cancelar el alta o la modificación de un taller
        btnGuardar = new Button("Confirmar");
        btnCancelar = new Button("Cancelar");
        HBox botonesFormulario = new HBox(10, btnGuardar, btnCancelar);
        botonesFormulario.setPadding(new Insets(10, 0, 0, 0));
        vboxTaller.getChildren().addAll(formulario, botonesFormulario);

        // Layout principal
        VBox layout = new VBox(15, vboxGrilla, vboxTaller);
        layout.setPadding(new Insets(15));

        // Mostrar la interfaz
        Scene scene = new Scene(layout, 800, 600);
        stageTalleres.setScene(scene);
        stageTalleres.setResizable(false);
        stageTalleres.show();

        vboxTaller.setVisible(false);  // Inicialmente oculto
        vboxGrilla.setVisible(true);   // Inicialmente visible

        // Acciones de los botones
        btnNuevo.setOnAction(e -> mostrarFormulario(true));
        btnEditar.setOnAction(e -> cargarDatosTallerSeleccionado(formulario));
        btnEliminar.setOnAction(e -> eliminarTaller());
        btnListar.setOnAction(e -> listarTalleres());
        btnGuardar.setOnAction(e -> guardarTaller(formulario));
        btnCancelar.setOnAction(e -> cancelarFormulario(formulario));
        btnVolver.setOnAction(e -> cerrarVentana(stageTalleres));
    }

    private void inicializarTabla() {
        // Nombre
        TableColumn<Taller, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().talNombreProperty());

        // Fecha de Inicio
        TableColumn<Taller, Date> colFechaInicio = new TableColumn<>("Fecha de Inicio");
        colFechaInicio.setCellValueFactory(cellData -> cellData.getValue().talFechaInicioProperty());

        // Fecha de Cierre
        TableColumn<Taller, Date> colFechaCierre = new TableColumn<>("Fecha de Cierre");
        colFechaCierre.setCellValueFactory(cellData -> cellData.getValue().talFechaCierreProperty());

        // Cupo Máximo
        TableColumn<Taller, Integer> colCupoMaximo = new TableColumn<>("Cupo Máximo");
        colCupoMaximo.setCellValueFactory(cellData -> cellData.getValue().talCupoMaximoProperty().asObject());

        // ID Docente
        TableColumn<Taller, Integer> colDocente = new TableColumn<>("ID Docente");
        colDocente.setCellValueFactory(cellData -> cellData.getValue().idDocenteProperty().asObject());

        // Comisión Docente
        TableColumn<Taller, Double> colComision = new TableColumn<>("Comisión Docente");
        colComision.setCellValueFactory(cellData -> cellData.getValue().talComisionDocenteProperty().asObject());

        // Estado
        TableColumn<Taller, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().talEstadoProperty());

        // Agregamos las columnas a la tabla
        tablaTalleres.getColumns().clear();
        tablaTalleres.getColumns().addAll(colNombre, colFechaInicio, colFechaCierre, colCupoMaximo, colDocente, colComision, colEstado);
        cargarTalleres();
    }

    private void cargarTalleres() {
        List<Taller> talleres = tallerDAO.obtenerTalleresActivos();
        ObservableList<Taller> observableTalleres = FXCollections.observableArrayList(talleres);
        tablaTalleres.setItems(observableTalleres);
    }

    private void cargarDatosTallerSeleccionado(GridPane formulario) {
        tallerSeleccionado = tablaTalleres.getSelectionModel().getSelectedItem();
        if (tallerSeleccionado != null) {
            txtNombre.setText(tallerSeleccionado.getTalNombre());
            txtCupoMaximo.setText(String.valueOf(tallerSeleccionado.getTalCupoMaximo()));
            txtDocente.setText(String.valueOf(tallerSeleccionado.getIdDocente()));
            txtComisionDocente.setText(String.valueOf(tallerSeleccionado.getTalComisionDocente()));
            cmbEstado.setValue(tallerSeleccionado.getTalEstado());

            if (tallerSeleccionado.getTalFechaInicio() != null) {
                dpFechaInicio.setValue(new java.sql.Date(tallerSeleccionado.getTalFechaInicio().getTime()).toLocalDate());
            } else {
                dpFechaInicio.setValue(null);
            }

            if (tallerSeleccionado.getTalFechaCierre() != null) {
                dpFechaCierre.setValue(new java.sql.Date(tallerSeleccionado.getTalFechaCierre().getTime()).toLocalDate());
            } else {
                dpFechaCierre.setValue(null);
            }

            mostrarFormulario(true);
        } else {
            mostrarAlerta("Seleccione un taller", "Selecciona un taller para poder editarlo.");
        }
    }

    private void mostrarFormulario(boolean mostrar) {
        vboxGrilla.setDisable(mostrar);
        vboxTaller.setVisible(mostrar);

        btnNuevo.setDisable(mostrar);
        btnEditar.setDisable(mostrar);
        btnEliminar.setDisable(mostrar);
        btnVolver.setDisable(mostrar);

        txtNombre.setDisable(!mostrar);
        txtCupoMaximo.setDisable(!mostrar);
        txtDocente.setDisable(!mostrar);
        txtComisionDocente.setDisable(!mostrar);
        dpFechaInicio.setDisable(!mostrar);
        dpFechaCierre.setDisable(!mostrar);
        cmbEstado.setDisable(!mostrar);
    }

    private void guardarTaller(GridPane formulario) {
        String nombre = txtNombre.getText();
        int cupoMaximo = Integer.parseInt(txtCupoMaximo.getText());
        int docente = Integer.parseInt(txtDocente.getText());
        double comision = Double.parseDouble(txtComisionDocente.getText());
        String estado = cmbEstado.getValue();

        java.time.LocalDate fechaInicio = dpFechaInicio.getValue();
        java.time.LocalDate fechaCierre = dpFechaCierre.getValue();

        if (tallerSeleccionado != null) {
            tallerSeleccionado.setTalNombre(nombre);
            tallerSeleccionado.setTalCupoMaximo(cupoMaximo);
            tallerSeleccionado.setIdDocente(docente);
            tallerSeleccionado.setTalComisionDocente(comision);
            tallerSeleccionado.setTalEstado(estado);

            if (fechaInicio != null) {
                tallerSeleccionado.setTalFechaInicio(java.sql.Date.valueOf(fechaInicio));
            }
            if (fechaCierre != null) {
                tallerSeleccionado.setTalFechaCierre(java.sql.Date.valueOf(fechaCierre));
            }

            tallerDAO.editarTaller(tallerSeleccionado);
        } else {
            Taller nuevoTaller = new Taller();
            nuevoTaller.setTalNombre(nombre);
            nuevoTaller.setTalCupoMaximo(cupoMaximo);
            nuevoTaller.setIdDocente(docente);
            nuevoTaller.setTalComisionDocente(comision);
            nuevoTaller.setTalEstado(estado);

            if (fechaInicio != null) {
                nuevoTaller.setTalFechaInicio(java.sql.Date.valueOf(fechaInicio));
            }
            if (fechaCierre != null) {
                nuevoTaller.setTalFechaCierre(java.sql.Date.valueOf(fechaCierre));
            }

        }

        cancelarFormulario(formulario);
        cargarTalleres();
    }

    private void eliminarTaller() {
        Taller tallerSeleccionado = tablaTalleres.getSelectionModel().getSelectedItem();
        if (tallerSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirma eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar este taller?");
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    tallerDAO.eliminarTaller(tallerSeleccionado.getIdTaller());
                    cargarTalleres();
                }
            });
        } else {
            mostrarAlerta("Seleccionar taller", "Selecciona un taller para eliminar.");
        }
    }

    private void cancelarFormulario(GridPane formulario) {
        limpiarFormulario();
        mostrarFormulario(false);
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        cmbEstado.setValue(null);
    }



    private void listarTalleres() {
        cargarTalleres();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana(Stage stage) {
        stage.close();
    }
}
