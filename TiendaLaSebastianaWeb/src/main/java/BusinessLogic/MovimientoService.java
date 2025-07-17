/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusinessLogic;

import Repository.RepoMovimientos;
import java.util.ArrayList;

/**
 *
 * @author migue
 */
public class MovimientoService {

    private final RepoMovimientos repo;

    public MovimientoService() {
        this.repo = new RepoMovimientos();
    }

    public RepoMovimientos getRepo() {
        return repo;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return repo.getMovimientos();
    }

    public void agregarMovimiento(Movimiento movimiento) {
        if (movimiento.getFecha() == null) {
            movimiento.setFecha(java.time.LocalDateTime.now());
        }
        repo.agregarMovimiento(movimiento);
    }

    public Producto obtenerProductoPorId(Long id) throws Exception {
        ProductoService productoService = new ProductoService();
        return productoService.buscarProducto("ID", id.toString());
    }

}
