package BusinessLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import org.bson.Document;

public abstract class Producto {

    private String nombre;
    private long id;
    private double precioMayorista;
    private double precio;
    private EnumTipoProd tipoProducto;
    private LocalDate fechaDeVencimiento;
    private ArrayList<String> etiquetas;

    public Producto() {
    }

    public Producto(String nombre, long id, double precioMayorista, double precio, EnumTipoProd tipoProducto, LocalDate fechaDeVencimiento,
            ArrayList<String> etiquetas) {
        this.nombre = nombre;
        this.id = id;
        this.precioMayorista = precioMayorista;
        this.precio = precio;
        this.tipoProducto = tipoProducto;
        this.fechaDeVencimiento = fechaDeVencimiento;
        this.etiquetas = etiquetas;
    }

    public EnumTipoProd getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(EnumTipoProd tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrecioMayorista() {
        return precioMayorista;
    }

    public void setPrecioMayorista(double precioMayorista) {
        this.precioMayorista = precioMayorista;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaDeVencimiento() {
        return fechaDeVencimiento;
    }

    public void setFechaDeVencimiento(LocalDate fechaDeVencimiento) {
        this.fechaDeVencimiento = fechaDeVencimiento;
    }

    public ArrayList<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(ArrayList<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public abstract String imprimirFicha();

    public abstract Document toDocument();

}
