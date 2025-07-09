/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import BusinessLogic.Aseo;
import BusinessLogic.Bebida;
import BusinessLogic.Enlatado;
import BusinessLogic.Granos;
import BusinessLogic.Mecato;
import BusinessLogic.Producto;
import BusinessLogic.Utils;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author migue
 */
public class RepoProductos {

    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public RepoProductos() {
        var client = MongoClients.create("mongodb+srv://miguemcu:admin@clusterejemploinicial.rvaw771.mongodb.net/");
        this.database = client.getDatabase("TiendaLaSebastiana");
        this.collection = database.getCollection("Productos");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public Map<Producto, Integer> getProductos() throws Exception {
        try {
            var productosEncontrados = collection.find();
            Map<Producto, Integer> productos = new HashMap<>();
            for (Document doc : productosEncontrados) {
                var producto = Utils.getProductoMongo((Document) doc.get("producto"));
                int cantidad = doc.getInteger("cantidad");
                productos.put(producto, cantidad);
            }
            return productos;
        } catch (Exception e) {
            throw new Exception("Ha ocurrido un error, contacte al administrador: " + e.getMessage());
        }
    }

    public boolean añadirProducto(String tipoProducto, String nombre, long id,
            double precioMayorista, double precio, LocalDate fechaDeVencimiento,
            ArrayList<String> etiquetas, int cantidad) throws Exception {
        try {
            Bson filtro = Filters.or(
                    Filters.eq("producto.nombre", nombre),
                    Filters.eq("producto.id", id));

            Document productoExistente = collection.find(filtro).first();

            if (productoExistente != null) { // Si ya existe un producto con esos datos, no lo deja
                return false;
            }

            Producto producto = null;

            switch (tipoProducto.toUpperCase()) {
                case "ASEO":
                    producto = new Aseo(nombre, id, precioMayorista, precio,
                            fechaDeVencimiento, etiquetas);
                    break;
                case "BEBIDA":
                    producto = new Bebida(nombre, id, precioMayorista, precio,
                            fechaDeVencimiento, etiquetas);
                    break;
                case "MECATO":
                    producto = new Mecato(nombre, id, precioMayorista, precio,
                            fechaDeVencimiento, etiquetas);
                    break;
                case "ENLATADO":
                    producto = new Enlatado(nombre, id, precioMayorista, precio,
                            fechaDeVencimiento, etiquetas);
                    break;
                case "GRANOS":
                    producto = new Granos(nombre, id, precioMayorista, precio,
                            fechaDeVencimiento, etiquetas);
            }
            Document doc = new Document("producto", producto.toDocument()).
                    append("cantidad", cantidad);
            collection.insertOne(doc);

            return true;
        } catch (Exception e) {
            throw new Exception("Ha ocurrido un error, por favor contacte al administrador");
        }
    }

    public void editarCantidadProducto(Producto producto, int cantidad) throws Exception {
        try {
            Bson filtro = Filters.eq("producto.nombre", producto.getNombre());
            Bson cantidadNueva = Updates.set("cantidad", cantidad);
            collection.updateOne(filtro, cantidadNueva);
        } catch (Exception e) {
            throw new Exception("Ha ocurrido un error, por favor contacte al administrador");
        }
    }
    
    public boolean actualizarProducto(String tipoProducto, String nombre, long id,
                                      double precioMayorista, double precio, LocalDate fechaDeVencimiento,
                                      ArrayList<String> etiquetas, int cantidad) throws Exception {
        try {

            Bson filtro = Filters.eq("producto.id", id);

            Document productoExistente = collection.find(filtro).first();

            if (productoExistente == null) {
                throw new NoSuchElementException("Producto no encontrado");
            }

            ArrayList<Bson> updates = new ArrayList<>();

            updates.add(Updates.set("producto.nombre", nombre));
            updates.add(Updates.set("producto.precioMayorista", precioMayorista));
            updates.add(Updates.set("producto.precio", precio));
            updates.add(Updates.set("producto.fechaDeVencimiento", fechaDeVencimiento.toString()));
            updates.add(Updates.set("producto.etiquetas", etiquetas));
            updates.add(Updates.set("cantidad", cantidad));
            
            updates.add(Updates.set("producto.tipoProducto", tipoProducto.toUpperCase()));
            collection.updateOne(filtro, Updates.combine(updates)).getModifiedCount();
            return true;
        } catch (IllegalArgumentException e) {
             throw new IllegalArgumentException(e.getMessage());
        }
        catch (NoSuchElementException e) {
             throw new NoSuchElementException(e.getMessage());
        }
        catch (Exception e) {
            throw new Exception("Error inesperado, por favor contacte al administrador: " + e.getMessage());
        }
    }
    
    public boolean eliminarProducto(long id) throws Exception {
        try {
            Bson filtro = Filters.eq("producto.id", id);

            if (collection.deleteOne(filtro) == null){
                return false;
            }
            return true;
            
        } catch (Exception e) {
            throw new Exception("Ha ocurrido un error al eliminar el producto, por favor contacte al administrador: " + e.getMessage(), e);
        }
    }
}
