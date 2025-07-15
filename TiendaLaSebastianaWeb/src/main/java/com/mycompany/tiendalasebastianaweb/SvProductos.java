/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.tiendalasebastianaweb;

import BusinessLogic.Producto;
import BusinessLogic.ProductoService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map; // Necesario si usas Map para la respuesta

@WebServlet(name = "SvProductos", urlPatterns = {"/SvProductos"})
public class SvProductos extends HttpServlet {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(gson.toJson(new ErrorResponse("Método GET no permitido para esta operación.")));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String accion = request.getParameter("accion");
        System.out.println("DEBUG SvProductos - Accion recibida: " + accion);

        try {
            if (accion == null || accion.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Acción no especificada.")));
                return;
            }

            ProductoService service = new ProductoService();

            switch (accion) {
    // Nuevo: Pasa 'response' como segundo argumento
case "buscar": {
    handleBuscar(request, response, out, service);
    break;
}
case "ajustar": {
    handleAjustar(request, response, out, service);
    break;
}
                case "crear": {
                    out.print(gson.toJson(new SuccessResponse("Funcionalidad de crear producto no implementada aún.")));
                    break;
                }
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(gson.toJson(new ErrorResponse("Acción no reconocida: " + accion)));
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse("Error interno del servidor: " + ex.getMessage())));
        } finally {
            out.flush();
        }
    }

   private void handleBuscar(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        String valor = request.getParameter("valor");
        String tipoBusqueda = request.getParameter("busqueda");
        System.out.println("DEBUG SvProductos - valor: " + valor + ", busqueda: " + tipoBusqueda);

        Producto prod = service.buscarProducto(valor, tipoBusqueda); // Asegúrate de que este método maneja long o String según tu BD

        if (prod != null) {
            // Asumo que ProductoService.getCantidadProducto(id) obtiene la cantidad del inventario
            // Si Producto ya tiene la cantidad, no necesitas esta llamada adicional.
            int cantidad = service.getCantidadProducto(prod.getId()); // prod.getId() debe devolver long si es long

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("producto", prod);
            responseData.put("cantidad", cantidad);

            out.print(gson.toJson(responseData));
        } else {
            out.print(gson.toJson(new ErrorResponse("Producto no encontrado.")));
        }
    }

    private void handleAjustar(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception{
    String idStr = request.getParameter("id");
    String nuevaCantidadStr = request.getParameter("nuevaCantidad");
    System.out.println("DEBUG SvProductos - ajustar producto: id=" + idStr + ", nuevaCantidad=" + nuevaCantidadStr);

    if (idStr == null || idStr.isEmpty() || nuevaCantidadStr == null || nuevaCantidadStr.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print(gson.toJson(new ErrorResponse("ID y nueva cantidad requeridos.")));
        return;
    }

    long idProducto; 
    int nuevaCantidad;

    try {
        idProducto = Long.parseLong(idStr); 
        nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
            Producto productoAjustar = service.buscarProducto(idStr, "ID"); // O service.buscarProductoPorId(idProducto) si existe

        if (productoAjustar == null) {
            out.print(gson.toJson(new ErrorResponse("Producto no encontrado para ajustar.")));
            return;
        }

        // Ahora que tienes el objeto Producto, pásalo al método
        boolean ok = service.editarCantidadProducto(productoAjustar, nuevaCantidad); // ¡Aquí se pasa el objeto Producto!

        if (ok) {
            out.print(gson.toJson(new SuccessResponse("Cantidad ajustada con éxito.")));
        } else {
            out.print(gson.toJson(new ErrorResponse("No se pudo ajustar la cantidad."))); // Mensaje más genérico
        }
    } catch (NumberFormatException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        if (!isValidLong(idStr)) {
            out.print(gson.toJson(new ErrorResponse("El ID del producto debe ser un número válido.")));
        } else {
            out.print(gson.toJson(new ErrorResponse("La nueva cantidad debe ser un número válido.")));
        }
    }
}

    // Helper para verificar si un String es un long válido (útil en el catch)
    private boolean isValidLong(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Clases auxiliares para respuestas JSON.
     */
    private static class ErrorResponse {
        private String error;
        public ErrorResponse(String error) { this.error = error; }
    }

    private static class SuccessResponse {
        private String mensaje;
        public SuccessResponse(String mensaje) { this.mensaje = mensaje; }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para manejar productos.";
    }
}
