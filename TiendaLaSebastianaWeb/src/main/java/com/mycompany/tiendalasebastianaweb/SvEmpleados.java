/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.tiendalasebastianaweb;

import java.io.IOException;
import java.io.PrintWriter;

import BusinessLogic.Empleado;
import BusinessLogic.EmpleadoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian
 */
@WebServlet(name = "SvEmpleados", urlPatterns = {"/SvEmpleados"})
public class SvEmpleados extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Svempleados</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Svempleados at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        EmpleadoService service = null;

        try {
            service = new EmpleadoService();
        } catch (Exception ex) {
            Logger.getLogger(SvEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (accion == null || service == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        switch (accion) {
            case "login":
                String nombreLogin = request.getParameter("nombre");
                String documentoLogin = request.getParameter("documento");

                Empleado empleado = null;
                try {
                    empleado = service.validarDocumento(nombreLogin, documentoLogin);
                } catch (Exception ex) {
                    Logger.getLogger(SvEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (empleado != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("empleado", empleado);
                    response.sendRedirect("tienda.jsp");
                } else {
                    request.setAttribute("error", "Nombre o documento incorrecto");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
                break;

            case "registro":
                String nombreRegistro = request.getParameter("nombre");
                String documentoRegistro = request.getParameter("documento");

                boolean registrado = false;
                try {
                    registrado = service.agregarEmpleado(nombreRegistro, documentoRegistro);
                } catch (Exception ex) {
                    Logger.getLogger(SvEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (registrado) {
                    request.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
                } else {
                    request.setAttribute("error", "No válido, o ya existe un empleado con ese documento.");
                }

                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;

            default:
                
                response.sendRedirect("index.jsp");
                break;
        }
    }
}

