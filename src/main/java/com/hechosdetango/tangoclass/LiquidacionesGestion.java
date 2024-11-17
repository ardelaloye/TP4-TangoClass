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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

public class LiquidacionesGestion {
    private TableView<Liquidacion> tablaLiquidaciones;
    private Button btnGenerarLiquidaciones, btnRegistrarPago, btnVolver;
    private VBox vboxGrilla;
    private LiquidacionDAO liquidacionDAO;
    private TallerDAO tallerDAO;
    private CuotaDAO cuotaDAO;
    private InscripcionDAO inscripcionDAO;
    private ComboBox<Taller> cbTaller;

    public LiquidacionesGestion(Stage primaryStage) {
        // Inicialización de los DAO necesarios
        inscripcionDAO = new InscripcionDAO();
        cuotaDAO = new CuotaDAO();
        tallerDAO = new TallerDAO();
        liquidacionDAO = new LiquidacionDAO();


        Stage stageLiquidaciones = new Stage();
        stageLiquidaciones.setTitle("TangoClass - Gestión de Liquidaciones");

        tablaLiquidaciones = new TableView<>();
        inicializarTabla();

        // Botones del ABM
        btnGenerarLiquidaciones = new Button("Generar Liquidación");
        btnRegistrarPago = new Button("Registrar Pago");
        btnVolver = new Button("Volver");

        // ComboBox para seleccionar el taller al que se le realizará la liquidación
        cbTaller = new ComboBox<>();
        cargarTalleres();
        Label labelTaller = new Label("Seleccione el taller:");
        HBox tallerBox = new HBox(10, labelTaller, cbTaller);
        tallerBox.setPadding(new Insets(0, 0, 10, 0));
        tallerBox.setAlignment(Pos.CENTER_LEFT);

        // Cargar las liquidaciones al seleccionar un taller
        cbTaller.setOnAction(e -> cargarLiquidaciones());

        // Acciones de los botones
        btnGenerarLiquidaciones.setOnAction(e -> abrirVentanaGenerarLiquidaciones());
        btnRegistrarPago.setOnAction(e -> manejarRegistrarPago());
        btnVolver.setOnAction(e -> cerrarVentana(stageLiquidaciones));

        // Grilla con las liquidaciones existentes
        vboxGrilla = new VBox();
        HBox botonesGrilla = new HBox(10, btnGenerarLiquidaciones, btnRegistrarPago, btnVolver);
        botonesGrilla.setPadding(new Insets(0, 0, 10, 0));
        vboxGrilla.getChildren().addAll(botonesGrilla, tallerBox, tablaLiquidaciones);

        // Layout principal
        VBox layout = new VBox(15, vboxGrilla);
        layout.setPadding(new Insets(15));

        // Mostrar la interfaz
        Scene scene = new Scene(layout, 800, 600);
        stageLiquidaciones.setScene(scene);
        stageLiquidaciones.setResizable(false);
        stageLiquidaciones.show();

        cargarLiquidaciones();
    }

    private void inicializarTabla() {
        // Configuración de las columnas de la tabla
        TableColumn<Liquidacion, Integer> colId = new TableColumn<>("ID Liquidación");
        colId.setCellValueFactory(cellData -> cellData.getValue().idLiquidacionProperty().asObject());

        TableColumn<Liquidacion, Double> colIngresos = new TableColumn<>("Ingresos Taller");
        colIngresos.setCellValueFactory(cellData -> cellData.getValue().liqIngresosTallerProperty().asObject());

        TableColumn<Liquidacion, Double> colComision = new TableColumn<>("Comisión Docente");
        colComision.setCellValueFactory(cellData -> cellData.getValue().liqComisionDocenteProperty().asObject());

        TableColumn<Liquidacion, Double> colImporte = new TableColumn<>("Importe Liquidar");
        colImporte.setCellValueFactory(cellData -> cellData.getValue().liqImporteLiquidarProperty().asObject());

        TableColumn<Liquidacion, Date> colFechaPago = new TableColumn<>("Fecha de Pago");
        colFechaPago.setCellValueFactory(cellData -> cellData.getValue().liqFechaPagoProperty());

        TableColumn<Liquidacion, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> cellData.getValue().liqEstadoProperty());

        tablaLiquidaciones.getColumns().addAll(colId, colIngresos, colComision, colImporte, colFechaPago, colEstado);
    }

    // Botón "Generar Liquidaciones". Abrimos una nueva ventana para generar una nueva liquidación al taller seleccionado
    private void abrirVentanaGenerarLiquidaciones() {
        Taller tallerSeleccionado = cbTaller.getValue();

        if (tallerSeleccionado == null) {
            // Mostramos error si el usuario no selecciono un taller
            mostrarAlerta("Error", "Seleccione un taller");
        } else {
            // Nueva ventana para generar la liquidacion
            Stage stageGenerar = new Stage();
            stageGenerar.setTitle("Generar Liquidaciones");

            // Tabla para mostrar las cuotas de este taller que están pagadas pero no estan liquidadas (null en IdLiquidacion)
            TableView<Cuota> tablaCuotasPagas = new TableView<>();
            CuotaDAO cuotasNoLiq = new CuotaDAO();
            List<Cuota> cuotasPagasNoLiquidadas = cuotasNoLiq.obtenerCuotasPagasNoLiquidadas(tallerSeleccionado.getIdTaller());

            inicializarTablaGenerar(tablaCuotasPagas);
            ObservableList<Cuota> cuotasObservableList = FXCollections.observableArrayList(cuotasPagasNoLiquidadas);
            tablaCuotasPagas.setItems(cuotasObservableList);

            // Botones "Generar" y "Volver"
            Button btnGenerar = new Button("Generar");  // Genera la liquidación
            Button btnVolver = new Button("Volver");    // Cancela la generación de la liquidación

            btnVolver.setOnAction(e -> stageGenerar.close());
            Label lblImporteTotalCuotas = new Label("Importe total de cuotas: $0.00");
            Label lblImporteTotalLiquidar = new Label("Importe total a liquidar: $0.00");

            // Layout de la ventana
            VBox layoutGenerar = new VBox(15, tablaCuotasPagas,
                                 new HBox(10, lblImporteTotalCuotas),
                                 new HBox(10, lblImporteTotalLiquidar),
                                 new HBox(10, btnGenerar, btnVolver)
            );
            layoutGenerar.setPadding(new Insets(15));

            Scene sceneGenerar = new Scene(layoutGenerar, 600, 400);
            stageGenerar.setScene(sceneGenerar);
            stageGenerar.setResizable(false);
            stageGenerar.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stageGenerar.show();
        }
    }

    // Mostramos una tabla con todas las cuotas del taller seleccionado pagadas, pero no incluidas en ninguna liquidacion
    private void inicializarTablaGenerar(TableView<Cuota> tablaCuotasPagas) {
        // ID de la Inscripcion que generó la cuota
        TableColumn<Cuota, String> colIdInscripcion = new TableColumn<>("ID Insc.");
        colIdInscripcion.setCellValueFactory(cellData ->
                cellData.getValue().idInscripcionProperty().asString()
        );

        // Alumno
        TableColumn<Cuota, String> colNombreAlumno = new TableColumn<>("Alumno");
        colNombreAlumno.setCellValueFactory(cellData -> {
            int idInscripcion = cellData.getValue().getIdInscripcion();
            InscripcionDAO inscripcionDAO = new InscripcionDAO();
            Integer idAlumno = inscripcionDAO.obtenerIdAlumnoInscripcion(idInscripcion);
            String aluNomAp = AlumnoDAO.getAluNomApPorId(idAlumno);
            return new SimpleStringProperty(aluNomAp);
        });

        // Taller
        TableColumn<Cuota, String> colTallerNombre = new TableColumn<>("Taller");
        colTallerNombre.setCellValueFactory(cellData -> {
            int idInscripcion = cellData.getValue().getIdInscripcion();
            InscripcionDAO inscripcionDAO = new InscripcionDAO();
            Integer idTaller = inscripcionDAO.obtenerIdTallerInscripcion(idInscripcion);
            return new SimpleStringProperty(idTaller.toString());
        });

        TableColumn<Cuota, Double> colFechaPago = new TableColumn<>("Fecha de Pago");

        TableColumn<Cuota, String> colImporte = new TableColumn<>("Importe");
        colImporte.setCellValueFactory(cellData -> {
            return cellData.getValue().cuoImporteProperty().asString();
        });

        TableColumn<Cuota, Boolean> colSeleccionar = new TableColumn<>("Seleccionar");
        colSeleccionar.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccionar));

        tablaCuotasPagas.getColumns().clear();
        tablaCuotasPagas.getColumns().addAll(colIdInscripcion, colNombreAlumno, colImporte, colSeleccionar);
    }

    // Acción del botón que registra el pago de una liquidación
    private void manejarRegistrarPago() {
        // Registrar el pago de una liquidación seleccionada
    }

    // Cargar los talleres disponibles en el ComboBox
    private void cargarTalleres() {
        List<Taller> talleres = tallerDAO.obtenerTalleresActivos();
        cbTaller.setItems(FXCollections.observableArrayList(talleres));
    }

    // Cargar las liquidaciones para el taller seleccionado
    private void cargarLiquidaciones() {
        Taller tallerSeleccionado = cbTaller.getValue();
        if (tallerSeleccionado != null) {
            List<Liquidacion> liquidaciones = liquidacionDAO.obtenerLiquidacionesPorTaller(tallerSeleccionado.getIdTaller());
            tablaLiquidaciones.setItems(FXCollections.observableArrayList(liquidaciones));
        }
    }

    // Muestra mensajes en pantalla
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Cerrar la ventana
    private void cerrarVentana(Stage stageLiquidaciones) {
        stageLiquidaciones.close();
    }
}
