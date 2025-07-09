/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusinessLogic;

import Repository.RepoEmpleados;
import java.util.ArrayList;

/**
 *
 * @author migue
 */
public class EmpleadoService {
    private RepoEmpleados repositorio;
    private Empleado cajero;

    public EmpleadoService() throws Exception {
        this.repositorio = new RepoEmpleados();
    }

    public RepoEmpleados getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(RepoEmpleados repositorio) {
        this.repositorio = repositorio;
    }

    public Empleado getCajero() {
        return cajero;
    }

    public void setCajero(Empleado cajero) {
        this.cajero = cajero;
    }
    
    public ArrayList<Empleado> getEmpleados() throws Exception {
        return repositorio.getEmpleados();
    }
    
    public Empleado validarDocumento(String nombre, String documento) throws Exception {
        var empleado = repositorio.getEmpleado(nombre);
        
        if (empleado != null && empleado.getDocumento().equals(documento)){
            return empleado;
        }
        
        return null;
    }
    
    public boolean agregarEmpleado(String nombre, String documento) throws Exception{
        return repositorio.agregarEmpleado(nombre, documento);
    }
    
    public boolean eliminarEmpleado(String nombre, String documento) throws Exception{
        return repositorio.eliminarEmpleado(nombre, documento);
    }
}
