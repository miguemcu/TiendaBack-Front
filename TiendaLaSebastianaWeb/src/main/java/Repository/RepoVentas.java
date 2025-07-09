/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import BusinessLogic.DetalleVenta;
import BusinessLogic.Producto;
import BusinessLogic.Utils;
import BusinessLogic.Venta;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author migue
 */
public class RepoVentas {

    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public RepoVentas() {
        var client = MongoClients.create("mongodb+srv://miguemcu:admin@clusterejemploinicial.rvaw771.mongodb.net/");
        this.database = client.getDatabase("TiendaLaSebastiana");
        this.collection = database.getCollection("Ventas");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public ArrayList<Venta> getVentas() throws Exception {
        try {
            var ventasEncontradas = collection.find();
            ArrayList<Venta> ventas = new ArrayList<>();
            for (Document doc : ventasEncontradas) {
                Long id = doc.getLong("id");
                String fechaStr = doc.getString("fecha");
                ArrayList<Document> detallesDoc = (ArrayList<Document>) doc.get("detalles");
                ArrayList<DetalleVenta> detalles = new ArrayList<>();
                for (Document docDetalle : detallesDoc) {
                    // Atributos del producto contenido en el detalle
                    Document docProducto = (Document) docDetalle.get("producto");
                    var producto = Utils.getProductoMongo(docProducto);

                    // Atributos del detalle
                    int cantidad = docDetalle.getInteger("cantidad");
                    double precioUnitario = docDetalle.getDouble("precioUnitario");
                    double subtotalBruto = docDetalle.getDouble("subtotalBruto");
                    double iva = docDetalle.getDouble("iva");
                    double descuento = docDetalle.getDouble("descuento");
                    double subtotalNeto = docDetalle.getDouble("subtotalNeto");
                    DetalleVenta detalle = new DetalleVenta(producto, cantidad, precioUnitario, subtotalBruto,
                            iva, descuento, subtotalNeto);
                    detalles.add(detalle);
                }
                double totalVenta = doc.getDouble("totalVenta");
                double totalBruto = doc.getDouble("totalBruto");
                double totalDescuento = doc.getDouble("totalDescuento");
                double totalIVA = doc.getDouble("totalIVA");

                LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                Venta venta = new Venta(detalles, totalVenta, totalBruto, totalDescuento, totalIVA, fecha, id);
                ventas.add(venta);
            }
            return ventas;
        } catch (Exception e) {
            throw new Exception("Ha ocurrido un error, contacte al administrador: " + e.getMessage());
        }
    }
    
    public Venta getVenta(long id) throws Exception {
        try{
            Document doc = collection.find(Filters.eq("id", id)).first();

            if (doc == null) {
                throw new Exception("Venta no encontrada.");
            } else {
                return Venta.fromDocument(doc, id);
                
            }
        } catch (Exception e) {
            throw new Exception("Error inesperado: " + e.getMessage());
        }
    }

    public boolean agregarVenta(ArrayList<DetalleVenta> detalles, double totalVenta, double totalBruto,
            double totalDescuento, double totalIva, LocalDateTime fecha, long id) throws Exception {
        try {
            Venta venta = new Venta(detalles, totalVenta, totalBruto, totalDescuento, totalIva, fecha, id);
            collection.insertOne(venta.toDocument());
            return true;
        } catch (Exception e) {
            throw new Exception("Ha ocurrido un error, contacte al administrador: " + e.getMessage());
        }
    }
    public boolean eliminarVenta(Venta venta) throws Exception{
        try {
            Bson filtro = Filters.eq("id", venta.getID());
            

            if (collection.find(filtro).first() == null) {
                return false; // No existe esa venta
            }

            collection.deleteOne(filtro);
            return true;
        } catch (Exception e) {
            throw new Exception("Ha ocurrido un error, contacte al administrador: " + e.getMessage());
        }
    }

}
