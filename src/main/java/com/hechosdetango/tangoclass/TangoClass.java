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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class TangoClass extends Application {
    public static void main(String[] args) {
        if (Conexion.conectar()) { // Conectamos a la base de datos
            launch(args);
        } else {
            mostrarAlerta("Error de Conexión", "Ocurrió un error al intentar conectar con la base de datos.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TangoClass - Sistema de Gestión Escuela Hechos de Tango");

        int Ancho = 150;  // Ancho de los botones
        int Alto = 50;    // Alto de los botones

        // Crear los botones
        Button[] botones = new Button[]{
                new Button("Alumnos"),
                new Button("Talleres"),
                new Button("Docentes"),
                new Button("Inscripciones"),
                new Button("Cuotas"),
                new Button("Clases"),
                new Button("Liquidaciones"),
                new Button("Acerca de..."),
                new Button("Salir")
        };

        // Tamaño de los botones
        for (Button boton : botones) {
            boton.setPrefWidth(Ancho);
            boton.setPrefHeight(Alto);
        }

        // Configuración del GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);   // Espacio vertical entre filas
        grid.setHgap(10);   // Espacio horizontal entre botones

        // Agregar los botones al GridPane
        grid.add(botones[0], 0, 0); // Alumnos
        grid.add(botones[2], 1, 0); // Docentes
        grid.add(botones[1], 2, 0); // Talleres

        grid.add(botones[3], 0, 1); // Inscripciones
        grid.add(botones[4], 1, 1); // Cuotas
        grid.add(botones[6], 2, 1); // Liquidaciones

        grid.add(botones[5], 0, 2); // Clases
        grid.add(botones[7], 1, 2); // Probar conexión
        grid.add(botones[8], 2, 2); // Salir

        VBox vbox = new VBox();
        vbox.setSpacing(100);

        // Cargar y agregar el logo
        InputStream logoStream = getClass().getResourceAsStream("/logo.png");
        if (logoStream == null) {
            System.out.println("No se pudo encontrar logo.png");
        } else {
            Image logoImage = new Image(logoStream);
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(500);
            logoView.setPreserveRatio(true);
            vbox.getChildren().add(logoView);
        }

        vbox.getChildren().add(grid);

        // Creamos la pantalla principal
        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Acciones para cada botón de gestión
        botones[0].setOnAction(e -> gestionarAlumnos(primaryStage));
        botones[1].setOnAction(e -> gestionarTalleres(primaryStage));
        botones[2].setOnAction(e -> gestionarDocentes(primaryStage));
        botones[3].setOnAction(e -> gestionarInscripciones(primaryStage));
        botones[4].setOnAction(e -> gestionarCuotas());
        botones[5].setOnAction(e -> gestionarClases());
        botones[6].setOnAction(e -> gestionarLiquidaciones());

        // Acción para el botón "Acerca De"
        botones[7].setOnAction(e -> mostrarInfo());

        // Acción para el botón "Salir"
        botones[8].setOnAction(e -> primaryStage.close());
    }

    // Acciones de los botones
    private void gestionarAlumnos(Stage primaryStage) {
        System.out.println("Gestión de Alumnos");
        new AlumnosGestion(primaryStage);
    }

    private void gestionarTalleres(Stage primaryStage) {
        System.out.println("Gestión de Talleres");
        new TallerGestion(primaryStage);
    }

    private void gestionarDocentes(Stage primaryStage) {
        System.out.println("Gestión de Docentes");
        new DocentesGestion(primaryStage);
    }

    private void gestionarInscripciones(Stage primaryStage) {
        System.out.println("Gestión de Inscripciones (En desarrollo)");
        new InscripcionesGestion(primaryStage);
    }

    private void gestionarCuotas() {
        System.out.println("Gestión de Cuotas (En desarrollo");
    }

    private void gestionarClases() {
        System.out.println("Gestión de Clases (En desarrollo");
    }

    private void gestionarLiquidaciones() {
        System.out.println("Gestión de Liquidaciones (En desarrollo)");
    }

    // Mostrar Info
    private void mostrarInfo() {
            mostrarAlerta("Acerca de...", "TangoClass. Desarrollado por Ariel Delaloye (VINF011381), para el TP3 del Seminario de Práctica de Informática, Universidad Siglo 21, Octubre de 2024");
    }

    // Para mostrar mensajes en pantalla
    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
