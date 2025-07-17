package com.mycompany.tiendalasebastianaweb;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import BusinessLogic.Movimiento;
import BusinessLogic.MovimientoService;
import BusinessLogic.Producto;
import BusinessLogic.ProductoService;
import BusinessLogic.EnumTipoMovimiento;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migue
 */
@WebServlet(urlPatterns = {"/SvMovimientos"})
public class SvMovimientos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SvMovimientos</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SvMovimientos at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MovimientoService movimientoService = new MovimientoService();
        List<Movimiento> movimientos = movimientoService.getMovimientos();

        List<Map<String, Object>> movimientosConProducto = new ArrayList<>();
        for (Movimiento mov : movimientos) {
            Map<String, Object> map = new HashMap<>();
            map.put("movimiento", mov);
            if (mov.getId() != null) {
                try {
                    Producto producto = movimientoService.obtenerProductoPorId(mov.getId());
                    map.put("producto", producto);

                    // Obtener cantidad usando ProductoService
                    int cantidad = new ProductoService().obtenerCantidadProductoPorId(mov.getId());
                    map.put("cantidad", cantidad);
                } catch (Exception e) {
                    map.put("producto", null);
                    map.put("cantidad", 0);
                }
            } else {
                map.put("producto", null);
                map.put("cantidad", 0);
            }
            movimientosConProducto.add(map);
        }

        // Guardar la lista como atributo en el request
        request.setAttribute("movimientosConProducto", movimientosConProducto);
        
        request.getRequestDispatcher("movimientos.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("application/json;charset=UTF-8");
    PrintWriter out = response.getWriter();

    try {
        Long idProducto = Long.parseLong(request.getParameter("idProducto"));
        int nuevaCantidad = Integer.parseInt(request.getParameter("nuevaCantidad"));
        int cantidadAnterior = Integer.parseInt(request.getParameter("cantidadAnterior"));
        String comentario = request.getParameter("comentario");

        System.out.println("DEBUG SvMovimientos: idProducto=" + idProducto + ", nuevaCantidad=" + nuevaCantidad + ", cantidadAnterior=" + cantidadAnterior);

        EnumTipoMovimiento tipoMovimiento;
        if (nuevaCantidad > cantidadAnterior) {
            tipoMovimiento = EnumTipoMovimiento.ADJUSTP;
        } else if (nuevaCantidad < cantidadAnterior) {
            tipoMovimiento = EnumTipoMovimiento.ADJUSTM;
        } else {
            tipoMovimiento = null;
        }

        if (tipoMovimiento != null) {
            Movimiento movimiento = new Movimiento();
            movimiento.setTipo(tipoMovimiento);
            movimiento.setId(idProducto);
            movimiento.setComentario(comentario != null ? comentario : "Ajuste de inventario");
            movimiento.setFecha(java.time.LocalDateTime.now());

            MovimientoService movimientoService = new MovimientoService();
            movimientoService.agregarMovimiento(movimiento);

        } else {
            
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        out.print("{\"success\":false,\"mensaje\":\"Error al registrar el movimiento.\"}");
    }
    out.flush();
}
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}