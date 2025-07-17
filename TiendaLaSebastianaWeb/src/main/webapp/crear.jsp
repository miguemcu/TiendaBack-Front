<%-- 
    Document   : crear
    Created on : 15/07/2025, 9:15:26 p. m.
    Author     : Sebastian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Nuevo Producto - Tienda La Sebastiana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <style>
        /* Estilos básicos para el mensaje de respuesta */
        .alert-container {
            min-height: 60px; /* Espacio para el mensaje */
            display: flex;
            align-items: center;
            justify-content: center;
        }
        /* Estilos para la imagen de error general */
        .image-container-error {
            text-align: center;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin: 20px auto;
            max-width: 400px;
        }
        .image-container-error img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
        }
        .image-container-error p {
            margin-top: 15px;
            font-size: 1.1em;
            color: #6c757d;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-header text-center bg-primary text-white">
                        <h3>Crear Nuevo Producto</h3>
                    </div>
                    <div class="card-body">
                        <!-- Contenedor para mensajes de éxito/error -->
                        <div id="messageContainer" class="alert-container mb-3"></div>

                        <form id="formCrearProducto">
                            <div class="mb-3">
                                <label for="idProducto" class="form-label">ID del Producto</label>
                                <input type="text" class="form-control" id="idProducto" name="id" 
                                       required pattern="[0-9]{1,10}" title="Solo números, máximo 10 dígitos">
                            </div>

                            <div class="mb-3">
                                <label for="nombreProducto" class="form-label">Nombre del Producto</label>
                                <!-- Se escapa el guion (-) en el patrón para evitar errores de sintaxis -->
                                <input type="text" class="form-control" id="nombreProducto" name="nombre" 
                                       required maxlength="100" pattern="[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ\s.,\-]{1,100}" 
                                       title="Solo letras, números, espacios y caracteres especiales como '.', ',', '-', máximo 100 caracteres">
                            </div>
                            
                            <div class="mb-3">
                                <label for="tipoProducto" class="form-label">Tipo de Producto</label>
                                <select class="form-select" id="tipoProducto" name="tipoProducto" required>
                                    <option value="" disabled selected>Seleccione un tipo</option>
                                    <option value="ASEO">ASEO</option>
                                    <option value="ENLATADO">ENLATADO</option>
                                    <option value="GRANOS">GRANOS</option>
                                    <option value="MECATO">MECATO</option>
                                    <option value="BEBIDA">BEBIDA</option>
                                    <option value="OTROS">Otros</option>
                                </select>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="precioMayorista" class="form-label">Precio Mayorista</label>
                                    <input type="number" class="form-control" id="precioMayorista" name="precioMayorista" 
                                           required min="0.01" step="0.01" title="Ingrese un precio válido (ej. 10.50)">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="precioMinorista" class="form-label">Precio Minorista</label>
                                    <input type="number" class="form-control" id="precioMinorista" name="precioMinorista" 
                                           required min="0.01" step="0.01" title="Ingrese un precio válido (ej. 15.99)">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="fechaVencimiento" class="form-label">Fecha de Vencimiento (YYYY-MM-DD)</label>
                                <input type="date" class="form-control" id="fechaVencimiento" name="fechaVencimiento">
                                <small class="form-text text-muted">Campo opcional. Formato: Año-Mes-Día.</small>
                            </div>

                            <div class="mb-3">
                                <label for="etiquetas" class="form-label">Etiquetas (separadas por coma)</label>
                                <input type="text" class="form-control" id="etiquetas" name="etiquetas" 
                                       placeholder="Ej: organico, sin gluten, oferta" maxlength="200">
                                <small class="form-text text-muted">Opcional. Separe las etiquetas con comas (ej. etiqueta1,etiqueta2).</small>
                            </div>

                            <div class="mb-3">
                                <label for="cantidad" class="form-label">Cantidad en Inventario</label>
                                <input type="number" class="form-control" id="cantidad" name="cantidad" 
                                       required min="0" title="Ingrese una cantidad válida (solo números enteros positivos)">
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100">Guardar Producto</button>
                        </form>
                    </div>
                </div>
                <p class="text-center mt-3 text-muted">Nairo Quintana © 2025</p>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const formCrearProducto = document.getElementById('formCrearProducto');
            const messageContainer = document.getElementById('messageContainer');

            // Función para mostrar mensajes de éxito o error en el contenedor
            function showMessage(message, type) {
                messageContainer.innerHTML = ''; // Limpiar mensajes anteriores
                
                // Si el mensaje es de ID o nombre ya usado, mostrar la imagen de error
                if (message === "Ya existe un producto con el ID proporcionado." || 
                    message === "Ya existe un producto con el nombre proporcionado.") {
                    // CORRECCIÓN: URL completa y correcta para la imagen de error
                   const imageUrl = "http://googleusercontent.com/file_content/uploaded:image_a80f84.png-e8593c38-9433-4336-979c-456e56ca7448"; // La imagen de error general
                messageContainer.innerHTML = `
                    <div class="image-container-error">
                        <img src="${imageUrl}" alt="ID o nombre existente" onerror="this.style.display='none'; console.error('DEBUG JS: La imagen de error no pudo cargar: ' + this.src);">
                        <p>ID o nombre existente</p>
                    </div>
                    `;
                    setTimeout(() => {
                        messageContainer.innerHTML = ''; // Limpiar el contenedor
                    }, 10000); // 10 segundos para la imagen
                } else {
                    // Para otros mensajes (éxito, errores generales de red/servidor)
                    const alertHtml = `
                        <div class="alert alert-${type} alert-dismissible fade show w-75 text-center" role="alert">
                            ${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    `;
                    messageContainer.innerHTML = alertHtml;
                    // Ocultar el mensaje después de 5 segundos
                    setTimeout(() => {
                        const alertElement = messageContainer.querySelector('.alert');
                        if (alertElement) {
                            alertElement.classList.remove('show');
                            alertElement.addEventListener('transitionend', function handler() {
                                alertElement.removeEventListener('transitionend', handler);
                                alertElement.remove();
                            });
                        }
                    }, 5000);
                }
            }

            // Event listener para el envío del formulario
            formCrearProducto.addEventListener('submit', function(event) {
                event.preventDefault(); // Prevenir el envío por defecto del formulario

                // Validar el formulario usando la API de validación HTML5
                if (!formCrearProducto.checkValidity()) {
                    formCrearProducto.classList.add('was-validated'); // Muestra las validaciones de Bootstrap
                    return;
                }
                formCrearProducto.classList.remove('was-validated'); // Quitar la clase si ya es válido

                const formData = new FormData(formCrearProducto);
                
                // Convertir FormData a un objeto plano para JSON
                const jsonObject = {};
                for (const [key, value] of formData.entries()) {
                    if (key === "etiquetas" && value.trim() !== "") {
                        // Convertir la cadena de etiquetas separadas por coma en un array
                        jsonObject[key] = value.split(',').map(tag => tag.trim());
                    } else if (key === "cantidad" || key === "id") {
                        // Convertir ID y cantidad a enteros
                        jsonObject[key] = parseInt(value);
                    } else if (key === "precioMayorista" || key === "precioMinorista") {
                        // Convertir precios a flotantes
                        jsonObject[key] = parseFloat(value);
                    }
                    else {
                        jsonObject[key] = value;
                    }
                }
                jsonObject.accion = "crear"; // Añadir la acción para el Servlet SvProductos
                
                // Eliminar fechaVencimiento si está vacía, para que no envíe una cadena vacía
                if (jsonObject.fechaVencimiento === "") {
                    delete jsonObject.fechaVencimiento;
                }

                console.log("DEBUG JS: JSON a enviar (Crear Producto):", JSON.stringify(jsonObject, null, 2));

                fetch('SvProductos', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest' // Indicar que es una petición AJAX
                    },
                    body: JSON.stringify(jsonObject)
                })
                .then(response => {
                    console.log("DEBUG JS: Respuesta fetch recibida. Status:", response.status);
                    if (!response.ok) {
                        // Si la respuesta HTTP no es 2xx (ej. 400, 409, 500), intentar leer el JSON de error
                        return response.json().then(errorData => {
                            console.error("DEBUG JS: ErrorData del servidor:", errorData);
                            // Usar el mensaje de error del servidor si está disponible, sino un mensaje genérico
                            const errorMessage = errorData.error || `Error del servidor: ${response.status} ${response.statusText}`;
                            throw new Error(errorMessage); // Propagar el error para el catch final
                        });
                    }
                    return response.json(); // Si la respuesta es OK, parsear el JSON de datos
                })
                .then(data => {
                    console.log("DEBUG JS: Datos recibidos (Crear Producto):", data);
                    if (data.mensaje) {
                        // Si el servidor devuelve un mensaje de éxito
                        showMessage(data.mensaje, 'success');
                        formCrearProducto.reset(); // Limpiar el formulario después del éxito
                        formCrearProducto.classList.remove('was-validated'); // Quitar la clase de validación
                    } else if (data.error) {
                        // Si el servidor devuelve un objeto con "error" (aunque la respuesta HTTP sea 200 OK)
                        showMessage(data.error, 'danger');
                    } else {
                        // Si el servidor devuelve un objeto con "error" (aunque la respuesta HTTP sea 200 OK)
                        // y el mensaje no coincide con los específicos, lo mostramos como un error genérico
                        showMessage('Error desconocido al crear el producto.', 'danger');
                    }
                })
                .catch(error => {
                    // Este catch final se ejecutará para errores de red o errores propagados desde los .then()
                    console.error("DEBUG JS: Error en la petición fetch:", error);
                    showMessage(error.message || 'Error de red o del servidor al crear el producto.', 'danger');
                });
            });
        });
    </script>
</body>
</html>



