package BusinessLogic;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Sebastian
 */
import BusinessLogic.DetalleVenta;
import BusinessLogic.Producto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import org.bson.Document;

public class Venta {

    private ArrayList<DetalleVenta> detalles;
    private double totalVenta;
    private double totalBruto;
    private double totalDescuento;
    private double totalIva;
    private LocalDateTime fecha;
    private long ID;

    public Venta() {
    }

    public Venta(ArrayList<DetalleVenta> detalles, double totalVenta, double totalBruto,
            double totalDescuento, double totalIva, LocalDateTime fecha, long ID) {
        this.ID = ID;
        this.detalles = detalles;
        this.totalVenta = totalVenta;
        this.totalBruto = totalBruto;
        this.totalDescuento = totalDescuento;
        this.totalIva = totalIva;
        this.fecha = fecha;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public ArrayList<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public double getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(double totalBruto) {
        this.totalBruto = totalBruto;
    }

    public double getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(double totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public double getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(double totalIva) {
        this.totalIva = totalIva;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Document toDocument() {
        Document ventaDocument = new Document("id", ID)
                .append("fecha", fecha.toString());

        ArrayList<Document> detallesDoc = new ArrayList<>();
        for (DetalleVenta detalle : detalles) {
            Document detalleDoc = new Document();
            Producto producto = detalle.getProducto();
            var docProducto = producto.toDocument();
            detalleDoc.append("producto", docProducto)
                    .append("cantidad", detalle.getCantidad())
                    .append("precioUnitario", Double.valueOf(detalle.getPrecioUnitario()))
                    .append("subtotalBruto", Double.valueOf(detalle.getSubtotalBruto()))
                    .append("iva", Double.valueOf(detalle.getIva()))
                    .append("descuento", Double.valueOf(detalle.getDescuento()))
                    .append("subtotalNeto", Double.valueOf(detalle.getSubtotalNeto()));

            detallesDoc.add(detalleDoc);
        }

        ventaDocument.append("detalles", detallesDoc)
                .append("totalVenta", totalVenta)
                .append("totalBruto", totalBruto)
                .append("totalDescuento", totalDescuento)
                .append("totalIVA", totalIva);
        return ventaDocument;
    }
    
    public static Venta fromDocument(Document doc, Long id){
        String fechaStr = doc.getString("fecha");
        double totalVenta = doc.getDouble("totalVenta");
        double totalBruto = doc.getDouble("totalBruto");
        double totalDescuento = doc.getDouble("totalDescuento");
        double totalIVA = doc.getDouble("totalIVA");

        ArrayList<Document> docDetalles = (ArrayList<Document>) doc.get("detalles");
        ArrayList<DetalleVenta> DetallesVenta = new ArrayList<>();

        if (docDetalles != null) {
            for (Document docDetalle : docDetalles) {
                Document docProducto = (Document) docDetalle.get("producto");
                Producto producto = Utils.getProductoMongo(docProducto);

                // Atributos del detalle de la venta
                int cantidad = docDetalle.getInteger("cantidad");
                double precioUnitario = docDetalle.getDouble("precioUnitario");
                double subtotalBrutoDetalle = docDetalle.getDouble("subtotalBruto");
                double ivaDetalle = docDetalle.getDouble("iva");
                double descuentoDetalle = docDetalle.getDouble("descuento");
                double subtotalNetoDetalle = docDetalle.getDouble("subtotalNeto");

                DetalleVenta detalleVenta = new DetalleVenta(producto, cantidad, precioUnitario,
                        subtotalBrutoDetalle, ivaDetalle, descuentoDetalle, subtotalNetoDetalle);
                DetallesVenta.add(detalleVenta);
            }
        }

        LocalDateTime fechaVenta = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        var venta = new Venta(DetallesVenta, totalVenta, totalBruto, totalDescuento, totalIVA, fechaVenta, id);
        return venta;
    }
}
