module com.hechosdetango.tangoclass {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.hechosdetango.tangoclass to javafx.fxml;
    exports com.hechosdetango.tangoclass;
}