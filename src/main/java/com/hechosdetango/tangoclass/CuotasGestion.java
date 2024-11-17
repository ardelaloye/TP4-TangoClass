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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CuotasGestion {
    private TableView<Cuota> tablaCuotas;
    private Button btnGenerarCuotas, btnRegistrarPago, btnVolver;
    private VBox vboxGrilla;
    private CuotaDAO cuotaDAO;
    private ValoresTallerDAO valoresTallerDAO;
    private InscripcionDAO inscripcionDAO;
    private ComboBox<Integer> cbPeriodo;

    public CuotasGestion(Stage primaryStage) {
        // Inicialización de los DAO necesarios
        cuotaDAO = new CuotaDAO();
        valoresTallerDAO = new ValoresTallerDAO();
        inscripcionDAO = new InscripcionDAO();

        Stage stageCuotas = new Stage();
        stageCuotas.setTitle("TangoClass - Gestión de Cuotas");

        tablaCuotas = new TableView<>();
        inicializarTabla();

        // Botones del ABM
        btnGenerarCuotas = new Button("Generar Cuotas");
        btnRegistrarPago = new Button("Registrar Pago");
        btnVolver = new Button("Volver");

        // Manejo del periodo que muestra la grilla
        cbPeriodo = new ComboBox<>();
        cargarPeriodos();  // Llenamos el ComboBox con los periodos disponibles
        cbPeriodo.setOnAction(e -> cargarCuotas(cbPeriodo.getValue()));
        Label labelPeriodo = new Label("Seleccione período:");
        HBox periodoBox = new HBox(10, labelPeriodo, cbPeriodo);
        periodoBox.setPadding(new Insets(0,0,10,0));
        periodoBox.setAlignment(Pos.CENTER_LEFT);

        // Acciones de los botones
        btnGenerarCuotas.setOnAction(e -> manejarGenerarCuotas());
        btnRegistrarPago.setOnAction(e -> manejarRegistrarPago());
        btnVolver.setOnAction(e -> cerrarVentana(stageCuotas));

        // Grilla con las cuotas existentes
        vboxGrilla = new VBox();
        HBox botonesGrilla = new HBox(10, btnGenerarCuotas, btnRegistrarPago, btnVolver);
        botonesGrilla.setPadding(new Insets(0, 0, 10, 0));
        vboxGrilla.getChildren().addAll(botonesGrilla, periodoBox, tablaCuotas);

        // Layout principal
        VBox layout = new VBox(15, vboxGrilla);
        layout.setPadding(new Insets(15));

        // Mostrar la interfaz
        Scene scene = new Scene(layout, 800, 600);
        stageCuotas.setScene(scene);
        stageCuotas.setResizable(false);
        stageCuotas.show();

        // Inicialmente mostramos las cuotas del periodo actual (AAAAMM)
        int periodoActual = YearMonth.now().getYear() * 100 + YearMonth.now().getMonthValue();
        cargarCuotas(periodoActual);
        cbPeriodo.setValue(periodoActual);


        vboxGrilla.setVisible(true);
    }


    private void inicializarTabla() {
        // ID de la Inscripcion que generó la cuota
        TableColumn<Cuota, String> colIdInscripcion = new TableColumn<>("ID Insc.");
        colIdInscripcion.setCellValueFactory(cellData ->
                cellData.getValue().idInscripcionProperty().asString()
        );

        // Periodo
        TableColumn<Cuota, String> colPeriodo = new TableColumn<>("Período");
        colPeriodo.setCellValueFactory(cellData -> {
            return cellData.getValue().cuoPeriodoProperty().asString();
        });

        // Importe
        TableColumn<Cuota, String> colImporte = new TableColumn<>("Importe");
        colImporte.setCellValueFactory(cellData -> {
            return cellData.getValue().cuoImporteProperty().asString();
        });

        // Estado
        TableColumn<Cuota, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> {
            return cellData.getValue().cuoEstadoProperty();
        });

        // Fecha de Vencimiento
        TableColumn<Cuota, String> colVencimiento = new TableColumn<>("Vence");
        colVencimiento.setCellValueFactory(cellData -> {
            return cellData.getValue().cuoFechaVencimientoProperty().asString();
        });

        // Alumno
        TableColumn<Cuota, String> colNombreAlumno = new TableColumn<>("Alumno");
        colNombreAlumno.setCellValueFactory(cellData -> {
            int idInscripcion = cellData.getValue().getIdInscripcion();
            InscripcionDAO inscripcionDAO = new InscripcionDAO();
            Inscripcion inscripcion = inscripcionDAO.obtenerInscripcionPorId(idInscripcion);

            // a partir del ID de la inscripcion, obtenemos el ID del alumno y consultamos el nombre y apellido
            if (inscripcion != null) {
                int idAlumno = inscripcion.getIdAlumno();
                String aluNomAp = AlumnoDAO.getAluNomApPorId(idAlumno);
                return new SimpleStringProperty(aluNomAp);
            } else {
                return new SimpleStringProperty("No se encontró la inscripción");
            }
        });


        // Taller
        TableColumn<Cuota, String> colTallerNombre = new TableColumn<>("Taller");
        colTallerNombre.setCellValueFactory(cellData -> {
            int idInscripcion = cellData.getValue().getIdInscripcion();
            InscripcionDAO inscripcionDAO = new InscripcionDAO();
            Inscripcion inscripcion = inscripcionDAO.obtenerInscripcionPorId(idInscripcion);

            // a partir del ID de la inscripcion, obtenemos el ID del taller y consultamos el nombre del mismo
            if (inscripcion != null) {
                int idTaller = inscripcion.getIdTaller();
                String talNombre = TallerDAO.getTalNombrePorId(idTaller);
                return new SimpleStringProperty(talNombre);
            } else {
                return new SimpleStringProperty("No se encontró el taller");
            }
        });

        // Agregamos las columnas a la tabla
        tablaCuotas.getColumns().clear();
        tablaCuotas.getColumns().addAll(colPeriodo, colIdInscripcion, colTallerNombre, colNombreAlumno, colVencimiento, colImporte, colEstado);
    }

    // Acción del boton "Generar Cuotas" - Este botón genera todas las cuotas para el período actual.
    // Si ya existen, no hace nada (no las modifica)
    private void manejarGenerarCuotas() {
        try {
            generarCuotas();
        } catch (SQLException ex) {
            mostrarAlerta("Error", "Ocurrió un error al generar las cuotas: " + ex.getMessage());
        }
    }

    // Accion del boton que registra el pago de una cuota por parte del alumno
    private void manejarRegistrarPago() {
        Cuota cuotaSeleccionada = tablaCuotas.getSelectionModel().getSelectedItem();

        if (cuotaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una cuota para registrar el pago.");
            return;
        }

        Stage modalStage = new Stage();
        modalStage.setTitle("Registrar Pago");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        Label lblFechaPago = new Label("Seleccione la fecha de pago:");
        DatePicker dpFechaPago = new DatePicker();
        Button btnAceptar = new Button("Aceptar");
        Button btnCancelar = new Button("Cancelar");

        HBox hboxButtons = new HBox(10, btnAceptar, btnCancelar);
        vbox.getChildren().addAll(lblFechaPago, dpFechaPago, hboxButtons);

        Scene scene = new Scene(vbox, 300, 150);
        modalStage.setScene(scene);
        modalStage.setResizable(false);

        btnAceptar.setOnAction(event -> {
            if (dpFechaPago.getValue() == null) {
                mostrarAlerta("Error", "Debe seleccionar una fecha de pago.");
                return;
            }

            java.sql.Date fechaPago = java.sql.Date.valueOf(dpFechaPago.getValue());

            try {
                cuotaSeleccionada.setCuoEstado("Pagada");
                cuotaSeleccionada.setCuoFechaPago(fechaPago);
                cuotaDAO.actualizarCuota(cuotaSeleccionada);

                cargarCuotas(cbPeriodo.getValue());
                mostrarAlerta("Éxito", "El pago ha sido registrado correctamente.");
            } catch (Exception ex) {
                mostrarAlerta("Error", "Ocurrió un error al registrar el pago: " + ex.getMessage());
            } finally {
                modalStage.close();
            }
        });

        btnCancelar.setOnAction(event -> modalStage.close());
        modalStage.showAndWait();
    }


    // Carga los períodos disponibles en el ComboBox
    private void cargarPeriodos() {
        List<Integer> periodos = cuotaDAO.obtenerPeriodos();
        cbPeriodo.setItems(FXCollections.observableArrayList(periodos));
    }

    // Generar las cuotas del periodo
    public void generarCuotas() throws SQLException {
        // Obtener el mes y año actual
        LocalDate fechaActual = LocalDate.now();
        int año = fechaActual.getYear();
        int mes = fechaActual.getMonthValue();

        // El periodo esta compuesto por el año y el mes, en un solo numero de 6 digitos (AAAAMM)
        int cuoPeriodo = año * 100 + mes;

        // Obtener las inscripciones activas en el mes actual (Si la inscripcion esta activa, debe pagar cuota)
        List<Inscripcion> inscripcionesActivas = inscripcionDAO.obtenerInscripcionesActivas();

        for (Inscripcion inscripcion : inscripcionesActivas) {
            if ("Activo".equals(inscripcion.getInsEstado()) && inscripcion.getInsFechaBaja() == null) {
                // Taller en el que se inscribio el alumno
                int idTaller = inscripcion.getIdTaller();

                // Valor de la cuota del taller en la fecha actual
                ValoresTaller valoresTaller = valoresTallerDAO.obtenerValorTaller(idTaller, Date.valueOf(fechaActual));
                double montoCuota = valoresTaller.obtenerCostoEnFecha(idTaller, java.sql.Date.valueOf(fechaActual));

                // Genera la cuota para esta inscripción
                Cuota cuota = new Cuota();
                cuota.setIdInscripcion(inscripcion.getIdInscripcion());
                cuota.setCuoPeriodo(cuoPeriodo);
                cuota.setCuoImporte(montoCuota);
                cuota.setCuoEstado("Impaga");       // La cuota se genera con estado 'Impaga'

                // Fecha de emisión y vencimiento - La fecha de emision es hoy. La fecha de vencimiento es el día 15 del mes
                cuota.setCuoFechaEmision(java.sql.Date.valueOf(fechaActual));
                cuota.setCuoFechaVencimiento(java.sql.Date.valueOf(fechaActual.withDayOfMonth(15)));

                cuotaDAO.agregarCuota(cuota);
            }
        }

        mostrarAlerta("Cuotas Generadas", "Las cuotas han sido generadas correctamente para el mes " + mes + "/" + año);
        cargarCuotas(cbPeriodo.getValue());
    }

    // Cargar las cuotas, filtrandolas por el periodo al que pertenecen
    private void cargarCuotas(Integer periodo) {
        List<Cuota> cuotas;
        if (periodo == null) {
            cuotas = cuotaDAO.obtenerCuotasActivas();  // Si no hay filtro, devuelve todas las cuotas
        } else {
            cuotas = cuotaDAO.obtenerCuotasPorPeriodo(periodo);  // Devuelve las cuotas del periodo solicitado
        }

        ObservableList<Cuota> observableCuotas = FXCollections.observableArrayList(cuotas);
        tablaCuotas.setItems(observableCuotas);
    }

    // Muestra mensajes en pantalla
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana(Stage stageCuotas) {
        stageCuotas.close();
    }
}

