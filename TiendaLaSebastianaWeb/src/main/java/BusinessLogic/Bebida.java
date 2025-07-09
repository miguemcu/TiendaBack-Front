package BusinessLogic;

import java.util.ArrayList;
import java.time.LocalDate;
import org.bson.Document;

public class Bebida extends Producto {

    public Bebida() {
    }

    public Bebida(String nombre, long id, double precio, double precioMayorista,
            LocalDate fechaDeVencimiento, ArrayList<String> etiquetas) {
        super(nombre, id, precio, precioMayorista, EnumTipoProd.BEBIDA, fechaDeVencimiento, etiquetas);
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
