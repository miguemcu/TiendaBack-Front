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
import com.google.gson.JsonParser; // Necesario para parsear el JSON de entrada
import com.google.gson.JsonObject; // Necesario para trabajar con objetos JSON
import com.google.gson.JsonArray; // Necesario para trabajar con arrays JSON
import com.google.gson.JsonElement; // Para iterar sobre elementos JSON
import com.google.gson.JsonSyntaxException; // Para manejar errores de sintaxis JSON
import jakarta.servlet.RequestDispatcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList; // Necesario para las etiquetas
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(name = "SvProductos", urlPatterns = {"/SvProductos"})
public class SvProductos extends HttpServlet {

    private Gson gson;
    private ProductoService service = new ProductoService(); // Instancia del servicio de productos

    @Override
    public void init() throws ServletException {
        super.init();
        // Serializador para LocalDate (de Java a JSON)
        JsonSerializer<LocalDate> localDateSerializer = (src, typeOfSrc, context) -> {
            if (src == null) {
                return null;
            }
            return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
        };
        // Deserializador para LocalDate (de JSON a Java)
        JsonDeserializer<LocalDate> localDateDeserializer = (json, typeOfT, context) -> {
            if (json == null || json.isJsonNull() || json.getAsString().isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                try {
                    // Intentar con otro formato si el ISO_LOCAL_DATE falla
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

    // Clases internas para la estructura de respuesta JSON
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

        public Producto getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }
    }

    // Ya no necesitamos la clase RequestData genérica, se parseará directamente a JsonObject
    // Método auxiliar para validar si un String es un número Long válido
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
        Map<Producto, Integer> productos = new HashMap<>();
        ProductoService productoServicio = new ProductoService();
        try {

            productos = productoServicio.getProductos();
            request.setAttribute("productos", productos);
            RequestDispatcher dispatcher = request.getRequestDispatcher("verInventario.jsp");
            dispatcher.forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(SvProductos.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(
                "DEBUG SvProductos - doGet: Se recibió una petición GET.");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

        response.setContentType(
                "application/json;charset=UTF-8");
        response.getWriter()
                .print(gson.toJson(new ErrorResponse("Método GET no permitido para esta operación. Se espera un POST.")));
        response.setContentType(
                "application/json;charset=UTF-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            System.out.println("DEBUG SvProductos - doGet: Se recibió una petición GET. No permitido para operaciones principales.");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405 Method Not Allowed
            out.print(gson.toJson(new ErrorResponse("Método GET no permitido para esta operación. Se espera un POST.")));
        } catch (Exception ex) {
            System.err.println("DEBUG SvProductos - doGet: Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (out != null) {
                out.print(gson.toJson(new ErrorResponse("Error interno del servidor en GET: " + ex.getMessage())));
            }
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
        JsonObject jsonRequest = null; // Se usará JsonObject directamente

        try {
            out = response.getWriter();
            System.out.println("DEBUG SvProductos - doPost: Recibiendo petición POST.");

            // Leer el cuerpo de la petición JSON
            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println("DEBUG SvProductos - doPost: Cuerpo de la petición JSON: " + requestBody);

            if (requestBody == null || requestBody.isEmpty()) {
                System.out.println("DEBUG SvProductos - doPost: Cuerpo de la petición JSON vacío.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Cuerpo de la petición vacío. Se espera un JSON.")));
                return;
            }

            // Parsear el JSON directamente a JsonObject
            jsonRequest = JsonParser.parseString(requestBody).getAsJsonObject();

            String accion = null;
            if (jsonRequest.has("accion") && !jsonRequest.get("accion").isJsonNull()) {
                accion = jsonRequest.get("accion").getAsString();
            }

            if (accion == null || accion.isEmpty()) {
                System.out.println("DEBUG SvProductos - doPost: Accion es nula o vacía en el JSON.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Acción no especificada en el JSON.")));
                return;
            }

            System.out.println("DEBUG SvProductos - doPost: Accion recibida del JSON: '" + accion + "'");

            switch (accion) {
                case "buscar": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'buscar'.");
                    handleBuscar(jsonRequest, response, out, service);
                    break;
                }
                case "ajustar": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'ajustar'.");
                    handleAjustar(jsonRequest, response, out, service);
                    break;
                }
                case "crear": {
                    System.out.println("DEBUG SvProductos - doPost: Ejecutando accion 'crear'.");
                    handleCrearProducto(jsonRequest, response, out, service); // Llamada al nuevo método
                    break;
                }
                default:
                    System.out.println("DEBUG SvProductos - doPost: Accion no reconocida: '" + accion + "'");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(gson.toJson(new ErrorResponse("Acción no reconocida: " + accion)));
                    break;
            }

        } catch (JsonSyntaxException jsonEx) {
            System.err.println("DEBUG SvProductos - doPost: Error de sintaxis JSON: " + jsonEx.getMessage());
            jsonEx.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (out != null) {
                out.print(gson.toJson(new ErrorResponse("Formato JSON inválido en la petición.")));
            }
        } catch (Exception ex) {
            System.err.println("DEBUG SvProductos - doPost: Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (out != null) {
                out.print(gson.toJson(new ErrorResponse("Error interno del servidor: " + ex.getMessage())));
            }
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    // Nuevo método para manejar la creación de productos
    private void handleCrearProducto(JsonObject jsonRequest, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        try {
            // Extraer datos del JsonObject de forma segura
            String tipoProducto = jsonRequest.has("tipoProducto") ? jsonRequest.get("tipoProducto").getAsString() : null;
            String nombre = jsonRequest.has("nombre") ? jsonRequest.get("nombre").getAsString() : null;
            long idProducto = jsonRequest.has("id") ? jsonRequest.get("id").getAsLong() : -1; // Usar un valor por defecto si no existe
            double precioMayorista = jsonRequest.has("precioMayorista") ? jsonRequest.get("precioMayorista").getAsDouble() : -1.0;
            double precioMinorista = jsonRequest.has("precioMinorista") ? jsonRequest.get("precioMinorista").getAsDouble() : -1.0;

            LocalDate fechaVencimiento = null;
            if (jsonRequest.has("fechaVencimiento") && !jsonRequest.get("fechaVencimiento").isJsonNull() && !jsonRequest.get("fechaVencimiento").getAsString().isEmpty()) {
                fechaVencimiento = LocalDate.parse(jsonRequest.get("fechaVencimiento").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
            }

            ArrayList<String> etiquetas = new ArrayList<>();
            if (jsonRequest.has("etiquetas") && jsonRequest.get("etiquetas").isJsonArray()) {
                JsonArray jsonEtiquetas = jsonRequest.get("etiquetas").getAsJsonArray();
                for (JsonElement element : jsonEtiquetas) {
                    etiquetas.add(element.getAsString());
                }
            } else if (jsonRequest.has("etiquetas") && jsonRequest.get("etiquetas").isJsonPrimitive()) {
                // Manejar el caso donde etiquetas es una cadena simple (ej. si el JS lo envía así)
                String etiquetasStr = jsonRequest.get("etiquetas").getAsString();
                if (!etiquetasStr.trim().isEmpty()) {
                    for (String tag : etiquetasStr.split(",")) {
                        etiquetas.add(tag.trim());
                    }
                }
            }
            int cantidad = jsonRequest.has("cantidad") ? jsonRequest.get("cantidad").getAsInt() : -1;

            // Validaciones de campos requeridos y valores válidos
            if (tipoProducto == null || tipoProducto.trim().isEmpty()
                    || nombre == null || nombre.trim().isEmpty()
                    || idProducto <= 0
                    || // ID debe ser positivo
                    precioMayorista <= 0 || precioMinorista <= 0
                    || // Precios deben ser positivos
                    cantidad < 0) { // Cantidad puede ser 0 o más

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ErrorResponse("Datos de producto incompletos o inválidos. Asegúrese de que ID, nombre, tipo, precios y cantidad sean válidos y estén presentes.")));
                return;
            }

            // --- VALIDACIÓN DE DUPLICADOS ---
            Producto productoExistentePorId = service.buscarProducto("ID", String.valueOf(idProducto));
            if (productoExistentePorId != null) {
                System.out.println("DEBUG SvProductos - handleCrearProducto: Ya existe un producto con el ID: " + idProducto);
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
                out.print(gson.toJson(new ErrorResponse("Ya existe un producto con el ID proporcionado.")));
                return;
            }

            Producto productoExistentePorNombre = service.buscarProducto("NOMBRE", nombre);
            if (productoExistentePorNombre != null) {
                System.out.println("DEBUG SvProductos - handleCrearProducto: Ya existe un producto con el nombre: " + nombre);
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
                out.print(gson.toJson(new ErrorResponse("Ya existe un producto con el nombre proporcionado.")));
                return;
            } // --- FIN VALIDACIÓN DE DUPLICADOS ---
// Llama al servicio para agregar el producto
            boolean creado = service.añadirProducto(
                    tipoProducto,
                    nombre,
                    idProducto,
                    precioMayorista,
                    precioMinorista,
                    fechaVencimiento,
                    etiquetas,
                    cantidad
            );

            if (creado) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(new SuccessResponse("Producto creado exitosamente.")));
            } else {
                // Este else se ejecutaría si añadirProducto falla por alguna otra razón interna
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // O 400 si es un error de negocio específico
                out.print(gson.toJson(new ErrorResponse("No se pudo crear el producto por una razón desconocida.")));
            }
        } catch (NumberFormatException e) {
            System.err.println("DEBUG SvProductos - handleCrearProducto: Error al parsear números: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ErrorResponse("Formato numérico inválido para ID, precios o cantidad.")));
        } catch (DateTimeParseException e) {
            System.err.println("DEBUG SvProductos - handleCrearProducto: Error al parsear fecha: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ErrorResponse("Formato de fecha inválido. Use YYYY-MM-DD.")));
        } catch (Exception e) {
            System.err.println("DEBUG SvProductos - handleCrearProducto: Error interno al crear producto: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ErrorResponse("Error interno del servidor al crear el producto: " + e.getMessage())));
        }
    }

    private void handleBuscar(JsonObject jsonRequest, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        String valorBusqueda = null;
        String tipoBusqueda = null;

        if (jsonRequest.has("valor") && !jsonRequest.get("valor").isJsonNull()) {
            valorBusqueda = jsonRequest.get("valor").getAsString();
        }
        if (jsonRequest.has("busqueda") && !jsonRequest.get("busqueda").isJsonNull()) {
            tipoBusqueda = jsonRequest.get("busqueda").getAsString();
        }

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
            // CORRECCIÓN: Obtener la cantidad del servicio, ya que Producto no tiene getCantidad()
            cantidadEnInventario = service.getCantidadProducto(producto.getId());
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(new ProductoBusquedaResponse(producto, cantidadEnInventario)));
        } else {
            System.out.println("DEBUG SvProductos - handleBuscar: Producto no encontrado para valor: '" + valorBusqueda + "'");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print(gson.toJson(new ErrorResponse("Producto no encontrado.")));
        }
    }

    private void handleAjustar(JsonObject jsonRequest, HttpServletResponse response, PrintWriter out, ProductoService service) throws Exception {
        String idStr = null;
        String nuevaCantidadStr = null;

        if (jsonRequest.has("id") && !jsonRequest.get("id").isJsonNull()) {
            idStr = jsonRequest.get("id").getAsString();
        }
        if (jsonRequest.has("nuevaCantidad") && !jsonRequest.get("nuevaCantidad").isJsonNull()) {
            nuevaCantidadStr = jsonRequest.get("nuevaCantidad").getAsString();
        }

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
