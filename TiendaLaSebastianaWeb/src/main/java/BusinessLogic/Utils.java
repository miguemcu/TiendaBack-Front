/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusinessLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import org.bson.BsonInt64;
import org.bson.Document;

/**
 *
 * @author migue
 */
public class Utils {

    public static Long generarIdUnico(ArrayList<Venta> ventas) {
        Random random = new Random();
        Long nuevoId;
        boolean existe;

        do {
            nuevoId = random.nextLong();
            if (nuevoId < 0) { // Por si me generó un número Negativo
                nuevoId = -nuevoId;
            }

            existe = false;
            for (Venta venta : ventas) {
                if (venta.getID() == nuevoId) {
                    existe = true;
                    break;
                }
            }
        } while (existe);

        return nuevoId;
    }

    public static String mostrarMenu(Producto producto) {
        StringBuilder ficha = new StringBuilder();
        ficha.append("=== Ficha Producto ===\n");
        ficha.append("Nombre: ").append(producto.getNombre()).append("\n");
        ficha.append("ID: ").append(producto.getId()).append("\n");
        ficha.append("Precio: $").append(producto.getPrecio()).append("\n");
        ficha.append("Precio Mayorista: $").append(producto.getPrecioMayorista()).append("\n");
        if (producto.getTipoProducto() != null) {
            LocalDate fechaDeVencimiento = producto.getFechaDeVencimiento();
            if (fechaDeVencimiento != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
                ficha.append("Fecha Vencimiento: ").append(fechaDeVencimiento.format(formatter)).append("\n");
            } else {
                ficha.append("Fecha Vencimiento: No especificada\n");
            }

            if (producto.getEtiquetas() == null) {
                ficha.append("El producto no tiene etiquetas.").append("\n");
            } else {
                ficha.append("Etiquetas:\n");
                for (String etiqueta : producto.getEtiquetas()) {
                    ficha.append("- ").append(etiqueta).append("\n");
                }
            }
        }
        return ficha.toString();
    }

    public static Producto getProductoMongo(Document producto) {
        String nombre = producto.getString("nombre");
        Long id = producto.getLong("id");
        Double precio = producto.getDouble("precio");
        Double precioMayorista = producto.getDouble("precioMayorista");
        String tipoProducto = producto.getString("tipoProducto");
        String fechaStr = producto.getString("fechaDeVencimiento");
        ArrayList<String> etiquetas = (ArrayList<String>) producto.get("etiquetas");
        
        LocalDate fechaDeVencimiento = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);

        switch (tipoProducto.toUpperCase()) {
            case ("ASEO"):
                Producto aseo = new Aseo(nombre, id, precioMayorista, precio,
                        fechaDeVencimiento, etiquetas);
                return aseo;
            case ("BEBIDA"):
                Producto bebida = new Bebida(nombre, id, precioMayorista, precio,
                        fechaDeVencimiento, etiquetas);
                return bebida;
            case ("MECATO"):
                Producto mecato = new Mecato(nombre, id, precioMayorista, precio,
                        fechaDeVencimiento, etiquetas);
                return mecato;
            case ("ENLATADO"):
                Producto enlatado = new Enlatado(nombre, id, precioMayorista, precio,
                        fechaDeVencimiento, etiquetas);
                return enlatado;
            case ("GRANOS"):
                Producto grano = new Granos(nombre, id, precioMayorista, precio,
                        fechaDeVencimiento, etiquetas);
                return grano;
        }
        return null;
    }
    
    public static Document productoToDocument(Producto producto){
        Document doc = new Document()
                .append("nombre", producto.getNombre())
                .append("id", new BsonInt64(producto.getId())) // Me aseguro de que no lo guarde como int, que siempre sea Long
                .append("precioMayorista", producto.getPrecioMayorista())
                .append("precio", producto.getPrecio())
                .append("tipoProducto", producto.getTipoProducto())
                .append("fechaDeVencimiento", producto.getFechaDeVencimiento().toString())
                .append("etiquetas", producto.getEtiquetas());
        return doc;
    }
}
