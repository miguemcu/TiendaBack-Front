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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "SvProductos", urlPatterns = {"/SvProductos"})
public class SvProductos extends HttpServlet {

    // Modificamos la inicialización de Gson para incluir un TypeAdapter para LocalDate
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        // Creador de un Serializador para LocalDate
        JsonSerializer<LocalDate> localDateSerializer = (src, typeOfSrc, context) ->
                src == null ? null : context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE));

        // Creador de un Deserializador para LocalDate
        JsonDeserializer<LocalDate> localDateDeserializer = (json, typeOfT, context) ->
                json == null ? null : LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);

        // Construir Gson con los TypeAdapters
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateSerializer)
                .registerTypeAdapter(LocalDate.class, localDateDeserializer)
                .setPrettyPrinting() // Opcional: para que el JSON se vea más legible
                .create();
    }

    private static class ErrorResponse {
        String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }

    private static class SuccessResponse {
        String mensaje;
        public SuccessResponse(String mensaje) {
            this.mensaje = mensaje;
        }
    }

    private static class ProductoBusquedaResponse {
        Producto producto;
        int cantidad;

        public ProductoBusquedaResponse(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }
    }

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("DEBUG SvProductos - doGet: Se recibió una petición GET.");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(gson.toJson(new ErrorResponse("Método GET no permitido para esta operación. Se espera un POST.")));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // --- INICIO DE DEPURACIÓN DE PARÁMETROS ---
        System.out.println("DEBUG SvProductos - doPost: Recibiendo petición POST.");
        java.util.Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            for (String paramValue : paramValues) {
                System.out.println("DEBUG SvProductos - Parámetro: " + paramName + " = " + paramValue);
            }
        }
        // --- FIN DE DEPURACIÓN DE PARÁMETROS ---

        String accion = request.getParameter("accion");
        System.out.println("DEBUG SvProductos - doPost: Accion recibida: '" + accion + "'");

        try {
            if (accion == null || accion.isEmpty()) {
                System.out.println("DEBUG SvProductos - doPost: Accion es nula o vacía.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Acción no especificada.")));
                return;
            }

            ProductoService service = new ProductoService();

            switch (accion) {
                case "buscar": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'buscar'.");
                    handleBuscar(request, response, out, service);
                    break;
                }
                case "ajustar": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'ajustar'.");
                    handleAjustar(request, response, out, service);
                    break;
                }
                case "crear": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'crear'. (Funcionalidad no implementada en backend)");
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(gson.toJson(new SuccessResponse("Funcionalidad de crear producto no implementada aún.")));
                    break;
                }
                default:
                    System.out.println("DEBUG SvProductos - doPost: Accion no reconocida: '" + accion + "'");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(gson.toJson(new ErrorResponse("Acción no reconocida: " + accion)));
                    break;
            }

        } catch (Exception ex) {
            System.err.println("DEBUG SvProductos - doPost: Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse("Error interno del servidor: " + ex.getMessage())));
        } finally {
            out.flush();
            out.close();
        }
    }

    private void handleBuscar(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        String valorBusqueda = request.getParameter("valor");
        String tipoBusqueda = request.getParameter("busqueda");

        System.out.println("DEBUG SvProductos - handleBuscar: Valor: '" + valorBusqueda + "', Tipo: '" + tipoBusqueda + "'");

        if (valorBusqueda == null || valorBusqueda.trim().isEmpty()) {
            System.out.println("DEBUG SvProductos - handleBuscar: Valor de búsqueda nulo o vacío.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ErrorResponse("El valor de búsqueda no puede estar vacío.")));
            return;
        }
        if (tipoBusqueda == null || (!tipoBusqueda.equalsIgnoreCase("ID") && !tipoBusqueda.equalsIgnoreCase("NOMBRE"))) {
            System.out.println("DEBUG SvProductos - handleBuscar: Tipo de búsqueda inválido: '" + tipoBusqueda + "'");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ErrorResponse("Tipo de búsqueda inválido. Debe ser 'ID' o 'NOMBRE'.")));
            return;
        }

        Producto producto = null;
        int cantidadEnInventario = 0;

        producto = service.buscarProducto(tipoBusqueda, valorBusqueda);
        System.out.println("DEBUG SvProductos - handleBuscar: Usando service.buscarProducto(tipo: " + tipoBusqueda + ", valor: " + valorBusqueda + ")");

        if (producto != null) {
            System.out.println("DEBUG SvProductos - handleBuscar: Producto encontrado: " + producto.getNombre() + " (ID: " + producto.getId() + ")");
            cantidadEnInventario = service.getCantidadProducto(producto.getId());
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(new ProductoBusquedaResponse(producto, cantidadEnInventario)));
        } else {
            System.out.println("DEBUG SvProductos - handleBuscar: Producto no encontrado para valor: '" + valorBusqueda + "'");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print(gson.toJson(new ErrorResponse("Producto no encontrado.")));
        }
    }

    private void handleAjustar(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        String idStr = request.getParameter("id");
        String nuevaCantidadStr = request.getParameter("nuevaCantidad");

        System.out.println("DEBUG SvProductos - handleAjustar: ID: '" + idStr + "', Nueva Cantidad: '" + nuevaCantidadStr + "'");

        if (idStr == null || idStr.isEmpty() || nuevaCantidadStr == null || nuevaCantidadStr.isEmpty()) {
            System.out.println("DEBUG SvProductos - handleAjustar: ID o nueva cantidad son nulos/vacíos.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ErrorResponse("ID y nueva cantidad requeridos.")));
            return;
        }

        try {
            long idProducto = Long.parseLong(idStr);
            int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);

            System.out.println("DEBUG SvProductos - handleAjustar: ID parseado: " + idProducto + ", Cantidad parseada: " + nuevaCantidad);

            Producto productoAjustar = service.buscarProducto("ID", String.valueOf(idProducto));

            if (productoAjustar == null) {
                System.out.println("DEBUG SvProductos - handleAjustar: Producto con ID " + idProducto + " no encontrado para ajustar.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(new ErrorResponse("Producto no encontrado para ajustar.")));
                return;
            }

            boolean ok = service.editarCantidadProducto(productoAjustar, nuevaCantidad);

            if (ok) {
                System.out.println("DEBUG SvProductos - handleAjustar: Cantidad ajustada con éxito para ID: " + idProducto);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(new SuccessResponse("Cantidad ajustada con éxito.")));
            } else {
                System.out.println("DEBUG SvProductos - handleAjustar: No se pudo ajustar la cantidad o producto no encontrado (servicio).");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(new ErrorResponse("No se pudo ajustar la cantidad o producto no encontrado.")));
            }
        } catch (NumberFormatException e) {
            System.err.println("DEBUG SvProductos - handleAjustar: Error al parsear número: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (!isValidLong(idStr)) {
                out.print(gson.toJson(new ErrorResponse("El ID del producto debe ser un número válido.")));
            } else {
                out.print(gson.toJson(new ErrorResponse("La nueva cantidad debe ser un número válido.")));
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para manejar productos.";
    }
}