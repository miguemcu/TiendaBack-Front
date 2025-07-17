<%-- 
    Document   : verInventario
    Created on : 14/07/2025, 10:25:29 p. m.
    Author     : DELL
--%>

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="BusinessLogic.Producto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Tienda La Sebastiana - Ver Inventario</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
    </head>
    <body>
        <div class="container text-center mt-5">
            <div class="row">
                <div class="col">

                </div>
                <div class="col-12">
                    <% Map<Producto, Integer> productos = (Map<Producto, Integer>) request.getAttribute("productos");%>
                    <table id="TablaProductos" class="table table-striped-columns table-bordered">
                        <thead>
                            <tr class="table-primary">
                                <th scope="col" style="color: #1e3a8a">ID</th>
                                <th scope="col" style="color: #1e3a8a">Nombre</th>
                                <th scope="col" style="color: #1e3a8a">Tipo</th>
                                <th scope="col" style="color: #1e3a8a">Precio</th>
                                <th scope="col" style="color: #1e3a8a">Cantidad</th>           
                                <th scope="col" style="color: #1e3a8a">Acciones</th>
                            </tr>
                        </thead>
                        <tbody class="table-group-divider ">

                            <%if (productos != null) {

                                    for (Map.Entry<Producto, Integer> entry : productos.entrySet()) {
                                        Producto producto = entry.getKey();
                                        Integer cantidad = entry.getValue();
                                        String estilo = "";
                                        if (cantidad < 5) {
                                            estilo = "background-color: #f8d7da!important";
                                        }
                            %>
                            <tr>
                                <td style="<%= estilo%>">
                                    <%= producto.getId()%>
                                </td>
                                <td style="<%= estilo%>">
                                    <%= producto.getNombre()%>
                                </td>
                                <td style="<%= estilo%>">
                                    <%= producto.getTipoProducto()%>
                                </td>
                                <td style="<%= estilo%>">
                                    <%= producto.getPrecio()%>
                                </td>
                                <td style="<%= estilo%>">
                                    <%=cantidad%>
                                </td>
                                <td style="<%= estilo%>">
                                    <form method="GET" action="inventario.jsp">
                                        <input type="hidden" name="idProducto" value="<%= producto.getId()%>"/>
                                        <button type="submit" class="btn btn-info btn-sm">Ver Detalles</button>
                                    </form>
                                </td>
                            </tr>                
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="5" class="text-center">No hay productos disponibles.</td>
                            </tr>
                            <% }%>
                        </tbody>

                    </table>
                    <script>
                        $(document).ready(function () {
                            $('#TablaProductos').DataTable({
                                language: {
                                    url: '//cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
                                }
                            });
                        });
                    </script>
                </div>
                <div class="col">

                </div>
            </div>
    </body>
</html>
