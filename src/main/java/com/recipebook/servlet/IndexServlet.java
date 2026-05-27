package com.recipebook.servlet;

import java.io.IOException;

import com.recipebook.dao.SQLController;
import com.recipebook.dao.UserDao;
import com.recipebook.dao.VistaDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author jkqui
 */
@WebServlet(name = "IndexServlet", urlPatterns = { "/IndexServlet" })
public class IndexServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        // Conexión a PostgreSQL
        String connectionUrl = "jdbc:postgresql://localhost:5432/recipebook";
        String user = "giosreina";
        String password = "Kabuto43*";
        
        SQLController sqlController = new SQLController(connectionUrl, user, password);

        session.setAttribute("connectionUrl", connectionUrl);

        boolean connected = sqlController.isConnected();
        String conexion = connected ? "Conectado" : "Desconectado";
        session.setAttribute("conexion", conexion);

        UserDao userDao = new UserDao(sqlController);
        session.setAttribute("userDao", userDao);

        VistaDao vistaDao = new VistaDao(sqlController);
        session.setAttribute("vistaDao", vistaDao);

        response.sendRedirect("registro.jsp");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}