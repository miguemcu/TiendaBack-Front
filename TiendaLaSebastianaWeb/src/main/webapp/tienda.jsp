<%-- 
    Document   : tienda
    Created on : Jul 12, 2025, 8:44:12 PM
    Author     : migue
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tienda La Sebastiana - Panel Principal</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <h2 class="text-center mb-4">Bienvenido a Tienda La Sebastiana</h2>

        <div class="row g-4 justify-content-center">
            <!-- Columna izquierda -->
            <div class="col-md-5">
                <div class="d-grid gap-4">
                    <a href="hacerVenta.jsp" class="btn btn-primary btn-lg">Hacer Venta</a>
                    <a href="inventario.jsp" class="btn btn-warning btn-lg">Inventario</a>
                </div>
            </div>

            <!-- Columna derecha -->
            <div class="col-md-5">
                <div class="d-grid gap-4">
                    <a href="devolucion.jsp" class="btn btn-danger btn-lg">Devolución</a>
                    <a href="reportes.jsp" class="btn btn-success btn-lg">Reportes</a>
                </div>
            </div>
        </div>

        <div class="text-center mt-5">
            <p class="text-muted">Nairo Quintana © 2025</p>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>

