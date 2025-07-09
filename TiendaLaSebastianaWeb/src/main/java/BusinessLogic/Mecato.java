package BusinessLogic;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



import java.util.ArrayList;
import java.time.LocalDate;
import org.bson.Document;

/**
 *
 * @author Sebastian
 */
public class Mecato extends Producto {

    public Mecato() {
    }
    
    public Mecato(String nombre, long id, double precio, double precioMayorista,
                  LocalDate fechaDeVencimiento, ArrayList<String> etiquetas) {
        super(nombre,id,precio, precioMayorista, EnumTipoProd.MECATO, fechaDeVencimiento, etiquetas);
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

