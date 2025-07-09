package BusinessLogic;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Sebastian
 */
import BusinessLogic.Empleado;
import java.util.ArrayList;
import BusinessLogic.Producto;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class Caja {

    private ArrayList<Venta> ventas;
    private Inventario inventario;

    public Caja() throws Exception {
        this.ventas = new ArrayList<>();
        this.inventario = new Inventario();
        this.ventas = new ArrayList<>();
    }
    
    public ArrayList<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(ArrayList<Venta> ventas) {
        this.ventas = ventas;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public ArrayList<Venta> obtenerVentasSegunPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        ArrayList<Venta> ventasFiltradas = ventas.stream()
                .filter(v -> {
                    LocalDate fechaVenta = v.getFecha().toLocalDate();
                    return !fechaVenta.isBefore(fechaInicio) && !fechaVenta.isAfter(fechaFin);
                })
                .collect(Collectors.toCollection(ArrayList::new));

        return ventasFiltradas;
    }
}
