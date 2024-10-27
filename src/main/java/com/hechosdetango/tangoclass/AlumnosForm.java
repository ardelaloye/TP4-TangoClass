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

import java.util.Date;

public class AlumnosForm {

    private Alumno alumno;
    private TextField txtApellido, txtNombre, txtDNI, txtDomicilio, txtTelefono, txtEmail;
    private DatePicker datePickerFechaNacimiento;

    private ComboBox<String> comboEstado;
    private Button btnAceptar, btnCancelar;

    // Constructor para agregar un nuevo alumno
    public AlumnosForm(Stage parentStage) {
        this.alumno = new Alumno();         // El alumno es nuevo
        mostrarFormulario(parentStage, "Agregar Alumno");
    }

    // Constructor para editar un alumno existente
    public AlumnosForm(Stage parentStage, Alumno alumno) {
        this.alumno = alumno;               // El Alumno que se va a editar
        mostrarFormulario(parentStage, "Editar Alumno");
    }

    private void mostrarFormulario(Stage parentStage, String titulo) {
        Stage formularioStage = new Stage();
        formularioStage.setTitle(titulo);
        datePickerFechaNacimiento = new DatePicker();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Campos a mostrarse en el formulario
        txtApellido = new TextField(alumno.getAluApellido());
        txtNombre = new TextField(alumno.getAluNombre());
        txtDNI = new TextField(alumno.getAluDNI());
        txtDomicilio = new TextField(alumno.getAluDomicilio());
        txtTelefono = new TextField(alumno.getAluTelefono());
        txtEmail = new TextField(alumno.getAluEmail());

        if (alumno.getAluFechaNacimiento() != null) {
            datePickerFechaNacimiento.setValue(new java.sql.Date(alumno.getAluFechaNacimiento().getTime()).toLocalDate());
        } else {
            datePickerFechaNacimiento.setValue(null);
        }

        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Activo", "Inactivo");
        comboEstado.setValue(alumno.getAluEstado());

        btnAceptar = new Button("Aceptar");
        btnCancelar = new Button("Cancelar");

        // Añadimos los campos al grid
        grid.add(new Label("Apellido:"), 0, 0);
        grid.add(txtApellido, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("DNI:"), 0, 2);
        grid.add(txtDNI, 1, 2);
        grid.add(new Label("Domicilio:"), 0, 3);
        grid.add(txtDomicilio, 1, 3);
        grid.add(new Label("Teléfono:"), 0, 4);
        grid.add(txtTelefono, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(txtEmail, 1, 5);
        grid.add(new Label("Fecha de Nacimiento:"), 0, 6);
        grid.add(datePickerFechaNacimiento, 1, 6);
        grid.add(new Label("Estado:"), 0, 7);
        grid.add(comboEstado, 1, 7);

        // Botones para aceptar o cancelar
        grid.add(btnAceptar, 0, 8);
        grid.add(btnCancelar, 1, 8);

        // Accion del Boton "Aceptar"
        btnAceptar.setOnAction(e -> {
            if (validarFormulario()) {
                guardarAlumno();
                formularioStage.close();
            } else {
                mostrarAlerta("Error", "Por favor, completa todos los campos.");
            }
        });

        // Accion del Boton "Cancelar"
        btnCancelar.setOnAction(e -> formularioStage.close());

        Scene scene = new Scene(grid, 400, 400);
        formularioStage.setScene(scene);
        formularioStage.initOwner(parentStage);
        formularioStage.showAndWait();
    }

    private boolean validarFormulario() {
        // Validamos que no haya campos vacíos
        return !txtApellido.getText().isEmpty() && !txtNombre.getText().isEmpty()
                && !txtDNI.getText().isEmpty() && !txtDomicilio.getText().isEmpty()
                && !txtTelefono.getText().isEmpty() && !txtEmail.getText().isEmpty()
                && datePickerFechaNacimiento.getValue() != null;
    }

    private void guardarAlumno() {
        alumno.setAluApellido(txtApellido.getText());
        alumno.setAluNombre(txtNombre.getText());
        alumno.setAluDNI(txtDNI.getText());
        alumno.setAluDomicilio(txtDomicilio.getText());
        alumno.setAluTelefono(txtTelefono.getText());
        alumno.setAluEmail(txtEmail.getText());
        alumno.setAluEstado(comboEstado.getValue());

        Date fechaNacimiento = java.sql.Date.valueOf(datePickerFechaNacimiento.getValue());
        alumno.setAluFechaNacimiento(fechaNacimiento);

        OperacionesCRUD operacion = new OperacionesCRUD();
        if (alumno.getIdAlumno() == 0) {
            if (operacion.agregarAlumno(alumno)) {
                System.out.println("Alumno agregado correctamente.");
            } else {
                System.out.println("Error al agregar el Alumno.");
            }
        } else {
            if (operacion.editarAlumno(alumno)) {
                System.out.println("Alumno modificado correctamente.");
            } else {
                System.out.println("Error al editar el Alumno.");
            }
        }

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
