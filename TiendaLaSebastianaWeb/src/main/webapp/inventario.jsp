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
    <!-- ASEGÚRATE DE QUE ESTAS URLs SEAN CORRECTAS Y ACCESIBLES. -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css"> 
    <style>
        /* Estilos para depuración visual del mensaje */
        /* Estos estilos estaban aquí para asegurar que el mensaje fuera visible incluso sin style.css.
           Ahora se eliminan para permitir la validación nativa del navegador y los estilos de Bootstrap. */
        .alert {
            /* border: 5px solid red !important; */ 
            /* font-weight: bold !important; */
            /* font-size: 24px !important; */
            /* padding: 2rem !important; */
            margin-bottom: 1rem !important; 
            display: block !important; 
            opacity: 1 !important; 
            visibility: visible !important; 
            /* color: black !important; */
            /* background-color: yellow !important; */
            /* border-color: red !important; */
            text-align: center !important; /* Centrar texto */
            min-width: 300px !important; /* Ancho mínimo para que quepa el texto */
            min-height: 100px !important; /* Alto mínimo */
            /* box-shadow: 0 0 15px rgba(255, 0, 0, 0.5) !important; */
        }
        .alert-success {
            background-color: #d4edda !important;
            border-color: #c3e6cb !important;
            color: #155724 !important;
        }
        .alert-warning {
            background-color: #fff3cd !important;
            border-color: #ffeeba !important;
            color: #856404 !important;
        }
        .alert-danger {
            background-color: #f8d7da !important;
            border-color: #f5c6cb !important;
            color: #721c24 !important;
        }
        .alert-info {
            background-color: #d1ecf1 !important;
            border-color: #bee5eb !important;
            color: #0c5460 !important;
        }
        #messageContainer {
            min-height: 120px; /* Un poco más de altura para el contenedor */
        }
        /* Estilos para la imagen de 404 */
        .image-container-404, .image-container-error {
            text-align: center;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin: 20px auto;
            max-width: 400px;
        }
        .image-container-404 img, .image-container-error img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
        }
        .image-container-404 p, .image-container-error p {
            margin-top: 15px;
            font-size: 1.1em;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>Gestión de Inventario</h1>
        
        <div id="messageContainer" class="mt-3"></div>

        <div class="mb-3">
            <label for="buscarValor" class="form-label">Buscar por Nombre o ID:</label>
            <div class="input-group">
                <input type="text" class="form-control" id="buscarValor" placeholder="Ingrese ID o Nombre del producto" required
                minlength="1" maxlength="50" pattern="[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ ]{1,50}" 
                title="Solo letras, números y espacios, entre 1 y 50 caracteres">
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
                <input type="number" class="form-control" id="ajustarCantidadInput" placeholder="Ingrese la nueva cantidad para el producto buscado"
                required minlength="1" maxlength="10" pattern="\d{1,10}"
                title="Solo números, entre 1 y 10 dígitos">

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
        function showMessage(message, type) {
            console.log(`DEBUG JS: showMessage llamado con: mensaje='${message}', tipo='${type}'`);
            const messageContainer = document.getElementById('messageContainer');
            if (!messageContainer) {
                console.error("DEBUG JS: El contenedor de mensajes 'messageContainer' no fue encontrado.");
                return;
            }
            messageContainer.innerHTML = ''; // Limpiar mensajes anteriores
            
            if (type === 'image-404') {
                const imageUrl = "http://googleusercontent.com/file_content/uploaded:image_a865bd.png-ff617340-3f59-4cac-825a-0e05cedb401a";
                messageContainer.innerHTML = `
                    <div class="image-container-404">
                        <img src="${imageUrl}" alt="ID o nombre inexistente" onerror="this.style.display='none'; console.error('DEBUG JS: La imagen 404 no pudo cargar: ' + this.src);">
                        <p>ID o nombre inexistente</p>
                    </div>
                `;
                // Set timeout to clear the image after 10 seconds
                setTimeout(() => {
                    console.log("DEBUG JS: Intentando ocultar imagen de 404...");
                    messageContainer.innerHTML = ''; // Clear the container
                    console.log("DEBUG JS: Imagen de 404 ocultada y removida del DOM.");
                }, 10000);
            } else if (type === 'image-error') {
                const imageUrl = "http://googleusercontent.com/file_content/uploaded:image_a80f84.png-e8593c38-9433-4336-979c-456e56ca7448"; // La imagen de error general
                messageContainer.innerHTML = `
                    <div class="image-container-error">
                        <img src="${imageUrl}" alt="ID o nombre inexistente" onerror="this.style.display='none'; console.error('DEBUG JS: La imagen de error no pudo cargar: ' + this.src);">
                        <p>ID o nombre inexistente</p>
                    </div>
                `;
                // Set timeout to clear the image after 10 seconds
                setTimeout(() => {
                    console.log("DEBUG JS: Intentando ocultar imagen de error...");
                    messageContainer.innerHTML = ''; // Clear the container
                    console.log("DEBUG JS: Imagen de error ocultada y removida del DOM.");
                }, 10000);
            }
            else {
                const displayMessage = (message && String(message).trim() !== '') ? String(message).trim() : 'Ha ocurrido un error inesperado.';
                
                messageContainer.innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
                                                    ${displayMessage}
                                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                                </div>`;
                
                const alertElement = messageContainer.querySelector('.alert');
                if (alertElement) {
                    console.log("DEBUG JS: Contenido HTML de la alerta (outerHTML):", alertElement.outerHTML);
                    console.log("DEBUG JS: Texto interno de la alerta (textContent.trim()):", alertElement.textContent.trim());
                    console.log("DEBUG JS: innerHTML de la alerta:", alertElement.innerHTML);
                    const computedStyle = window.getComputedStyle(alertElement);
                    console.log("DEBUG JS: Estilo computado - display:", computedStyle.display);
                    console.log("DEBUG JS: Estilo computado - visibility:", computedStyle.visibility);
                    console.log("DEBUG JS: Estilo computado - opacity:", computedStyle.opacity);
                    console.log("DEBUG JS: Estilo computado - color:", computedStyle.color);
                    console.log("DEBUG JS: Estilo computado - background-color:", computedStyle.backgroundColor);
                    console.log("DEBUG JS: Estilo computado - font-size:", computedStyle.fontSize);
                    console.log("DEBUG JS: Estilo computado - line-height:", computedStyle.lineHeight);
                    console.log("DEBUG JS: Estilo computado - overflow-x:", computedStyle.overflowX);
                    console.log("DEBUG JS: Estilo computado - overflow-y:", computedStyle.overflowY);
                    console.log("DEBUG JS: Estilo computado - text-indent:", computedStyle.textIndent);
                    console.log("DEBUG JS: Estilo computado - white-space:", computedStyle.whiteSpace);

                } else {
                    console.error("DEBUG JS: No se pudo encontrar el elemento de alerta recién creado.");
                }

                setTimeout(() => {
                    console.log("DEBUG JS: Intentando ocultar mensaje...");
                    const alertElement = messageContainer.querySelector('.alert');
                    if (alertElement) {
                        if (alertElement.classList.contains('fade')) {
                             alertElement.classList.remove('show');
                             alertElement.addEventListener('transitionend', function handler() {
                                 alertElement.removeEventListener('transitionend', handler);
                                 alertElement.remove();
                                 console.log("DEBUG JS: Mensaje ocultado y removido del DOM.");
                             });
                        } else {
                            alertElement.remove();
                            console.log("DEBUG JS: Mensaje removido del DOM (sin fade).");
                        }
                    } else {
                        console.log("DEBUG JS: No se encontró alertElement para ocultar.");
                    }
                }, 10000); // 10 segundos
            }
        }

        function clearProductResults() {
            document.getElementById('idProductoResultado').value = '';
            document.getElementById('nombreProductoResultado').value = '';
            document.getElementById('tipoProductoResultado').value = '';
            document.getElementById('precioMayoristaResultado').value = '';
            document.getElementById('precioMinoristaResultado').value = '';
            document.getElementById('fechaVencimientoResultado').value = '';
            document.getElementById('etiquetasResultado').value = '';
            document.getElementById('cantidadInventarioResultado').value = '';
        }

        document.addEventListener('DOMContentLoaded', function() {
            console.log("DEBUG JS: DOMContentLoaded disparado. El script principal está intentando inicializarse.");

            const btnBuscar = document.getElementById("btnBuscar");
            const buscarValorInput = document.getElementById("buscarValor");
            const tipoBusquedaSelect = document.getElementById("tipoBusqueda");

            if (!btnBuscar || !buscarValorInput || !tipoBusquedaSelect) {
                console.error("DEBUG JS: ¡ERROR CRÍTICO! No se encontraron los elementos básicos del formulario de búsqueda. El script no se puede inicializar correctamente.");
                showMessage("Error grave: La interfaz de usuario no se cargó completamente. Por favor, recargue la página.", "danger");
                return;
            }
            console.log("DEBUG JS: Elementos de búsqueda encontrados. Adjuntando listener a btnBuscar.");

            btnBuscar.addEventListener("click", function(event) {
                // Permitir que la validación nativa del navegador se encargue
                // de los campos requeridos y patrones.
                // Si el campo no es válido, el evento.preventDefault() no se ejecutará
                // y el navegador mostrará su mensaje.
                if (!buscarValorInput.checkValidity()) {
                    return; 
                }
                event.preventDefault(); // Prevenir el comportamiento por defecto del formulario solo si es válido
                console.log("DEBUG JS: Botón Buscar clickeado.");

                const buscarValor = buscarValorInput.value.trim();
                const tipoBusqueda = tipoBusquedaSelect.value;

                console.log(`DEBUG JS: Valores de input obtenidos - Valor: '${buscarValor}', Tipo: '${tipoBusqueda}'`);

                const requestBody = {
                    accion: "buscar",
                    valor: buscarValor,
                    busqueda: tipoBusqueda
                };

                console.log("DEBUG JS: JSON a enviar (Buscar):", JSON.stringify(requestBody, null, 2));

                fetch('SvProductos', {
                    method: 'POST', 
                    headers: {
                        'Content-Type': 'application/json' 
                    },
                    body: JSON.stringify(requestBody) 
                })
                .then(response => {
                    console.log("DEBUG JS: Respuesta fetch recibida. Status:", response.status);
                    // Si la respuesta no es OK, intentar parsear el JSON de error
                    if (!response.ok) {
                        return response.json()
                            .then(errorData => {
                                console.log("DEBUG JS: errorData recibido de la respuesta no-OK:", errorData);
                                let errorMessage = `Error HTTP: ${response.status} - ${response.statusText}`;
                                if (errorData && typeof errorData === 'object' && typeof errorData.error === 'string') {
                                    errorMessage = errorData.error.trim();
                                }
                                console.log(`DEBUG JS: Mensaje de error extraído: '${errorMessage}'`);
                                
                                // Lógica para mostrar imagen o mensaje de texto
                                if (errorMessage === "Producto no encontrado.") {
                                    showMessage('', 'image-404'); // Mostrar imagen para "Producto no encontrado."
                                } else {
                                    showMessage('', 'image-error'); // Mostrar imagen de error general
                                }
                                return Promise.reject(new Error(errorMessage)); // Rechazar la promesa para detener la cadena
                            })
                            .catch(jsonParseError => {
                                // Este catch se ejecuta si response.json() falla (ej. la respuesta no es JSON)
                                console.error("DEBUG JS: Fallo al parsear la respuesta JSON del servidor:", jsonParseError);
                                const errorMessage = `Error HTTP: ${response.status} - ${response.statusText}. La respuesta no es JSON o está corrupta.`;
                                showMessage('', 'image-error'); // Mostrar imagen de error general
                                return Promise.reject(new Error(errorMessage));
                            });
                    }
                    // Si la respuesta es OK, parsear JSON de datos
                    return response.json(); 
                })
                .then(data => {
                    console.log("DEBUG JS: Datos JSON recibidos (Buscar):", data);
                    if (data.producto) {
                        document.getElementById('idProductoResultado').value = data.producto.id || '';
                        document.getElementById('nombreProductoResultado').value = data.producto.nombre || '';
                        document.getElementById('tipoProductoResultado').value = data.producto.tipoProducto || '';
                        document.getElementById('precioMayoristaResultado').value = data.producto.precioMayorista || '';
                        document.getElementById('precioMinoristaResultado').value = data.producto.precio || '';
                        document.getElementById('fechaVencimientoResultado').value = data.producto.fechaDeVencimiento || 'N/A';
                        document.getElementById('etiquetasResultado').value = (data.producto.etiquetas && data.producto.etiquetas.length > 0) ? data.producto.etiquetas.join(', ') : 'N/A';
                        document.getElementById('cantidadInventarioResultado').value = data.cantidad || '';
                        // NO MOSTRAR MENSAJE DE ÉXITO SEGÚN LA SOLICITUD DEL USUARIO
                        // showMessage("Producto encontrado.", "success"); 
                    } else if (data.error) {
                        // Si el servidor devuelve un objeto con "error" (ej. "Producto no encontrado.") con status OK
                        clearProductResults(); 
                        if (data.error === "Producto no encontrado.") {
                            showMessage('', 'image-404'); // Mostrar imagen para "Producto no encontrado."
                        } else {
                            showMessage('', 'image-error'); // Mostrar imagen de error general
                        }
                    } else {
                        clearProductResults();
                        showMessage('', 'image-error'); // Fallback para respuesta inesperada
                    }
                })
                .catch(error => {
                    // Este catch final solo se ejecutará si hay un error de red
                    // o si la promesa fue rechazada explícitamente y no se manejó antes en el .then(response => ...)
                    console.error("DEBUG JS: Error final en la petición fetch (Buscar):", error);
                    showMessage('', 'image-error'); // Mostrar imagen de error general para errores de red
                });
            });

            const btnAjustar = document.getElementById("btnAjustar");
            const idProductoResultado = document.getElementById('idProductoResultado');
            const ajustarCantidadInput = document.getElementById('ajustarCantidadInput');

            if (btnAjustar && idProductoResultado && ajustarCantidadInput) {
                btnAjustar.addEventListener("click", function(event) {
                    // Permitir que la validación nativa del navegador se encargue
                    // de los campos requeridos y patrones.
                    if (!ajustarCantidadInput.checkValidity()) {
                        return;
                    }
                    if (idProductoResultado.value.trim() === "") {
                        // Este caso es una validación de lógica de negocio, no de HTML5,
                        // por lo que se sigue usando showMessage.
                        showMessage("Primero, busque un producto para ajustar su cantidad.", "warning");
                        return;
                    }
                    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario solo si es válido
                    console.log("DEBUG JS: Botón Ajustar clickeado.");

                    const idProducto = idProductoResultado.value.trim();
                    const nuevaCantidad = ajustarCantidadInput.value.trim();

                    const requestBody = {
                        accion: "ajustar",
                        id: idProducto,
                        nuevaCantidad: nuevaCantidad
                    };

                    console.log("DEBUG JS: JSON a enviar (Ajustar):", JSON.stringify(requestBody, null, 2));

                    fetch('SvProductos', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(requestBody)
                    })
                    .then(response => {
                        console.log("DEBUG JS: Respuesta fetch recibida (Ajustar). Status:", response.status);
                        if (!response.ok) {
                            return response.json()
                                .then(errorData => {
                                    console.log("DEBUG JS: errorData recibido de la respuesta no-OK (Ajustar):", errorData);
                                    let errorMessage = `Error HTTP: ${response.status} - ${response.statusText}`;
                                    if (errorData && typeof errorData === 'object' && typeof errorData.error === 'string') {
                                        errorMessage = errorData.error.trim();
                                    }
                                    console.log(`DEBUG JS: Mensaje de error extraído (Ajustar): '${errorMessage}'`);
                                    
                                    if (errorMessage === "Producto no encontrado.") {
                                        showMessage('', 'image-404');
                                    } else {
                                        showMessage('', 'image-error');
                                    }
                                    return Promise.reject(new Error(errorMessage));
                                })
                                .catch(e => {
                                    console.error("DEBUG JS: Fallo al procesar JSON de error o errorData.error es nulo (Ajustar):", e);
                                    showMessage('', 'image-error');
                                    return Promise.reject(new Error(`Error HTTP: ${response.status} - ${response.statusText}`));
                                });
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log("DEBUG JS: Datos JSON recibidos (Ajustar):", data);
                        if (data.mensaje) {
                            showMessage(data.mensaje, "success"); // Aquí sí se muestra mensaje de éxito para el ajuste
                            document.getElementById('btnBuscar').click(); 
                        } else if (data.error) {
                            if (data.error === "Producto no encontrado.") {
                                showMessage('', 'image-404');
                            } else {
                                showMessage('', 'image-error');
                            }
                        } else {
                            showMessage('', 'image-error');
                        }
                    })
                    .catch(error => {
                        console.error("DEBUG JS: Error final en la petición fetch (Ajustar):", error);
                        showMessage('', 'image-error');
                    });
                });
            } else {
                console.warn("DEBUG JS: Elementos para el ajuste de cantidad no encontrados. El botón de ajuste no funcionará.");
            }

            const btnCrearNuevo = document.getElementById("btnCrearNuevo");
            if (btnCrearNuevo) {
                btnCrearNuevo.addEventListener("click", function() {
                    console.log("DEBUG JS: Botón Crear Nuevo clickeado.");
                    // Redirige a crear.jsp
                    window.location.href = 'crear.jsp';
                });
            } else {
                console.warn("DEBUG JS: El botón 'Crear Nuevo Producto' no fue encontrado.");
            }
        });
    </script>
</body>
</html>


