package BusinessLogic;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.Document;

/**
 *
 * @author Sebastian
 */
public class Aseo extends Producto {

    public Aseo() {
    }

    public Aseo(String nombre, long id, double precio, double precioMayorista, LocalDate fechaDeVencimiento, ArrayList<String> etiquetas) {
        super(nombre, id, precio, precioMayorista, EnumTipoProd.ASEO, fechaDeVencimiento, etiquetas);
    }

    @Override
    public String imprimirFicha() {
        return Utils.mostrarMenu(this);
    }

    @Override
    public Document toDocument() {
        return Utils.productoToDocument(this);
    }
}
