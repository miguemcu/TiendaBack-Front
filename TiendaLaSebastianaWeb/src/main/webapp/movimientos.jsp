<%-- 
    Document   : movimientos
    Created on : Jul 16, 2025, 8:33:23 PM
    Author     : migue
--%>

<%@page import="BusinessLogic.ProductoService"%>
<%@page import="BusinessLogic.Producto"%>
<%@page import="java.util.List"%>
<%@page import="BusinessLogic.Movimiento"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de Movimientos - Tienda La Sebastiana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.2/css/dataTables.dataTables.css" />
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://cdn.datatables.net/2.3.2/js/dataTables.js"></script>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-12">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white text-center">
                        <h3>Registro de Movimientos</h3>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="tablaMovimientos" class="table table-striped table-hover align-middle">
                                <thead class="table-dark">
                                    <tr>
                                        <th>#</th>
                                        <th>Tipo de Movimiento</th>
                                        <th>ID Producto</th>
                                        <th>Nombre Producto</th>
                                        <th>Cantidad</th>
                                        <th>Comentario</th>
                                        <th>Fecha</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        List<Map<String, Object>> movimientosConProducto = (List<Map<String, Object>>) request.getAttribute("movimientosConProducto");
                                        if (movimientosConProducto != null && !movimientosConProducto.isEmpty()) {
                                            int i = 1;
                                            for (Map<String, Object> map : movimientosConProducto) {
                                                Movimiento mov = (Movimiento) map.get("movimiento");
                                                Producto producto = (Producto) map.get("producto");
                                                int cantidad = (int) map.get("cantidad");
                                    %>
                                    <tr>
                                        <th scope="row"><%= i++ %></th>
                                        <td><%= mov.getTipo() != null ? mov.getTipo().name() : "" %></td>
                                        <td><%= mov.getId() != null ? mov.getId() : "" %></td>
                                        <td><%= (producto != null) ? producto.getNombre() : "No encontrado" %></td>
                                        <td><%= cantidad %></td>
                                        <td><%= mov.getComentario() != null ? mov.getComentario() : "" %></td>
                                        <td><%= mov.getFecha() != null ? mov.getFecha().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "" %></td>
                                    </tr>
                                    <%      }
                                            }
                                    %>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <th>#</th>
                                        <th>Tipo de Movimiento</th>
                                        <th>ID Producto</th>
                                        <th>Nombre Producto</th>
                                        <th>Cantidad</th>
                                        <th>Comentario</th>
                                        <th>Fcha</th>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                        <div class="text-end mt-3">
                            <a href="tienda.jsp" class="btn btn-secondary">Volver a la tienda</a>
                        </div>
                    </div>
                </div>
                <p class="text-center mt-3 text-muted">Nairo Quintana © 2025</p>
            </div>
        </div>
    </div>
    <script>
        new DataTable('#tablaMovimientos');
    </script>
</body>
</html>
