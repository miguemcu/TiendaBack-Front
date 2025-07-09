package BusinessLogic;

import org.bson.Document;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public class Empleado {

    private String nombre;
    private String documento;

    public Empleado() {
    }

    public Empleado(String nombre, String documento) {
        this.nombre = nombre;
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public static Empleado fromDocument(Document doc) {
        return new Empleado(doc.getString("nombre"), doc.getString("documento"));
    }

    public Document toDocument() {
        Document nuevoEmpleado = new Document("nombre", nombre)
                .append("documento", documento);
        return nuevoEmpleado;
    }
}
