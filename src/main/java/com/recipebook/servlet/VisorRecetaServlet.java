package com.recipebook.servlet;

import java.io.IOException;

import com.recipebook.dao.RecetaDao;
import com.recipebook.dao.SQLController;
import com.recipebook.logic.Receta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "VisorRecetaServlet", urlPatterns = {"/VisorRecetaServlet"})
public class VisorRecetaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        // Si no viene el ID, volver a explorar
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect("explorar.html");
            return;
        }

        int idReceta;
        try {
            idReceta = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("explorar.html");
            return;
        }

        // Conexión fresca por request — consulta la receta directamente por su ID real
        SQLController sql = new SQLController(
            "jdbc:postgresql://localhost:5432/recipebook", "giosreina", "Kabuto43*"
        );

        try {
            if (!sql.isConnected()) {
                response.sendRedirect("explorar.html");
                return;
            }

            RecetaDao recetaDao = new RecetaDao(sql);
            Receta receta = recetaDao.obtenerReceta(idReceta);

            if (receta == null) {
                // La receta no existe en BD
                response.sendRedirect("explorar.html");
                return;
            }

            // Poner la receta en el request (no en sesión)
            request.setAttribute("receta", receta);
            request.setAttribute("pasos", receta.getPasos());
            request.getRequestDispatcher("receta.jsp").forward(request, response);

        } finally {
            sql.closeConnection();
        }
    }
}