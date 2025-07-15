<%-- 
    Document   : inventario
    Created on : 14/07/2025, 5:45:22 p. m.
    Author     : Sebastian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestión de Inventario - Tienda La Sebastiana</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 50px;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 800px;
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .form-group label {
            font-weight: bold;
        }
        .btn-group {
            margin-top: 20px;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2 class="mb-4 text-center">Gestión de Inventario</h2>

        <div id="mensaje" class="alert d-none" role="alert"></div>

        <form id="productoForm">
            <div class="form-group">
                <label for="buscarValor">Buscar por Nombre o ID:</label>
                <div class="input-group">
                    <input type="text" class="form-control" id="buscarValor" placeholder="Ingrese ID o Nombre del producto">
                    <div class="input-group-append">
                        <select class="form-control" id="tipoBusqueda">
                            <option value="ID">ID</option>
                            <option value="NOMBRE">Nombre</option>
                        </select>
                        <button type="button" class="btn btn-primary" id="btnBuscar">Buscar</button>
                    </div>
                </div>
            </div>

            <hr>

            <h4 class="mb-3">Detalles del Producto</h4>

            <div class="form-group">
                <label for="id">ID del Producto:</label>
                <input type="text" class="form-control" id="id" disabled>
            </div>
            <div class="form-group">
                <label for="nombre">Nombre del Producto:</label>
                <input type="text" class="form-control" id="nombre" disabled>
            </div>
            <div class="form-group">
                <label for="tipo">Tipo:</label>
                <input type="text" class="form-control" id="tipo" disabled>
            </div>
            <div class="form-group">
                <label for="precioMayor">Precio Mayorista:</label>
                <input type="text" class="form-control" id="precioMayor" disabled>
            </div>
            <div class="form-group">
                <label for="precioMenor">Precio Minorista:</label>
                <input type="text" class="form-control" id="precioMenor" disabled>
            </div>
            <div class="form-group">
                <label for="ficha">Ficha Técnica / Etiquetas:</label>
                <textarea class="form-control" id="ficha" rows="3" disabled></textarea>
            </div>
            <div class="form-group">
                <label for="cantidad">Cantidad en Inventario:</label>
                <input type="number" class="form-control" id="cantidad" min="0">
            </div>

            <div class="btn-group d-flex justify-content-between">
                <button type="button" class="btn btn-success" id="btnAjustar">Ajustar Cantidad</button>
                <button type="button" class="btn btn-info" id="btnLimpiar">Limpiar Campos</button>
                <button type="button" class="btn btn-secondary" id="btnNuevo">Crear Nuevo Producto</button>
            </div>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <script>
        // Función para mostrar mensajes
        function showMessage(message, type = 'info') {
            const mensajeDiv = document.getElementById("mensaje");
            mensajeDiv.textContent = message;
            mensajeDiv.className = `alert alert-${type}`;
            mensajeDiv.classList.remove('d-none');
            setTimeout(() => {
                mensajeDiv.classList.add('d-none');
            }, 5000);
        }

        // Función para limpiar todos los campos del formulario
        function clearFields() {
            document.getElementById("id").value = "";
            document.getElementById("nombre").value = "";
            document.getElementById("tipo").value = "";
            document.getElementById("cantidad").value = "";
            document.getElementById("precioMayor").value = "";
            document.getElementById("precioMenor").value = "";
            document.getElementById("ficha").value = "";
            document.getElementById("buscarValor").value = "";
        }

        // Event Listener para el botón Buscar
        document.getElementById("btnBuscar").addEventListener("click", function() {
            const buscarValor = document.getElementById("buscarValor").value;
            const tipoBusqueda = document.getElementById("tipoBusqueda").value;

            if (buscarValor.trim() === "") {
                showMessage("Por favor, ingrese un valor para buscar.", "warning");
                return;
            }

            const formData = new FormData(); // Esta línea es la que falla si no está definida
            formData.append("accion", "buscar");
            formData.append("valor", buscarValor);
            formData.append("busqueda", tipoBusqueda);

            fetch('SvProductos', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(errorData => {
                        throw new Error(errorData.error || `Error HTTP: ${response.status}`);
                    }).catch(() => {
                        throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    showMessage("Error al buscar producto: " + data.error, "danger");
                    clearFields();
                } else {
                    const p = data.producto;
                    document.getElementById("id").value = p.id;
                    document.getElementById("nombre").value = p.nombre;
                    document.getElementById("tipo").value = p.tipoProducto;
                    document.getElementById("cantidad").value = data.cantidad;
                    document.getElementById("precioMayor").value = p.precioMayorista;
                    document.getElementById("precioMenor").value = p.precio;
                    document.getElementById("ficha").value = p.etiquetas;
                    showMessage("Producto encontrado.", "success");
                }
            })
            .catch(error => {
                console.error('Error en la petición fetch:', error);
                showMessage("Ha ocurrido un error en la comunicación con el servidor: " + error.message, "danger");
                clearFields();
            });
        });

        // Event Listener para el botón Ajustar Cantidad
        document.getElementById("btnAjustar").addEventListener("click", function() {
            const idProducto = document.getElementById("id").value;
            const nuevaCantidad = document.getElementById("cantidad").value;

            if (idProducto.trim() === "" || nuevaCantidad.trim() === "") {
                showMessage("Primero busque un producto y asegúrese que la cantidad no esté vacía.", "warning");
                return;
            }
            if (isNaN(nuevaCantidad) || parseInt(nuevaCantidad) < 0) {
                showMessage("La cantidad debe ser un número entero no negativo.", "warning");
                return;
            }

            const formData = new FormData();
            formData.append("accion", "ajustar");
            formData.append("id", idProducto);
            formData.append("nuevaCantidad", nuevaCantidad);

            fetch('SvProductos', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(errorData => {
                        throw new Error(errorData.error || `Error HTTP: ${response.status}`);
                    }).catch(() => {
                        throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    showMessage("Error al ajustar cantidad: " + data.error, "danger");
                } else {
                    showMessage(data.mensaje || "Cantidad ajustada con éxito.", "success");
                }
            })
            .catch(error => {
                console.error('Error en la petición fetch:', error);
                showMessage("Ha ocurrido un error al ajustar la cantidad: " + error.message, "danger");
            });
        });

        // Event Listener para el botón Limpiar Campos
        document.getElementById("btnLimpiar").addEventListener("click", clearFields);

        // Event Listener para el botón Crear Nuevo Producto (sin implementar backend aún)
        document.getElementById("btnNuevo").addEventListener("click", function() {
            clearFields();
            showMessage("Función para crear nuevo producto no implementada aún en el backend.", "info");
        });

        // Inicializar: limpiar campos al cargar la página
        document.addEventListener("DOMContentLoaded", clearFields);

    </script>
    </body>
</html>