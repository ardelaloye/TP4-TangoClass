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

import java.util.List;

public class DocentesGestion {

    private TableView<Docente> tablaDocentes;
    private TextField txtApellido, txtNombre, txtDNI, txtTelefono, txtEmail;
    private ComboBox<String> cmbEstado;
    private Button btnGuardar, btnCancelar, btnNuevo, btnEditar, btnEliminar, btnVolver, btnListar;

    private Docente docenteSeleccionado;
    private DocenteDAO docenteDAO;
    private VBox vboxGrilla, vboxDocente;

    public DocentesGestion(Stage primaryStage) {
        Stage stageDocentes = new Stage();
        stageDocentes.setTitle("TangoClass - Gestión de Docentes");

        docenteDAO = new DocenteDAO();
        tablaDocentes = new TableView<>();
        inicializarTabla();
        cargarDocentes();

        // Botones del ABM
        btnNuevo = new Button("Nuevo");
        btnEditar = new Button("Editar");
        btnEliminar = new Button("Eliminar");
        btnListar = new Button("Listar");
        btnVolver = new Button("Volver");

        // Grilla con los docentes existentes
        vboxGrilla = new VBox();
        HBox botonesGrilla = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnListar, btnVolver);
        botonesGrilla.setPadding(new Insets(0, 0, 10, 0));
        vboxGrilla.getChildren().addAll(botonesGrilla, tablaDocentes);

        // VBox para el formulario de datos de un docente
        vboxDocente = new VBox();
        GridPane formulario = new GridPane();
        formulario.setVgap(10);
        formulario.setHgap(10);

        txtApellido = new TextField();
        txtNombre = new TextField();
        txtDNI = new TextField();
        txtTelefono = new TextField();
        txtEmail = new TextField();
        cmbEstado = new ComboBox<>(FXCollections.observableArrayList("Activo", "Inactivo"));

        // Añadimos los campos al formulario
        formulario.add(new Label("Apellido:"), 0, 0);
        formulario.add(txtApellido, 1, 0);
        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);
        formulario.add(new Label("DNI:"), 0, 2);
        formulario.add(txtDNI, 1, 2);
        formulario.add(new Label("Teléfono:"), 0, 3);
        formulario.add(txtTelefono, 1, 3);
        formulario.add(new Label("Email:"), 0, 4);
        formulario.add(txtEmail, 1, 4);
        formulario.add(new Label("Estado:"), 0, 5);
        formulario.add(cmbEstado, 1, 5);

        // Botones para confirmar o cancelar el alta o la modificación de un docente
        btnGuardar = new Button("Confirmar");
        btnCancelar = new Button("Cancelar");
        HBox botonesFormulario = new HBox(10, btnGuardar, btnCancelar);
        botonesFormulario.setPadding(new Insets(10, 0, 0, 0));
        vboxDocente.getChildren().addAll(formulario, botonesFormulario);

        // Layout principal
        VBox layout = new VBox(15, vboxGrilla, vboxDocente);
        layout.setPadding(new Insets(15));

        // Mostrar la interfaz
        Scene scene = new Scene(layout, 800, 600);
        stageDocentes.setScene(scene);
        stageDocentes.setResizable(false);
        stageDocentes.show();

        vboxDocente.setVisible(false);  // Inicialmente oculto
        vboxGrilla.setVisible(true);    // Inicialmente visible

        // Acciones de los botones
        btnNuevo.setOnAction(e -> mostrarFormulario(true));
        btnEditar.setOnAction(e -> cargarDatosDocenteSeleccionado(formulario));
        btnEliminar.setOnAction(e -> eliminarDocente());
        btnListar.setOnAction(e -> listarDocentes());
        btnGuardar.setOnAction(e -> guardarDocente(formulario));
        btnCancelar.setOnAction(e -> cancelarFormulario(formulario));
        btnVolver.setOnAction(e -> cerrarVentana(stageDocentes));
    }

    private void inicializarTabla() {
        // Apellido
        TableColumn<Docente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cellData -> cellData.getValue().docApellidoProperty());

        // Nombre
        TableColumn<Docente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().docNombreProperty());

        // DNI
        TableColumn<Docente, String> colDNI = new TableColumn<>("DNI");
        colDNI.setCellValueFactory(cellData -> cellData.getValue().docDNIProperty());

        // Teléfono
        TableColumn<Docente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().docTelefonoProperty());

        // Email
        TableColumn<Docente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> cellData.getValue().docEmailProperty());

        // Estado
        TableColumn<Docente, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().docEstadoProperty());

        // Agregamos las columnas a la tabla
        tablaDocentes.getColumns().clear();
        tablaDocentes.getColumns().addAll(colApellido, colNombre, colDNI, colTelefono, colEmail, colEstado);
        cargarDocentes();
    }

    private void cargarDocentes() {
        List<Docente> docentes = docenteDAO.obtenerDocentesActivos();
        ObservableList<Docente> observableDocentes = FXCollections.observableArrayList(docentes);
        tablaDocentes.setItems(observableDocentes);
    }

    private void cargarDatosDocenteSeleccionado(GridPane formulario) {
        docenteSeleccionado = tablaDocentes.getSelectionModel().getSelectedItem();
        if (docenteSeleccionado != null) {
            txtApellido.setText(docenteSeleccionado.getDocApellido());
            txtNombre.setText(docenteSeleccionado.getDocNombre());
            txtDNI.setText(docenteSeleccionado.getDocDNI());
            txtTelefono.setText(docenteSeleccionado.getDocTelefono());
            txtEmail.setText(docenteSeleccionado.getDocEmail());
            cmbEstado.setValue(docenteSeleccionado.getDocEstado());

            mostrarFormulario(true);
        } else {
            mostrarAlerta("Seleccione un docente", "Selecciona un docente para poder editarlo.");
        }
    }

    private void mostrarFormulario(boolean mostrar) {
        vboxGrilla.setDisable(mostrar);
        vboxDocente.setVisible(mostrar);

        btnNuevo.setDisable(mostrar);
        btnEditar.setDisable(mostrar);
        btnEliminar.setDisable(mostrar);
        btnVolver.setDisable(mostrar);

        txtApellido.setDisable(!mostrar);
        txtNombre.setDisable(!mostrar);
        txtDNI.setDisable(!mostrar);
        txtTelefono.setDisable(!mostrar);
        txtEmail.setDisable(!mostrar);
        cmbEstado.setDisable(!mostrar);
    }

    private void guardarDocente(GridPane formulario) {
        if (docenteSeleccionado == null) {
            docenteSeleccionado = new Docente();
        }

        docenteSeleccionado.setDocApellido(txtApellido.getText());
        docenteSeleccionado.setDocNombre(txtNombre.getText());
        docenteSeleccionado.setDocDNI(txtDNI.getText());
        docenteSeleccionado.setDocTelefono(txtTelefono.getText());
        docenteSeleccionado.setDocEmail(txtEmail.getText());
        docenteSeleccionado.setDocEstado(cmbEstado.getValue());

        if (docenteSeleccionado.getIdDocente() == 0) {
            docenteDAO.agregarDocente(docenteSeleccionado);
        } else {
            docenteDAO.editarDocente(docenteSeleccionado);
        }

        cargarDocentes();
        mostrarFormulario(false);
    }

    private void cancelarFormulario(GridPane formulario) {
        limpiarFormulario();
        mostrarFormulario(false);
    }

    private void limpiarFormulario() {
        txtApellido.clear();
        txtNombre.clear();
        txtDNI.clear();
        txtTelefono.clear();
        txtEmail.clear();
        cmbEstado.setValue(null);
        docenteSeleccionado = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void eliminarDocente() {
        Docente docenteSeleccionado = tablaDocentes.getSelectionModel().getSelectedItem();

        if (docenteSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirma eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar este docente?");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    docenteDAO.eliminarDocente(docenteSeleccionado.getIdDocente());
                    cargarDocentes();
                }
            });
        } else {
            mostrarAlerta("Selección requerida", "Por favor, selecciona un docente para eliminar.");
        }
    }

    private void listarDocentes() {
        cargarDocentes();
    }

    private void cerrarVentana(Stage stage) {
        stage.close();
    }
}
