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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class AlumnosGestion {
    private TableView<Alumno> tablaAlumnos;
    private TextField txtApellido, txtNombre, txtDNI, txtDomicilio, txtTelefono, txtEmail;
    private DatePicker dpFechaNacimiento;
    private ComboBox<String> cmbEstado;
    private Button btnGuardar, btnCancelar, btnNuevo, btnEditar, btnEliminar, btnVolver, btnListar;

    private Alumno alumnoSeleccionado;
    private AlumnoDAO alumnoDAO;
    private VBox vboxGrilla, vboxAlumno;

    public AlumnosGestion(Stage primaryStage) {
        Stage stageAlumnos = new Stage();
        stageAlumnos.setTitle("TangoClass - Gestión de Alumnos");

        alumnoDAO = new AlumnoDAO();
        tablaAlumnos = new TableView<>();
        inicializarTabla();
        cargarAlumnos();

        // Botones del ABM
        btnNuevo = new Button("Nuevo");
        btnEditar = new Button("Editar");
        btnEliminar = new Button("Eliminar");
        btnListar = new Button("Listar");
        btnVolver = new Button("Volver");

        // Grilla con los alumnos existentes
        vboxGrilla = new VBox();
        HBox botonesGrilla = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnListar, btnVolver);
        botonesGrilla.setPadding(new Insets(0, 0, 10, 0));
        vboxGrilla.getChildren().addAll(botonesGrilla, tablaAlumnos);

        // VBox para el formulario donde se ingresan los datos de un alumno
        vboxAlumno = new VBox();
        GridPane formulario = new GridPane();
        formulario.setVgap(10);
        formulario.setHgap(10);

        txtApellido = new TextField();
        txtNombre = new TextField();
        txtDNI = new TextField();
        txtDomicilio = new TextField();
        txtTelefono = new TextField();
        txtEmail = new TextField();
        dpFechaNacimiento = new DatePicker();
        cmbEstado = new ComboBox<>(FXCollections.observableArrayList("Activo", "Inactivo"));

        // Añadimos los campos al formulario
        formulario.add(new Label("Apellido:"), 0, 0);
        formulario.add(txtApellido, 1, 0);
        formulario.add(new Label("Nombre:"), 0, 1);
        formulario.add(txtNombre, 1, 1);
        formulario.add(new Label("DNI:"), 0, 2);
        formulario.add(txtDNI, 1, 2);
        formulario.add(new Label("Domicilio:"), 0, 3);
        formulario.add(txtDomicilio, 1, 3);
        formulario.add(new Label("Teléfono:"), 0, 4);
        formulario.add(txtTelefono, 1, 4);
        formulario.add(new Label("Email:"), 0, 5);
        formulario.add(txtEmail, 1, 5);
        formulario.add(new Label("Fecha de Nacimiento:"), 0, 6);
        formulario.add(dpFechaNacimiento, 1, 6);
        formulario.add(new Label("Estado:"), 0, 7);
        formulario.add(cmbEstado, 1, 7);

        // Botones para confirmar o cancelar el alta o la modificación de un alumno
        btnGuardar = new Button("Confirmar");
        btnCancelar = new Button("Cancelar");
        HBox botonesFormulario = new HBox(10, btnGuardar, btnCancelar);
        botonesFormulario.setPadding(new Insets(10, 0, 0, 0));
        vboxAlumno.getChildren().addAll(formulario, botonesFormulario);

        // Layout principal
        VBox layout = new VBox(15, vboxGrilla, vboxAlumno);
        layout.setPadding(new Insets(15));

        // Mostrar la interfaz
        Scene scene = new Scene(layout, 800, 600);
        stageAlumnos.setScene(scene);
        stageAlumnos.setResizable(false);
        stageAlumnos.show();

        vboxAlumno.setVisible(false);  // Inicialmente oculto
        vboxGrilla.setVisible(true);   // Inicialmente visible

        // Acciones de los botones
        btnNuevo.setOnAction(e -> mostrarFormulario(true));
        btnEditar.setOnAction(e -> cargarDatosAlumnoSeleccionado(formulario));
        btnEliminar.setOnAction(e -> eliminarAlumno());
        btnListar.setOnAction(e -> listarAlumnos());
        btnGuardar.setOnAction(e -> guardarAlumno(formulario));
        btnCancelar.setOnAction(e -> cancelarFormulario(formulario));
        btnVolver.setOnAction(e -> cerrarVentana(stageAlumnos));
    }

    private void inicializarTabla() {
        // Apellido
        TableColumn<Alumno, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cellData -> cellData.getValue().aluApellidoProperty());

        // Nombre
        TableColumn<Alumno, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().aluNombreProperty());

        // Domicilio
        TableColumn<Alumno, String> colDomicilio = new TableColumn<>("Domicilio");
        colDomicilio.setCellValueFactory(cellData -> cellData.getValue().aluDomicilioProperty());

        // Teléfono
        TableColumn<Alumno, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().aluTelefonoProperty());

        // DNI
        TableColumn<Alumno, String> colDNI = new TableColumn<>("DNI");
        colDNI.setCellValueFactory(cellData -> cellData.getValue().aluDNIProperty());

        // Estado
        TableColumn<Alumno, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().aluEstadoProperty());

        // Agregamos las columnas a la tabla
        tablaAlumnos.getColumns().clear();
        tablaAlumnos.getColumns().addAll(colApellido, colNombre, colDomicilio, colTelefono, colDNI, colEstado);
        cargarAlumnos();
    }

    // Cargamos alumnos (sólo los que actualmente están activos) en la grilla
    private void cargarAlumnos() {
        List<Alumno> alumnos = alumnoDAO.obtenerAlumnosActivos();
        ObservableList<Alumno> observableAlumnos = FXCollections.observableArrayList(alumnos);
        tablaAlumnos.setItems(observableAlumnos);
    }

    // Cargamos datos del alumno seleccionado en el formulario
    private void cargarDatosAlumnoSeleccionado(GridPane formulario) {
        alumnoSeleccionado = tablaAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            txtApellido.setText(alumnoSeleccionado.getAluApellido());
            txtNombre.setText(alumnoSeleccionado.getAluNombre());
            txtDNI.setText(alumnoSeleccionado.getAluDNI());
            txtDomicilio.setText(alumnoSeleccionado.getAluDomicilio());
            txtTelefono.setText(alumnoSeleccionado.getAluTelefono());
            txtEmail.setText(alumnoSeleccionado.getAluEmail());
            cmbEstado.setValue(alumnoSeleccionado.getAluEstado());

            if (alumnoSeleccionado.getAluFechaNacimiento() != null) {
                dpFechaNacimiento.setValue(new java.sql.Date(alumnoSeleccionado.getAluFechaNacimiento().getTime()).toLocalDate());
            } else {
                dpFechaNacimiento.setValue(null);
            }

            mostrarFormulario(true);
        } else {
            mostrarAlerta("Seleccione un alumno", "Selecciona un alumno para poder editarlo.");
        }
    }

    // Mostramos el formulario de editar o dar de alta un alumno
    private void mostrarFormulario(boolean mostrar) {
        vboxGrilla.setDisable(mostrar);
        vboxAlumno.setVisible(mostrar);

        btnNuevo.setDisable(mostrar);
        btnEditar.setDisable(mostrar);
        btnEliminar.setDisable(mostrar);
        btnVolver.setDisable(mostrar);

        txtApellido.setDisable(!mostrar);
        txtNombre.setDisable(!mostrar);
        txtDNI.setDisable(!mostrar);
        txtDomicilio.setDisable(!mostrar);
        txtTelefono.setDisable(!mostrar);
        txtEmail.setDisable(!mostrar);
        dpFechaNacimiento.setDisable(!mostrar);
        cmbEstado.setDisable(!mostrar);
    }

    // Guardar datos del alumno
    private void guardarAlumno(GridPane formulario) {
        if (alumnoSeleccionado == null) {
            alumnoSeleccionado = new Alumno();
        }

        alumnoSeleccionado.setAluApellido(txtApellido.getText());
        alumnoSeleccionado.setAluNombre(txtNombre.getText());
        alumnoSeleccionado.setAluDNI(txtDNI.getText());
        alumnoSeleccionado.setAluDomicilio(txtDomicilio.getText());
        alumnoSeleccionado.setAluTelefono(txtTelefono.getText());
        alumnoSeleccionado.setAluEmail(txtEmail.getText());
        alumnoSeleccionado.setAluEstado(cmbEstado.getValue());

        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
        alumnoSeleccionado.setAluFechaNacimiento(java.sql.Date.valueOf(fechaNacimiento));

        if (alumnoSeleccionado.getIdAlumno() == 0) {
            alumnoDAO.agregarAlumno(alumnoSeleccionado);
        } else {
            alumnoDAO.editarAlumno(alumnoSeleccionado);
        }

        cargarAlumnos();
        mostrarFormulario(false);
    }

    // Cancela el alta o edicion de un alumno
    private void cancelarFormulario(GridPane formulario) {
        limpiarFormulario();
        mostrarFormulario(false);
    }

    private void limpiarFormulario() {
        txtApellido.clear();
        txtNombre.clear();
        txtDNI.clear();
        txtDomicilio.clear();
        txtTelefono.clear();
        txtEmail.clear();
        dpFechaNacimiento.setValue(null);
        cmbEstado.setValue(null);
        alumnoSeleccionado = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void eliminarAlumno() {
        Alumno alumnoSeleccionado = tablaAlumnos.getSelectionModel().getSelectedItem();

        if (alumnoSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirma eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar este alumno?");
            //confirmacion.setContentText("¿Estás seguro de que deseas eliminar este alumno?");
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    alumnoDAO.eliminarAlumno(alumnoSeleccionado.getIdAlumno());
                    cargarAlumnos();
                }
            });
        } else {
            mostrarAlerta("Seleccione un alumno", "Seleccione un alumno para poder eliminarlo.");
        }
    }

    private void listarAlumnos() {
        Stage vistaPreviaStage = new Stage();
        vistaPreviaStage.setTitle("Listado de Alumnos activos");

        VBox vboxReporte = new VBox(10);
        vboxReporte.setPadding(new Insets(15));

        // Título del reporte
        Text titulo = new Text("Listado de Alumnos Activos");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vboxReporte.getChildren().add(titulo);

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);

        // Títulos de las columnas del reporte
        grid.add(new Text("Apellido"), 0, 0);
        grid.add(new Text("Nombre"), 1, 0);
        grid.add(new Text("DNI"), 2, 0);
        grid.add(new Text("Domicilio"), 3, 0);
        grid.add(new Text("Teléfono"), 4, 0);
        grid.add(new Text("Estado"), 5, 0);

        List<Alumno> alumnosActivos = alumnoDAO.obtenerAlumnosActivos();

        int row = 1;
        for (Alumno alumno : alumnosActivos) {
            grid.add(new Text(alumno.getAluApellido()), 0, row);
            grid.add(new Text(alumno.getAluNombre()), 1, row);
            grid.add(new Text(alumno.getAluDNI()), 2, row);
            grid.add(new Text(alumno.getAluDomicilio()), 3, row);
            grid.add(new Text(alumno.getAluTelefono()), 4, row);
            grid.add(new Text(alumno.getAluEstado()), 5, row);
            row++;
        }

        vboxReporte.getChildren().add(grid);

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> vistaPreviaStage.close());
        vboxReporte.getChildren().add(btnCerrar);

        Scene scene = new Scene(vboxReporte, 600, 800);
        vistaPreviaStage.setScene(scene);
        vistaPreviaStage.setResizable(true);
        vistaPreviaStage.show();
    }

    private void cerrarVentana(Stage stageAlumnos) {
        stageAlumnos.close();
    }
}
