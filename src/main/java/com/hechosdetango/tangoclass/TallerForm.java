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

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TallerForm {
    private Taller taller;
    private ValoresTaller valoresTaller;
    private TextField txtNombre, txtCupoMaximo, txtComisionDocente;
    private ComboBox<String> comboDocente, comboEstado;
    private DatePicker datePickerInicio, datePickerCierre;
    private DatePicker datePickerValDesde, datePickerValHasta;
    private TextField txtValImporte;
    private Button btnAceptar, btnCancelar;
    private Map<String, Integer> docentesMap;

    // Constructor para agregar un nuevo taller
    public TallerForm(Stage parentStage) {
        this.taller = new Taller();
        this.valoresTaller = new ValoresTaller();
        this.docentesMap = new HashMap<>();
        mostrarFormulario(parentStage, "Agregar Taller");
    }

    // Constructor para editar un taller ya existente
    public TallerForm(Stage parentStage, Taller taller) {
        this.taller = taller;
        this.valoresTaller = new ValoresTaller();
        this.docentesMap = new HashMap<>();

        mostrarFormulario(parentStage, "Editar Taller");
    }

    private void mostrarFormulario(Stage parentStage, String titulo) {
        Stage formularioStage = new Stage();
        formularioStage.setTitle(titulo);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        txtNombre = new TextField(taller.getTalNombre());

        // Combo con los nombres de los docentes
        comboDocente = new ComboBox<>();
        cargarDocentes();
        if (taller.getIdDocente() != 0) {
            String nombreCompletoDocente = getDocenteNombreCompleto(taller.getIdDocente());
            comboDocente.setValue(nombreCompletoDocente);
        }

        // DatePicker para fechas de inicio y cierre del taller
        datePickerInicio = new DatePicker();
        datePickerCierre = new DatePicker();

        // Cupo máximo y comisión del docente
        txtCupoMaximo = new TextField(String.valueOf(taller.getTalCupoMaximo()));
        txtComisionDocente = new TextField(String.valueOf(taller.getTalComisionDocente()));

        // Combo para el estado del taller
        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Activo", "Inactivo");
        comboEstado.setValue(taller.getTalEstado());

        // Fechas de inicio y de cierre del taller
        if (taller.getTalFechaInicio() != null) {
            datePickerInicio.setValue(new java.sql.Date(taller.getTalFechaInicio().getTime()).toLocalDate());
        } else {
            datePickerInicio.setValue(null);
        }
        if (taller.getTalFechaCierre() != null) {
            datePickerCierre.setValue(new java.sql.Date(taller.getTalFechaCierre().getTime()).toLocalDate());
        } else {
            datePickerCierre.setValue(null);
        }

        // Campos para gestionar el costo del taller entre dos fechas
        datePickerValDesde = new DatePicker();
        datePickerValHasta = new DatePicker();
        txtValImporte = new TextField();

        // Botones para aceptar y cancelar
        btnAceptar = new Button("Aceptar");
        btnCancelar = new Button("Cancelar");

        // Agregamos los campos a la grilla
        grid.add(new Label("Nombre del Taller:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Cupo Máximo:"), 0, 1);
        grid.add(txtCupoMaximo, 1, 1);
        grid.add(new Label("Comisión del Docente:"), 0, 2);
        grid.add(txtComisionDocente, 1, 2);
        grid.add(new Label("Docente:"), 0, 3);
        grid.add(comboDocente, 1, 3);
        grid.add(new Label("Fecha de Inicio:"), 0, 4);
        grid.add(datePickerInicio, 1, 4);
        grid.add(new Label("Fecha de Cierre:"), 0, 5);
        grid.add(datePickerCierre, 1, 5);
        grid.add(new Label("Estado:"), 0, 6);
        grid.add(comboEstado, 1, 6);
        grid.add(new Label("Valor Desde:"), 0, 9);
        grid.add(datePickerValDesde, 1, 9);
        grid.add(new Label("Valor Hasta:"), 0, 10);
        grid.add(datePickerValHasta, 1, 10);
        grid.add(new Label("Importe:"), 0, 11);
        grid.add(txtValImporte, 1, 11);

        // Botones aceptar y cancelar
        grid.add(btnAceptar, 0, 13);
        grid.add(btnCancelar, 1, 13);

        // Acción del botón aceptar
        btnAceptar.setOnAction(e -> {
            if (validarFormulario() && validarValoresTaller()) {
                guardarTaller();
                formularioStage.close();
            } else {
                mostrarAlerta("Error", "Por favor, completa todos los campos.");
            }
        });

        // Acción del botón cancelar
        btnCancelar.setOnAction(e -> formularioStage.close());

        Scene scene = new Scene(grid, 400, 500);
        formularioStage.setScene(scene);
        formularioStage.initOwner(parentStage);
        formularioStage.showAndWait();
    }

    private void cargarDocentes() {
        List<Docente> listaDocentes = OperacionesCRUD.obtenerDocentesActivos();

        for (Docente docente : listaDocentes) {
            String nombreCompleto = docente.getDocNombreCompleto();
            comboDocente.getItems().add(nombreCompleto);
            docentesMap.put(nombreCompleto, docente.getIdDocente());
        }
    }

    private String getDocenteNombreCompleto(int idDocente) {
        for (Map.Entry<String, Integer> entry : docentesMap.entrySet()) {
            if (entry.getValue().equals(idDocente)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean validarFormulario() {
        return !txtNombre.getText().isEmpty() && !txtCupoMaximo.getText().isEmpty()
                && !txtComisionDocente.getText().isEmpty() && comboDocente.getValue() != null
                && datePickerInicio.getValue() != null && datePickerCierre.getValue() != null;
    }

    private boolean validarValoresTaller() {
        if (datePickerValDesde.getValue() == null || datePickerValHasta.getValue() == null || txtValImporte.getText().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(txtValImporte.getText());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void guardarTaller() {
        // Guardar Taller
        taller.setTalNombre(txtNombre.getText());
        taller.setTalCupoMaximo(Integer.parseInt(txtCupoMaximo.getText()));
        taller.setTalComisionDocente(Double.parseDouble(txtComisionDocente.getText()));

        // Obtener el ID del docente seleccionado
        String docenteSeleccionado = comboDocente.getValue();
        int idDocente = docentesMap.get(docenteSeleccionado);
        taller.setIdDocente(idDocente);

        // Convertir fechas de taller
        java.sql.Date fechaInicio = Date.valueOf(datePickerInicio.getValue());
        java.sql.Date fechaCierre = Date.valueOf(datePickerCierre.getValue());
        taller.setTalFechaInicio(fechaInicio);
        taller.setTalFechaCierre(fechaCierre);
        taller.setTalEstado(comboEstado.getValue());

        OperacionesCRUD operacion = new OperacionesCRUD();
        if (taller.getIdTaller() == 0) {
            if (operacion.agregarTaller(taller)) {
                System.out.println("Taller agregado correctamente.");
            } else {
                System.out.println("Error al agregar el taller.");
            }
        } else {
            if (operacion.editarTaller(taller)) {
                System.out.println("Taller modificado correctamente.");
            } else {
                System.out.println("Error al editar el taller.");
            }
        }

        // Guardar ValoresTaller solo si los campos están completos y válidos
        if (validarValoresTaller()) {
            valoresTaller.setIdTaller(taller.getIdTaller());
            valoresTaller.setValFechaDesde(Date.valueOf(datePickerValDesde.getValue()));
            valoresTaller.setValFechaHasta(Date.valueOf(datePickerValHasta.getValue()));
            valoresTaller.setValImporte(Double.parseDouble(txtValImporte.getText()));

            operacion.guardarValoresTaller(valoresTaller);
            System.out.println("Valores del taller guardados correctamente.");
        } else {
            mostrarAlerta("Error", "Por favor, completa todos los campos de valores del taller correctamente.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
