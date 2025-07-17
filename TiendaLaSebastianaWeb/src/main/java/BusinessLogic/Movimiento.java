/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusinessLogic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bson.Document;

/**
 *
 * @author migue
 */
public class Movimiento {

    private EnumTipoMovimiento tipo;
    private Long id;
    private String comentario;
    private LocalDateTime fecha;

    public Movimiento() {
    }

    public Movimiento(EnumTipoMovimiento tipo, Long id, String comentario) {
        this.tipo = tipo;
        this.id = id;
        this.comentario = comentario;
    }

    public EnumTipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Movimiento fromDocument(Document doc) {
        switch (doc.getString("tipoMovimiento").toUpperCase()) {
            case ("ADD"):
                this.setTipo(EnumTipoMovimiento.ADD);
                break;
            case("DELETE"):
                this.setTipo(EnumTipoMovimiento.DELETE);
                break;
            case("ADJUSTP"):
                this.setTipo(EnumTipoMovimiento.ADJUSTP);
                break;
            case("ADJUSTM"):
                this.setTipo(EnumTipoMovimiento.ADJUSTM);
                break;
        }
        this.setId(doc.getLong("id"));
        this.setComentario(doc.getString("comentario"));
        String fechaStr = doc.getString("fecha");
        if (fechaStr != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            this.fecha = LocalDateTime.parse(fechaStr, formatter);
        }
        return this;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("tipoMovimiento", this.tipo.name());
        doc.append("id", this.id);
        doc.append("comentario", this.comentario);
        if (fecha != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            doc.append("fecha", fecha.format(formatter));
        }
        return doc;
    }

}
