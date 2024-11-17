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

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ClasesGestion {
    private TableView<Clase> tablaClases;
    private TextField txtTema, txtObservaciones;
    private DatePicker dpFechaClase;
    private ComboBox<String> cmbEstado, cmbTaller;
    private Button btnGuardar, btnCancelar, btnNuevo, btnEditar, btnEliminar, btnVolver, btnListar;

    private Clase claseSeleccionada;
    private ClaseDAO claseDAO;
    private TallerDAO tallerDAO;
    private VBox vboxGrilla, vboxClase;

    public ClasesGestion(Stage primaryStage) {
        Stage stageClases = new Stage();
        stageClases.setTitle("TangoClass - Gestión de Clases");

        claseDAO = new ClaseDAO();
        tallerDAO = new TallerDAO();
        tablaClases = new TableView<>();
        inicializarTabla();
        cargarClases();

        // Botones del ABM
        btnNuevo = new Button("Nuevo");
        btnEditar = new Button("Editar");
        btnEliminar = new Button("Eliminar");
        btnListar = new Button("Listar");
        btnVolver = new Button("Volver");

        // Grilla con las clases existentes
        vboxGrilla = new VBox();
        HBox botonesGrilla = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnListar, btnVolver);
        botonesGrilla.setPadding(new Insets(0, 0, 10, 0));
        vboxGrilla.getChildren().addAll(botonesGrilla, tablaClases);

        // VBox para el formulario donde se ingresan los datos de una clase
        vboxClase = new VBox();
        GridPane formulario = new GridPane();
        formulario.setVgap(10);
        formulario.setHgap(10);

        txtTema = new TextField();
        txtObservaciones = new TextField();
        dpFechaClase = new DatePicker();
        cmbEstado = new ComboBox<>(FXCollections.observableArrayList("Activo", "Inactivo"));
        cmbTaller = new ComboBox<>();

        // Cargar los talleres activos en el ComboBox
        cargarTalleresActivos();

        // Añadimos los campos al formulario
        formulario.add(new Label("Tema:"), 0, 0);
        formulario.add(txtTema, 1, 0);
        formulario.add(new Label("Observaciones:"), 0, 1);
        formulario.add(txtObservaciones, 1, 1);
        formulario.add(new Label("Fecha de Clase:"), 0, 2);
        formulario.add(dpFechaClase, 1, 2);
        formulario.add(new Label("Estado:"), 0, 3);
        formulario.add(cmbEstado, 1, 3);
        formulario.add(new Label("Taller:"), 0, 4);
        formulario.add(cmbTaller, 1, 4);

        // confirmar o cancelar el alta o la modificación de una clase
        btnGuardar = new Button("Confirmar");
        btnCancelar = new Button("Cancelar");
        HBox botonesFormulario = new HBox(10, btnGuardar, btnCancelar);
        botonesFormulario.setPadding(new Insets(10, 0, 0, 0));
        vboxClase.getChildren().addAll(formulario, botonesFormulario);

        // Layout principal
        VBox layout = new VBox(15, vboxGrilla, vboxClase);
        layout.setPadding(new Insets(15));

        // Mostrar la interfaz
        Scene scene = new Scene(layout, 800, 600);
        stageClases.setScene(scene);
        stageClases.setResizable(false);
        stageClases.show();

        vboxClase.setVisible(false);  // Inicialmente oculto
        vboxGrilla.setVisible(true);   // Inicialmente visible

        // Acciones de los botones
        btnNuevo.setOnAction(e -> mostrarFormulario(true));
        btnEditar.setOnAction(e -> cargarDatosClaseSeleccionada(formulario));
        btnEliminar.setOnAction(e -> eliminarClase());
        btnListar.setOnAction(e -> listarClases());
        btnGuardar.setOnAction(e -> guardarClase(formulario));
        btnCancelar.setOnAction(e -> cancelarFormulario(formulario));
        btnVolver.setOnAction(e -> cerrarVentana(stageClases));
    }

    // Cargar los talleres activos en el ComboBox
    private void cargarTalleresActivos() {
        List<Taller> talleresActivos = tallerDAO.obtenerTalleresActivos();

        List<String> talleresNombres = new ArrayList<>();
        for (Taller taller : talleresActivos) {
            String tallerDisplay = taller.getIdTaller() + " - " + taller.getTalNombre();
            talleresNombres.add(tallerDisplay);
        }

        ObservableList<String> talleresObservable = FXCollections.observableArrayList(talleresNombres);
        cmbTaller.setItems(talleresObservable);
    }


    private void inicializarTabla() {
        // Nombre del taller
        TableColumn<Clase, String> colTaller = new TableColumn<>("Taller");
        colTaller.setCellValueFactory(cellData -> {
            String talNombre = TallerDAO.getTalNombrePorId(cellData.getValue().getIdTaller());
            return new SimpleStringProperty(talNombre);
        });
        tablaClases.getColumns().add(colTaller);
        cargarClases();
    }

    private void cargarClases() {
        List<Clase> clases = claseDAO.obtenerClasesActivas();
        ObservableList<Clase> clasesObservable = FXCollections.observableArrayList(clases);
        tablaClases.setItems(clasesObservable);
    }

    private void mostrarFormulario(boolean esNuevo) {
        vboxGrilla.setVisible(false);
        vboxClase.setVisible(true);
    }

    private void cargarDatosClaseSeleccionada(GridPane formulario) {
        claseSeleccionada = tablaClases.getSelectionModel().getSelectedItem();
        if (claseSeleccionada != null) {
            txtTema.setText(claseSeleccionada.getClaTema());
            txtObservaciones.setText(claseSeleccionada.getClaObservaciones());

            if (claseSeleccionada.getClaFechaClase() != null) {
                dpFechaClase.setValue(new java.sql.Date(claseSeleccionada.getClaFechaClase().getTime()).toLocalDate());
            } else {
                dpFechaClase.setValue(null);
            }

            cmbEstado.setValue(claseSeleccionada.getClaEstado());

            // Muestra el texto "ID - Nombre" del taller en el ComboBox
            String tallerDisplay = claseSeleccionada.getIdTaller() + " - " + TallerDAO.getTalNombrePorId(claseSeleccionada.getIdTaller());
            cmbTaller.setValue(tallerDisplay);
        }
    }

    private void guardarClase(GridPane formulario) {
        Clase clase = new Clase();
        clase.setClaTema(txtTema.getText());
        clase.setClaObservaciones(txtObservaciones.getText());
        clase.setClaFechaClase(java.sql.Date.valueOf(dpFechaClase.getValue()));
        clase.setClaEstado(cmbEstado.getValue());

        // Obtenemos el texto seleccionado del ComboBox
        String tallerSeleccionado = cmbTaller.getSelectionModel().getSelectedItem();

        if (tallerSeleccionado != null && !tallerSeleccionado.isEmpty()) {
            // Extraemos el ID del taller desde la cadena "ID - Nombre"
            String idTallerStr = tallerSeleccionado.split(" - ")[0];
            int idTaller = Integer.parseInt(idTallerStr);
            clase.setIdTaller(idTaller);
        } else {
            System.out.println("Por favor, seleccione un taller.");
            return;
        }

        if (claseDAO.agregarClase(clase)) {
            cancelarFormulario(formulario);
            cargarClases();
        } else {
            System.out.println("No se pudo guardar la clase.");
        }
    }


    private void eliminarClase() {
        if (claseSeleccionada != null) {
            if (claseDAO.eliminarClase(claseSeleccionada.getIdClase())) {
                cargarClases();
            } else {
                System.out.println("No se pudo eliminar la clase.");
            }
        }
    }

    private void listarClases() {
        cargarClases();
    }

    private void cancelarFormulario(GridPane formulario) {
        formulario.getChildren().clear();
        vboxGrilla.setVisible(true);
        vboxClase.setVisible(false);
    }

    private void cerrarVentana(Stage stage) {
        stage.close();
    }
}
