<%-- 
    Document   : inventario
    Created on : 14/07/2025, 5:45:22 p. m.
    Author     : Sebastian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Inventario</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css"> </head>
<body>
    <div class="container mt-4">
        <h1>Gestión de Inventario</h1>
        
        <div id="messageContainer" class="mt-3"></div>

        <div class="mb-3">
            <label for="buscarValor" class="form-label">Buscar por Nombre o ID:</label>
            <div class="input-group">
                <input type="text" class="form-control" id="buscarValor" placeholder="Ingrese ID o Nombre del producto">
                <select class="form-select" id="tipoBusqueda">
                    <option value="ID">ID</option>
                    <option value="NOMBRE">Nombre</option>
                </select>
                <button class="btn btn-primary" id="btnBuscar">Buscar</button>
            </div>
        </div>

        <h2>Detalles del Producto</h2>
        <div class="mb-3">
            <label for="idProductoResultado" class="form-label">ID del Producto:</label>
            <input type="text" class="form-control" id="idProductoResultado" readonly>
        </div>
        <div class="mb-3">
            <label for="nombreProductoResultado" class="form-label">Nombre del Producto:</label>
            <input type="text" class="form-control" id="nombreProductoResultado" readonly>
        </div>
        <div class="mb-3">
            <label for="tipoProductoResultado" class="form-label">Tipo:</label>
            <input type="text" class="form-control" id="tipoProductoResultado" readonly>
        </div>
        <div class="mb-3">
            <label for="precioMayoristaResultado" class="form-label">Precio Mayorista:</label>
            <input type="text" class="form-control" id="precioMayoristaResultado" readonly>
        </div>
        <div class="mb-3">
            <label for="precioMinoristaResultado" class="form-label">Precio Minorista:</label>
            <input type="text" class="form-control" id="precioMinoristaResultado" readonly>
        </div>
        <div class="mb-3">
            <label for="fechaVencimientoResultado" class="form-label">Fecha de Vencimiento:</label>
            <input type="text" class="form-control" id="fechaVencimientoResultado" readonly>
        </div>
        <div class="mb-3">
            <label for="etiquetasResultado" class="form-label">Etiquetas:</label>
            <input type="text" class="form-control" id="etiquetasResultado" readonly>
        </div>
        <div class="mb-3">
            <label for="cantidadInventarioResultado" class="form-label">Cantidad en Inventario:</label>
            <input type="text" class="form-control" id="cantidadInventarioResultado" readonly>
        </div>

        <h2 class="mt-4">Ajustar Cantidad</h2>
        <div class="mb-3">
            <label for="ajustarCantidadInput" class="form-label">Nueva Cantidad:</label>
            <div class="input-group">
                <input type="number" class="form-control" id="ajustarCantidadInput" placeholder="Ingrese la nueva cantidad para el producto buscado">
                <button class="btn btn-warning" id="btnAjustar">Ajustar Cantidad</button>
            </div>
        </div>

        <h2 class="mt-4">Crear Nuevo Producto</h2>
        <div class="mb-3">
            <button class="btn btn-success" id="btnCrearNuevo">Crear Nuevo Producto</button>
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Función showMessage
        function showMessage(message, type) {
            const messageContainer = document.getElementById('messageContainer');
            if (!messageContainer) {
                console.error("El contenedor de mensajes 'messageContainer' no fue encontrado.");
                return;
            }
            messageContainer.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                                ${message}
                                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                            </div>`;
            setTimeout(() => {
                const alertElement = messageContainer.querySelector('.alert');
                if (alertElement) {
                    alertElement.classList.remove('show');
                    alertElement.classList.add('fade');
                    setTimeout(() => alertElement.remove(), 150);
                }
            }, 5000);
        }

        // Event Listener para el botón Buscar
        document.getElementById("btnBuscar").addEventListener("click", function() {
            const buscarValorInput = document.getElementById("buscarValor");
            const tipoBusquedaSelect = document.getElementById("tipoBusqueda");

            if (!buscarValorInput || !tipoBusquedaSelect) {
                console.error("DEBUG JS: No se encontraron los elementos 'buscarValor' o 'tipoBusqueda'.");
                showMessage("Error interno: No se pudo acceder a los campos de búsqueda.", "danger");
                return;
            }

            const buscarValor = buscarValorInput.value;
            const tipoBusqueda = tipoBusquedaSelect.value;

            if (typeof buscarValor !== 'string' || buscarValor.trim() === "") {
                showMessage("Por favor, ingrese un valor para buscar.", "warning");
                return;
            }

            const formData = new FormData();
            formData.append("accion", "buscar");
            formData.append("valor", buscarValor);
            formData.append("busqueda", tipoBusqueda);

            const params = new URLSearchParams();
            for (let pair of formData.entries()) {
                params.append(pair[0], pair[1]);
            }

            fetch('SvProductos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params.toString()
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
                console.log("Respuesta del servidor (Buscar):", data);
                if (data.producto) {
                    document.getElementById('idProductoResultado').value = data.producto.id || '';
                    document.getElementById('nombreProductoResultado').value = data.producto.nombre || '';
                    document.getElementById('tipoProductoResultado').value = data.producto.tipoProducto || '';
                    document.getElementById('precioMayoristaResultado').value = data.producto.precioMayorista || '';
                    document.getElementById('precioMinoristaResultado').value = data.producto.precio || '';
                    document.getElementById('fechaVencimientoResultado').value = data.producto.fechaDeVencimiento ? new Date(data.producto.fechaDeVencimiento).toLocaleDateString('es-CO') : 'N/A';
                    document.getElementById('etiquetasResultado').value = (data.producto.etiquetas && data.producto.etiquetas.length > 0) ? data.producto.etiquetas.join(', ') : 'N/A';
                    document.getElementById('cantidadInventarioResultado').value = data.cantidad || '';
                    showMessage("Producto encontrado.", "success");
                } else {
                    // Limpiar campos si no se encuentra producto
                    document.getElementById('idProductoResultado').value = '';
                    document.getElementById('nombreProductoResultado').value = '';
                    document.getElementById('tipoProductoResultado').value = '';
                    document.getElementById('precioMayoristaResultado').value = '';
                    document.getElementById('precioMinoristaResultado').value = '';
                    document.getElementById('fechaVencimientoResultado').value = '';
                    document.getElementById('etiquetasResultado').value = '';
                    document.getElementById('cantidadInventarioResultado').value = '';
                    showMessage("Producto no encontrado.", "warning");
                }
            })
            .catch(error => {
                console.error("Error en la petición fetch (Buscar):", error);
                showMessage("Ha ocurrido un error en la comunicación con el servidor: " + error.message, "danger");
            });
        });

        // Event Listener para el botón Ajustar Cantidad
        document.getElementById("btnAjustar").addEventListener("click", function() {
            const idProducto = document.getElementById('idProductoResultado').value;
            const nuevaCantidad = document.getElementById('ajustarCantidadInput').value;

            if (idProducto.trim() === "") {
                showMessage("Primero, busque un producto para ajustar su cantidad.", "warning");
                return;
            }
            if (nuevaCantidad.trim() === "" || isNaN(nuevaCantidad)) {
                showMessage("Por favor, ingrese una cantidad numérica válida para ajustar.", "warning");
                return;
            }

            const formData = new FormData();
            formData.append("accion", "ajustar");
            formData.append("id", idProducto);
            formData.append("nuevaCantidad", nuevaCantidad);

            const params = new URLSearchParams();
            for (let pair of formData.entries()) {
                params.append(pair[0], pair[1]);
            }

            fetch('SvProductos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params.toString()
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
                console.log("Respuesta del servidor (Ajustar):", data);
                if (data.mensaje) {
                    showMessage(data.mensaje, "success");
                    // Opcional: Volver a buscar el producto para actualizar la cantidad mostrada
                    document.getElementById('btnBuscar').click();
                } else {
                    showMessage("Error al ajustar la cantidad: " + (data.error || "Mensaje desconocido."), "danger");
                }
            })
            .catch(error => {
                console.error("Error en la petición fetch (Ajustar):", error);
                showMessage("Ha ocurrido un error en la comunicación con el servidor al ajustar: " + error.message, "danger");
            });
        });

        // Event Listener para el botón Crear Nuevo Producto
        document.getElementById("btnCrearNuevo").addEventListener("click", function() {
            // Aquí puedes redirigir a otra página o abrir un modal para el formulario de creación
            showMessage("Funcionalidad 'Crear Nuevo Producto' no implementada aún en el frontend.", "info");
            console.log("DEBUG JS: Redirigiendo o abriendo modal para crear nuevo producto...");
        });
    </script>
</body>
</html>