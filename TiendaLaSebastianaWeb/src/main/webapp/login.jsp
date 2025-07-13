<%-- Document : index Created on : 12/07/2025, 7:19:47 p. m. Author : Sebastian --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <title>Tienda La Sebastiana - Iniciar Sesión / Registrarse</title>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
                crossorigin="anonymous">
        </head>

        <body class="bg-light">
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <div class="card shadow">
                            <div class="card-header text-center">
                                <ul class="nav nav-tabs card-header-tabs" id="authTabs" role="tablist">
                                    <li class="nav-item">
                                        <button class="nav-link active" id="login-tab" data-bs-toggle="tab"
                                            data-bs-target="#login" type="button" role="tab">Iniciar Sesión</button>
                                    </li>
                                    <li class="nav-item">
                                        <button class="nav-link" id="register-tab" data-bs-toggle="tab"
                                            data-bs-target="#register" type="button" role="tab">Registrarse</button>
                                    </li>
                                </ul>
                            </div>
                            <div class="card-body">
                                <div class="tab-content" id="authTabsContent">
                                    <!-- Formulario de Iniciar Sesión -->
                                    <div class="tab-pane fade show active" id="login" role="tabpanel">
                                        <form action="SvEmpleados" method="post">
                                            <input type="hidden" name="accion" value="login">
                                            <div class="mb-3">
                                                <label for="nombreLogin" class="form-label">Nombre completo</label>
                                                <input type="text" class="form-control" id="nombreLogin" name="nombre"
                                                    required maxlength="50" pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,50}"
                                                    title="Solo letras, máximo 50 caracteres">
                                            </div>
                                            <div class="mb-3">
                                                <label for="documentoLogin" class="form-label">Documento</label>
                                                <input type="text" class="form-control" id="documentoLogin"
                                                    name="documento" required minlength="8" maxlength="15"
                                                    pattern="\d{8,15}" title="Solo números, entre 8 y 15 dígitos">
                                            </div>
                                            <button type="submit" class="btn btn-primary w-100">Iniciar Sesión</button>
                                        </form>
                                    </div>

                                    <!-- Formulario de Registro -->
                                    <div class="tab-pane fade" id="register" role="tabpanel">
                                        <form action="SvEmpleados" method="post">
                                            <input type="hidden" name="accion" value="registro">
                                            <div class="mb-3">
                                                <label for="nombreregister" class="form-label">Nombre completo</label>
                                                <input type="text" class="form-control" id="nombreregister" name="nombre"
                                                    required maxlength="50" pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,50}"
                                                    title="Solo letras, máximo 50 caracteres">
                                            </div>
                                            <div class="mb-3">
                                                <label for="documentoRegister" class="form-label">Documento</label>
                                                <input type="text" class="form-control" id="documentoRegister" name="documento"
                                                    required minlength="8" maxlength="15" pattern="\d{8,15}"
                                                    title="Solo números, entre 8 y 15 dígitos">
                                            </div>
                                            <button type="submit" class="btn btn-success w-100">Registrarse</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <p class="text-center mt-3 text-muted">Nairo Quintana © 2025</p>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
                crossorigin="anonymous"></script>
        </body>