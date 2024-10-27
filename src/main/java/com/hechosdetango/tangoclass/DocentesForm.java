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

public class DocentesForm {
    private Docente docente;
    private TextField txtApellido, txtNombre, txtDNI, txtTelefono, txtEmail;
    private ComboBox<String> comboEstado;
    private Button btnAceptar, btnCancelar;

    // Para agregar un nuevo docente
    public DocentesForm(Stage parentStage) {
        this.docente = new Docente();           // El docente es nuevo
        mostrarFormulario(parentStage, "Agregar Docente");
    }

    // Para editar un docente ya existente
    public DocentesForm(Stage parentStage, Docente docente) {
        this.docente = docente;             // El docente que se quiere editar
        mostrarFormulario(parentStage, "Editar Docente");
    }

    private void mostrarFormulario(Stage parentStage, String titulo) {
        Stage formularioStage = new Stage();
        formularioStage.setTitle(titulo);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Campos en ell formulario
        txtApellido = new TextField(docente.getDocApellido());
        txtNombre = new TextField(docente.getDocNombre());
        txtDNI = new TextField(docente.getDocDNI());
        txtTelefono = new TextField(docente.getDocTelefono());
        txtEmail = new TextField(docente.getDocEmail());

        comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Activo", "Inactivo");
        comboEstado.setValue(docente.getDocEstado());

        btnAceptar = new Button("Aceptar");
        btnCancelar = new Button("Cancelar");

        grid.add(new Label("Apellido:"), 0, 0);
        grid.add(txtApellido, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("DNI:"), 0, 2);
        grid.add(txtDNI, 1, 2);
        grid.add(new Label("Teléfono:"), 0, 3);
        grid.add(txtTelefono, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(txtEmail, 1, 4);
        grid.add(new Label("Estado:"), 0, 5);
        grid.add(comboEstado, 1, 5);

        // Botones para aceptar o cancelar
        grid.add(btnAceptar, 0, 6);
        grid.add(btnCancelar, 1, 6);

        // Accion del boton Aceptar
        btnAceptar.setOnAction(e -> {
            if (validarFormulario()) {
                guardarDocente();
                formularioStage.close();
            } else {
                mostrarAlerta("Error", "Por favor, completa todos los campos.");
            }
        });

        // Acción del boton Cancelar
        btnCancelar.setOnAction(e -> formularioStage.close());

        Scene scene = new Scene(grid, 400, 300);
        formularioStage.setScene(scene);
        formularioStage.initOwner(parentStage);
        formularioStage.showAndWait();
    }

    private boolean validarFormulario() {
        // Validamos que los campos no estén vacíos
        return !txtApellido.getText().isEmpty() && !txtNombre.getText().isEmpty()
                && !txtDNI.getText().isEmpty() && !txtTelefono.getText().isEmpty()
                && !txtEmail.getText().isEmpty();
    }

    private void guardarDocente() {
        docente.setDocApellido(txtApellido.getText());
        docente.setDocNombre(txtNombre.getText());
        docente.setDocDNI(txtDNI.getText());
        docente.setDocTelefono(txtTelefono.getText());
        docente.setDocEmail(txtEmail.getText());
        docente.setDocEstado(comboEstado.getValue());

        // Guardamos los valores que ingresó el usuario en la base de datos
        OperacionesCRUD operacion = new OperacionesCRUD();
        if (docente.getIdDocente() == 0) {
            if (operacion.agregarDocente(docente)) {
                System.out.println("Docente agregado correctamente.");
            } else {
                System.out.println("Error al agregar el docente.");
            }
        } else {
            if (operacion.editarDocente(docente)) {
                System.out.println("Docente modificado correctamente.");
            } else {
                System.out.println("Error al modificar el docente.");
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
