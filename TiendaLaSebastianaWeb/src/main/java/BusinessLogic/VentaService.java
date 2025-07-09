/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusinessLogic;

import Repository.RepoVentas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author migue
 */
public class VentaService {
    
    private RepoVentas repositorio;
    
    public VentaService() {
        this.repositorio = new RepoVentas();
    }
    
    public RepoVentas getRepositorio() {
        return repositorio;
    }
    
    public void setRepositorio(RepoVentas repositorio) {
        this.repositorio = repositorio;
    }
    
    public ArrayList<Venta> getVentas() throws Exception {
        return repositorio.getVentas();
    }
    
    public Venta getVenta(long id) throws Exception {
        return repositorio.getVenta(id);
    }
    
    public boolean añadirVenta(Venta venta) throws Exception {
        return repositorio.agregarVenta(venta.getDetalles(), venta.getTotalVenta(),
                venta.getTotalBruto(), venta.getTotalDescuento(), venta.getTotalIva(),
                venta.getFecha(), venta.getID());
    }
    
    public boolean eliminarVenta(Venta venta, ProductoService productoService) throws Exception {
        for (DetalleVenta detalle : venta.getDetalles()) {
            detalle.setCantidad(detalle.getCantidad() * -1);
        }
        productoService.ajustarCantidades(venta);
        return repositorio.eliminarVenta(venta);
    }
    
    public ArrayList<Venta> obtenerVentasSegunPeriodo(LocalDate fechaInicio,
            LocalDate fechaFin) throws Exception {
        ArrayList<Venta> ventasFiltradas = this.getVentas().stream()
                .filter(v -> {
                    LocalDate fechaVenta = v.getFecha().toLocalDate();
                    return !fechaVenta.isBefore(fechaInicio) && !fechaVenta.isAfter(fechaFin);
                })
                .collect(Collectors.toCollection(ArrayList::new));
        
        return ventasFiltradas;
    }
    
    public double calculaPrecio(double cantidad, Producto producto) {
        double precio;
        if (cantidad >= 20) {
            precio = producto.getPrecioMayorista();
        } else {
            precio = producto.getPrecio();
        }
        return precio;
    }
    
    public double calcularPrecioBruto(double precio, int cantidad) {
        return (precio * cantidad);
    }
    
    public double calcularPrecioNeto(double subtotalBruto, double iva, double descuento) {
        var subtotalIVA = (subtotalBruto + subtotalBruto * iva);
        return (subtotalIVA - (subtotalIVA * descuento));
    }
    
    public double calcularIVAGenerado(double subtotalBruto, double iva) {
        return (subtotalBruto * iva);
    }
    
    public double calcularDescuentoGenerado(double subtotalBruto, double iva, double descuento) {
        var subtotalIVA = (subtotalBruto + subtotalBruto * iva);
        return (subtotalIVA * descuento);
    }
    
}
