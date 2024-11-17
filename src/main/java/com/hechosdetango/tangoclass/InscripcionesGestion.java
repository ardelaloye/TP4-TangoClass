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

public class InscripcionesGestion {
    private TableView<Inscripcion> tablaInscripciones;
    private TextField txtIdAlumno, txtIdTaller;
    private DatePicker dpFechaInscripcion, dpFechaBaja;
    private ComboBox<String> cmbEstado;
    private Button btnGuardar, btnCancelar, btnNuevo, btnEditar, btnEliminar, btnVolver, btnListar;

    private Inscripcion inscripcionSeleccionada;
    private InscripcionDAO inscripcionDAO;
    private VBox vboxGrilla, vboxInscripcion;

    private ComboBox<Taller> cmbTalleres;
    private ComboBox<Alumno> cmbAlumno;


    public InscripcionesGestion(Stage primaryStage) {
        Stage stageInscripciones = new Stage();
        stageInscripciones.setTitle("TangoClass - Gestión de Inscripciones");

        inscripcionDAO = new InscripcionDAO();
        tablaInscripciones = new TableView<>();
        inicializarTabla();
        cargarInscripciones();

        // Botones del ABM
        btnNuevo = new Button("Nuevo");
        btnEditar = new Button("Editar");
        btnEliminar = new Button("Eliminar");
        btnListar = new Button("Listar");
        btnVolver = new Button("Volver");

        // Grilla con las inscripciones existentes
        vboxGrilla = new VBox();
        HBox botonesGrilla = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnListar, btnVolver);
        botonesGrilla.setPadding(new Insets(0, 0, 10, 0));
        vboxGrilla.getChildren().addAll(botonesGrilla, tablaInscripciones);

        // VBox para el formulario con datos de una inscripción
        vboxInscripcion = new VBox();
        GridPane formulario = new GridPane();
        formulario.setVgap(10);
        formulario.setHgap(10);

        //txtIdAlumno = new TextField();
        cmbAlumno = new ComboBox<Alumno>();
        cargarAlumnosEnComboBox();

        //txtIdTaller = new TextField();
        cmbTalleres = new ComboBox<Taller>();
        cargarTalleresEnComboBox();

        dpFechaInscripcion = new DatePicker();
        dpFechaBaja = new DatePicker();
        cmbEstado = new ComboBox<>(FXCollections.observableArrayList("Activo", "Cancelado"));

        // Añadimos los campos al formulario
        formulario.add(new Label("Alumno:"), 0, 0);
        formulario.add(cmbAlumno, 1, 0);
        formulario.add(new Label("Taller:"), 0, 1);
        formulario.add(cmbTalleres, 1, 1);
        formulario.add(new Label("Fecha de Inscripción:"), 0, 2);
        formulario.add(dpFechaInscripcion, 1, 2);
        formulario.add(new Label("Fecha de Baja:"), 0, 3);
        formulario.add(dpFechaBaja, 1, 3);
        formulario.add(new Label("Estado:"), 0, 4);
        formulario.add(cmbEstado, 1, 4);

        // Confirmar o cancelar el alta o la modificación de una inscripción
        btnGuardar = new Button("Confirmar");
        btnCancelar = new Button("Cancelar");
        HBox botonesFormulario = new HBox(10, btnGuardar, btnCancelar);
        botonesFormulario.setPadding(new Insets(10, 0, 0, 0));
        vboxInscripcion.getChildren().addAll(formulario, botonesFormulario);

        // Layout principal
        VBox layout = new VBox(15, vboxGrilla, vboxInscripcion);
        layout.setPadding(new Insets(15));

        // Mostrar la interfaz
        Scene scene = new Scene(layout, 800, 600);
        stageInscripciones.setScene(scene);
        stageInscripciones.setResizable(false);
        stageInscripciones.show();

        vboxInscripcion.setVisible(false);  // Inicialmente oculto
        vboxGrilla.setVisible(true);   // Inicialmente visible

        // Acciones de los botones
        btnNuevo.setOnAction(e -> mostrarFormulario(true));
        btnEditar.setOnAction(e -> cargarDatosInscripcionSeleccionada(formulario));
        btnEliminar.setOnAction(e -> eliminarInscripcion());
        btnListar.setOnAction(e -> listarInscripciones());
        btnGuardar.setOnAction(e -> guardarInscripcion(formulario));
        btnCancelar.setOnAction(e -> cancelarFormulario(formulario));
        btnVolver.setOnAction(e -> cerrarVentana(stageInscripciones));
    }

    private void inicializarTabla() {
        // ID de la inscripcion
        TableColumn<Inscripcion, Integer> colIdInscripcion = new TableColumn<>("ID Inscripción");
        colIdInscripcion.setCellValueFactory(cellData -> cellData.getValue().idInscripcionProperty().asObject());

        // Nombre Alumno
        TableColumn<Inscripcion, String> colNombreAlumno = new TableColumn<>("Nombre Alumno");
        colNombreAlumno.setCellValueFactory(cellData -> {
            // Obtener el nombre completo del alumno a través del ID de alumno en la inscripción
            String nombreAlumno = AlumnoDAO.getAluNomApPorId(cellData.getValue().getIdAlumno());
            return new javafx.beans.property.SimpleStringProperty(nombreAlumno != null ? nombreAlumno : "Desconocido");
        });

        // Nombre Taller
        TableColumn<Inscripcion, String> colNombreTaller = new TableColumn<>("Nombre Taller");
        colNombreTaller.setCellValueFactory(cellData -> {
            // Obtener el nombre del taller a través del ID del taller en la inscripción
            String nombreTaller = TallerDAO.getTalNombrePorId(cellData.getValue().getIdTaller());
            return new javafx.beans.property.SimpleStringProperty(nombreTaller != null ? nombreTaller : "Desconocido");
        });

        // Fecha Inscripción
        TableColumn<Inscripcion, String> colFechaInscripcion = new TableColumn<>("Fecha Inscripción");
        colFechaInscripcion.setCellValueFactory(cellData -> {
            if (cellData.getValue().getInsFechaInscripcion() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getInsFechaInscripcion().toString());
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // Fecha Baja
        TableColumn<Inscripcion, String> colFechaBaja = new TableColumn<>("Fecha Baja");
        colFechaBaja.setCellValueFactory(cellData -> {
            if (cellData.getValue().getInsFechaBaja() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getInsFechaBaja().toString());
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // Estado
        TableColumn<Inscripcion, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().insEstadoProperty());

        // Agregamos las columnas a la tabla
        tablaInscripciones.getColumns().clear();
        tablaInscripciones.getColumns().addAll(colNombreAlumno, colNombreTaller, colFechaInscripcion, colFechaBaja, colEstado);
        cargarInscripciones();
    }


    private void cargarAlumnosEnComboBox() {
        AlumnoDAO alumnoDAO = new AlumnoDAO();
        List<Alumno> alumnos = alumnoDAO.obtenerAlumnosActivos();
        ObservableList<Alumno> alumnosObservable = FXCollections.observableArrayList();

        for (Alumno alumno : alumnos) {
            alumnosObservable.add(alumno);
        }

        cmbAlumno.setItems(alumnosObservable);

        cmbAlumno.setOnAction(event -> {
            Alumno seleccionado = cmbAlumno.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                System.out.println("ID Alumno seleccionado: " + seleccionado.getIdAlumno());
            }
        });
    }


    private void cargarTalleresEnComboBox() {
        TallerDAO tallerDAO = new TallerDAO();
        List<Taller> talleres = tallerDAO.obtenerTalleresActivos();
        ObservableList<Taller> talleresObservable = FXCollections.observableArrayList();

        for (Taller taller : talleres) {
            talleresObservable.add(taller);
        }

        cmbTalleres.setItems(talleresObservable);

        cmbTalleres.setOnAction(event -> {
            Taller seleccionado = cmbTalleres.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                System.out.println("ID Taller seleccionado: " + seleccionado.getIdTaller());
            }
        });
    }




    private void cargarInscripciones() {
        List<Inscripcion> inscripciones = inscripcionDAO.obtenerInscripcionesActivas();
        ObservableList<Inscripcion> observableInscripciones = FXCollections.observableArrayList(inscripciones);
        tablaInscripciones.setItems(observableInscripciones);
    }

    private void cargarDatosInscripcionSeleccionada(GridPane formulario) {
        inscripcionSeleccionada = tablaInscripciones.getSelectionModel().getSelectedItem();
        if (inscripcionSeleccionada != null) {
            cmbEstado.setValue(inscripcionSeleccionada.getInsEstado());

            if (inscripcionSeleccionada.getInsFechaInscripcion() != null) {
                dpFechaInscripcion.setValue(new java.sql.Date(inscripcionSeleccionada.getInsFechaInscripcion().getTime()).toLocalDate());
            } else {
                dpFechaInscripcion.setValue(null);
            }

            if (inscripcionSeleccionada.getInsFechaBaja() != null) {
                dpFechaBaja.setValue(new java.sql.Date(inscripcionSeleccionada.getInsFechaBaja().getTime()).toLocalDate());
            } else {
                dpFechaBaja.setValue(null);
            }

            AlumnoDAO alumnoDAO = new AlumnoDAO();
            Alumno alumno = alumnoDAO.obtenerAlumnoPorId(inscripcionSeleccionada.getIdAlumno());
            if (alumno != null) {
                cmbAlumno.setValue(alumno);
            }

            TallerDAO tallerDAO = new TallerDAO();
            Taller taller = tallerDAO.obtenerTallerPorId(inscripcionSeleccionada.getIdTaller());
            cmbTalleres.setValue(taller);

            mostrarFormulario(true);
        } else {
            mostrarAlerta("Seleccione una inscripción", "Selecciona una inscripción para poder editarla.");
        }
    }

    private void mostrarFormulario(boolean mostrar) {
        vboxGrilla.setDisable(mostrar);
        vboxInscripcion.setVisible(mostrar);
        btnNuevo.setDisable(mostrar);
        btnEditar.setDisable(mostrar);
        btnEliminar.setDisable(mostrar);
        btnVolver.setDisable(mostrar);
        dpFechaInscripcion.setDisable(!mostrar);
        dpFechaBaja.setDisable(!mostrar);
        cmbEstado.setDisable(!mostrar);
    }

    private void guardarInscripcion(GridPane formulario) {
        if (inscripcionSeleccionada == null) {
            inscripcionSeleccionada = new Inscripcion();
        }

        Alumno alumnoSeleccionado = cmbAlumno.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            inscripcionSeleccionada.setIdAlumno(alumnoSeleccionado.getIdAlumno());
        }

        Taller tallerSeleccionado = cmbTalleres.getSelectionModel().getSelectedItem();
        if (tallerSeleccionado != null) {
            inscripcionSeleccionada.setIdTaller(tallerSeleccionado.getIdTaller());
        }

        inscripcionSeleccionada.setInsEstado(cmbEstado.getValue());

        LocalDate fechaInscripcion = dpFechaInscripcion.getValue();
        if (fechaInscripcion != null) {
            inscripcionSeleccionada.setInsFechaInscripcion(java.sql.Date.valueOf(fechaInscripcion));
        }

        LocalDate fechaBaja = dpFechaBaja.getValue();
        if (fechaBaja != null) {
            inscripcionSeleccionada.setInsFechaBaja(java.sql.Date.valueOf(fechaBaja));
        }

        if (inscripcionSeleccionada.getIdInscripcion() == 0) {
            inscripcionDAO.agregarInscripcion(inscripcionSeleccionada);
        } else {
            inscripcionDAO.editarInscripcion(inscripcionSeleccionada);
        }

        cargarInscripciones();
        mostrarFormulario(false);
    }

    private void cancelarFormulario(GridPane formulario) {
        limpiarFormulario();
        mostrarFormulario(false);
    }

    private void limpiarFormulario() {
        dpFechaInscripcion.setValue(null);
        dpFechaBaja.setValue(null);
        cmbEstado.setValue(null);
        inscripcionSeleccionada = null;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void eliminarInscripcion() {
        Inscripcion inscripcionSeleccionada = tablaInscripciones.getSelectionModel().getSelectedItem();

        if (inscripcionSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro de que desea eliminar esta inscripción?");
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    inscripcionDAO.eliminarInscripcion(inscripcionSeleccionada.getIdInscripcion());
                    cargarInscripciones();
                }
            });
        } else {
            mostrarAlerta("Seleccione una inscripción", "Seleccione una inscripción para poder eliminarla.");
        }
    }

    private void listarInscripciones() {
        Stage vistaPreviaStage = new Stage();
        vistaPreviaStage.setTitle("Listado de Inscripciones activas");

        VBox vboxReporte = new VBox(10);
        vboxReporte.setPadding(new Insets(15));

        // Título del reporte
        Text titulo = new Text("Listado de Inscripciones Activas");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vboxReporte.getChildren().add(titulo);

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);

        // Títulos de las columnas del reporte
        grid.add(new Text("ID Alumno"), 0, 0);
        grid.add(new Text("ID Taller"), 1, 0);
        grid.add(new Text("Fecha Inscripción"), 2, 0);
        grid.add(new Text("Fecha Baja"), 3, 0);
        grid.add(new Text("Estado"), 4, 0);

        List<Inscripcion> inscripcionesActivas = inscripcionDAO.obtenerInscripcionesActivas();

        int row = 1;
        for (Inscripcion inscripcion : inscripcionesActivas) {
            grid.add(new Text(String.valueOf(inscripcion.getIdAlumno())), 0, row);
            grid.add(new Text(String.valueOf(inscripcion.getIdTaller())), 1, row);
            grid.add(new Text(inscripcion.getInsFechaInscripcion().toString()), 2, row);
            grid.add(new Text(inscripcion.getInsFechaBaja() != null ? inscripcion.getInsFechaBaja().toString() : ""), 3, row);
            grid.add(new Text(inscripcion.getInsEstado()), 4, row);
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

    private void cerrarVentana(Stage stageInscripciones) {
        stageInscripciones.close();
    }
}