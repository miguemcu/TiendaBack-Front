package BusinessLogic;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sebastian
 */
import BusinessLogic.Producto;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotalBruto;
    private double iva;
    private double descuento;
    private double subtotalNeto;

    public DetalleVenta() {
    }

    public DetalleVenta(Producto producto, int cantidad, double precioUnitario,
                        double subtotalBruto, double iva, double descuento, double subtotalNeto) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotalBruto = subtotalBruto;
        this.iva = iva;
        this.descuento = descuento;
        this.subtotalNeto = subtotalNeto;
    }

    // Getters y Setters

    public double getSubtotalNeto() {
        return subtotalNeto;
    }

    public void setSubtotalNeto(double subtotalNeto) {
        this.subtotalNeto = subtotalNeto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotalBruto() {
        return subtotalBruto;
    }

    public void setSubtotalBruto(double subtotalBruto) {
        this.subtotalBruto = subtotalBruto;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
