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
import javafx.beans.property.StringProperty;

public class Docente {
    private int idDocente; // IDDocente (Primary Key)
    private SimpleStringProperty docApellido;
    private SimpleStringProperty docNombre;
    private SimpleStringProperty docDNI;
    private SimpleStringProperty docTelefono;
    private SimpleStringProperty docEmail;
    private SimpleStringProperty docEstado;

    // Constructor vacío
    public Docente() {
        this.docApellido = new SimpleStringProperty();
        this.docNombre = new SimpleStringProperty();
        this.docDNI = new SimpleStringProperty();
        this.docTelefono = new SimpleStringProperty();
        this.docEmail = new SimpleStringProperty();
        this.docEstado = new SimpleStringProperty();
    }

    // Getters y Setters
    public int getIdDocente() {return idDocente;}
    public void setIdDocente(int idDocente) {this.idDocente = idDocente; }

    public String getDocApellido() {return docApellido.get(); }
    public void setDocApellido(String docApellido) {this.docApellido.set(docApellido);}
    public StringProperty docApellidoProperty() {return docApellido;}

    public String getDocNombre() {return docNombre.get();}
    public void setDocNombre(String docNombre) {this.docNombre.set(docNombre);}
    public StringProperty docNombreProperty() {return docNombre;}


    public String getDocNombreCompleto() {return getDocNombre() + " " + getDocApellido();}

    public String getDocDNI() {return docDNI.get();}
    public void setDocDNI(String docDNI) {this.docDNI.set(docDNI);}
    public StringProperty docDNIProperty() {return docDNI;}

    public String getDocTelefono() {return docTelefono.get();}
    public void setDocTelefono(String docTelefono) {this.docTelefono.set(docTelefono);}
    public StringProperty docTelefonoProperty() {return docTelefono;}

    public String getDocEmail() {return docEmail.get();}
    public void setDocEmail(String docEmail) {this.docEmail.set(docEmail);}
    public StringProperty docEmailProperty() {return docEmail;}

    public String getDocEstado() {return docEstado.get();}
    public void setDocEstado(String docEstado) {this.docEstado.set(docEstado);}
    public StringProperty docEstadoProperty() {return docEstado;}
}
