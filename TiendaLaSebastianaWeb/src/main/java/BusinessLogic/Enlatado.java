package BusinessLogic;


import java.util.ArrayList;
import java.time.LocalDate;
import org.bson.Document;

/**
 *
 * @author Sebastian
 */
public class Enlatado extends Producto {
    public Enlatado() {
    }
    
    public Enlatado(String nombre, long id, double precio, double precioMayorista,
                  LocalDate fechaDeVencimiento, ArrayList<String> etiquetas) {
        super(nombre,id,precio, precioMayorista, EnumTipoProd.ENLATADO, fechaDeVencimiento, etiquetas);
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


