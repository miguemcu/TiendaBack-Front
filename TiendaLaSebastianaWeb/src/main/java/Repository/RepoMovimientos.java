package Repository;

import BusinessLogic.Movimiento;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import org.bson.Document;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author migue
 */
public class RepoMovimientos {

    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public RepoMovimientos() {
        var client = MongoClients.create("mongodb+srv://miguemcu:admin@clusterejemploinicial.rvaw771.mongodb.net/");
        this.database = client.getDatabase("TiendaLaSebastiana");
        this.collection = database.getCollection("Movimientos");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public ArrayList<Movimiento> getMovimientos() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();

        try {
            for (Document doc : collection.find()) {
                Movimiento movimiento = new Movimiento();
                movimiento.fromDocument(doc);
                movimientos.add(movimiento);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("No se pudieron obtener los movimientos de la base de datos, llame al administrador.");
        }
        return movimientos;
    }

    public void agregarMovimiento(Movimiento movimiento) {
        try {
            Document doc = movimiento.toDocument();
            collection.insertOne(doc);
        } catch (Exception e) {
            throw new IllegalArgumentException("No se pudo agregar el movimiento. Llame al administrador.");
        }
    }

}
