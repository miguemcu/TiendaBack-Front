/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.tiendalasebastianaweb;

import BusinessLogic.Producto;
import BusinessLogic.ProductoService;
import java.io.BufferedReader;
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
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@WebServlet(name = "SvProductos", urlPatterns = {"/SvProductos"})
public class SvProductos extends HttpServlet {

    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        JsonSerializer<LocalDate> localDateSerializer = (src, typeOfSrc, context) -> {
            if (src == null) return null;
            return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
        };
        JsonDeserializer<LocalDate> localDateDeserializer = (json, typeOfT, context) -> {
            if (json == null || json.isJsonNull() || json.getAsString().isEmpty()) return null;
            try {
                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                try {
                    return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException ex) {
                    System.err.println("DEBUG SvProductos - init: Error al parsear fecha '" + json.getAsString() + "': " + ex.getMessage());
                    throw new com.google.gson.JsonParseException("Error al parsear fecha: " + ex.getMessage(), ex);
                }
            }
        };
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateSerializer)
                .registerTypeAdapter(LocalDate.class, localDateDeserializer)
                .setPrettyPrinting()
                .create();
    }

    private static class ErrorResponse {
        String error;
        public ErrorResponse(String error) { this.error = error; }
    }

    private static class SuccessResponse {
        String mensaje;
        public SuccessResponse(String mensaje) { this.mensaje = mensaje; }
    }

    private static class ProductoBusquedaResponse {
        Producto producto;
        int cantidad;
        public ProductoBusquedaResponse(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }
        public Producto getProducto() { return producto; }
        public int getCantidad() { return cantidad; }
    }

    private static class RequestData {
        String accion;
        String valor;
        String busqueda;
        String id;
        String nuevaCantidad;
    }

    private boolean isValidLong(String str) {
        if (str == null || str.isEmpty()) return false;
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
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            System.out.println("DEBUG SvProductos - doGet: Se recibió una petición GET. No permitido para operaciones principales.");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            out.print(gson.toJson(new ErrorResponse("Método GET no permitido para esta operación. Se espera un POST.")));
        } catch (Exception ex) {
            System.err.println("DEBUG SvProductos - doGet: Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (out != null) out.print(gson.toJson(new ErrorResponse("Error interno del servidor en GET: " + ex.getMessage())));
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        RequestData requestData = null;

        try {
            out = response.getWriter();
            System.out.println("DEBUG SvProductos - doPost: Recibiendo petición POST.");

            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println("DEBUG SvProductos - doPost: Cuerpo de la petición JSON: " + requestBody);

            if (requestBody == null || requestBody.isEmpty()) {
                System.out.println("DEBUG SvProductos - doPost: Cuerpo de la petición JSON vacío.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Cuerpo de la petición vacío. Se espera un JSON.")));
                return;
            }

            requestData = gson.fromJson(requestBody, RequestData.class);

            if (requestData == null || requestData.accion == null || requestData.accion.isEmpty()) {
                System.out.println("DEBUG SvProductos - doPost: Accion es nula o vacía en el JSON.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Acción no especificada en el JSON.")));
                return;
            }

            String accion = requestData.accion;
            System.out.println("DEBUG SvProductos - doPost: Accion recibida del JSON: '" + accion + "'");

            ProductoService service = new ProductoService();

            switch (accion) {
                case "buscar": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'buscar'.");
                    handleBuscar(requestData, response, out, service);
                    break;
                }
                case "ajustar": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'ajustar'.");
                    handleAjustar(requestData, response, out, service);
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

        } catch (com.google.gson.JsonSyntaxException jsonEx) {
            System.err.println("DEBUG SvProductos - doPost: Error de sintaxis JSON: " + jsonEx.getMessage());
            jsonEx.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (out != null) out.print(gson.toJson(new ErrorResponse("Formato JSON inválido en la petición.")));
        } catch (Exception ex) {
            System.err.println("DEBUG SvProductos - doPost: Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (out != null) out.print(gson.toJson(new ErrorResponse("Error interno del servidor: " + ex.getMessage())));
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    private void handleBuscar(RequestData requestData, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        String valorBusqueda = requestData.valor;
        String tipoBusqueda = requestData.busqueda;

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

    private void handleAjustar(RequestData requestData, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        String idStr = requestData.id;
        String nuevaCantidadStr = requestData.nuevaCantidad;

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
