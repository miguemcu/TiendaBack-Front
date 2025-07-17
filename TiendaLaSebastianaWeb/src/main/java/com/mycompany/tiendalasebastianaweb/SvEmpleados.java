/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.tiendalasebastianaweb;

import java.io.IOException;
import java.util.Enumeration;

import BusinessLogic.Empleado;
import BusinessLogic.EmpleadoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Sebastian
 */
@WebServlet(name = "SvEmpleados", urlPatterns = {"/SvEmpleados"})
@MultipartConfig
public class SvEmpleados extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean esAjax = isAjax(request);
        response.setContentType("text/plain");
        try {
            String accion = request.getParameter("accion");
            System.out.println("DEBUG accion backend: " + accion);
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String param = paramNames.nextElement();
                System.out.println("PARAM: " + param + " = " + request.getParameter(param));
            }
            if (accion == null || accion.isEmpty()) {
                response.getWriter().write("Acción no especificada.");
                return;
            }

            EmpleadoService service = new EmpleadoService();

            if ("login".equals(accion)) {
                manejarLogin(request, response, service, esAjax);
                return;
            }
            if ("registro".equals(accion)) {
                manejarRegistro(request, response, service, esAjax);
                return;
            }
            // Si no es ninguna de las dos
            response.getWriter().write("Acción no reconocida.");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().write("Error interno del servidor.");
        }
    }

    private void manejarLogin(HttpServletRequest request, HttpServletResponse response,
                              EmpleadoService service, boolean esAjax) throws IOException, ServletException, Exception {
        String nombre = request.getParameter("nombre");
        String documento = request.getParameter("documento");

        Empleado empleado = service.validarDocumento(nombre, documento);

        if (empleado != null) {
            HttpSession session = request.getSession();
            session.setAttribute("empleado", empleado);

            if (esAjax) {
                response.getWriter().write("OK");
            } else {
                response.sendRedirect("tienda.jsp");
            }
        } else {
            if (esAjax) {
                response.getWriter().write("Nombre o documento incorrecto.");
            } else {
                request.setAttribute("error", "Nombre o documento incorrecto.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }

    private void manejarRegistro(HttpServletRequest request, HttpServletResponse response,
                                 EmpleadoService service, boolean esAjax) throws IOException, ServletException, Exception {
        String nombre = request.getParameter("nombre");
        String documento = request.getParameter("documento");

        // Normaliza y valida
        if (nombre == null || documento == null || nombre.trim().isEmpty() || documento.trim().isEmpty()) {
            if (esAjax) {
                response.getWriter().write("Nombre y documento son obligatorios.");
            } else {
                request.setAttribute("error", "Nombre y documento son obligatorios.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            return;
        }

        // Normaliza para evitar problemas de espacios
        nombre = nombre.trim();
        documento = documento.trim();

        boolean registrado = service.agregarEmpleado(nombre, documento);

        if (esAjax) {
            if (registrado) {
                response.getWriter().write("Registro exitoso. Ahora puedes iniciar sesión.");
            } else {
                response.getWriter().write("No válido, o ya existe un empleado con ese nombre o documento.");
            }
        } else {
            if (registrado) {
                request.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
            } else {
                request.setAttribute("error", "No válido, o ya existe un empleado con ese nombre o documento.");
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
