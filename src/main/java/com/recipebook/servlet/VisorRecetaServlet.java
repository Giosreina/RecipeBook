package com.recipebook.servlet;

import java.io.IOException;

<<<<<<< HEAD
import com.recipebook.logic.Receta;
import com.recipebook.logic.RecetasContainer;
=======
import com.recipebook.dao.RecetaDao;
import com.recipebook.dao.SQLController;
import com.recipebook.logic.Receta;
>>>>>>> dev

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "VisorRecetaServlet", urlPatterns = {"/VisorRecetaServlet"})
public class VisorRecetaServlet extends HttpServlet {

<<<<<<< HEAD
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
        // Get receta ID from request parameter
        String recetaId = request.getParameter("id");

        if (recetaId != null) {
            try {
                int id = Integer.parseInt(recetaId);
                
                // Get the recetas container from session
                @SuppressWarnings("unchecked")
                List<Receta> rContainer =  (List<Receta>) request.getSession().getAttribute("listRecetas");
                
                // Check if rContainer is null
                if (rContainer == null) {
                    System.out.println("Error: listRecetas not found in session");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No recipes available in session");
                    return;
                }
                
                RecetasContainer container = new RecetasContainer();
                container.setRecetas(rContainer);
                
                // Get the specific receta
                Receta receta = container.selectReceta(id);
                
                // Check if receta was found
                if (receta == null) {
                    System.out.println("Error: Receta not found with id: " + id);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Recipe not found");
                    return;
                }
                
                // Store receta and its steps in session for JSP access
                request.getSession().setAttribute("receta", receta);
                request.getSession().setAttribute("pasos", receta.getPasos());
                
                // Forward to receta.jsp
                response.sendRedirect("./receta.jsp");
                
            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid recipe ID");
            }
        } else {
            System.out.println("Error: recetaId is null");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Recipe ID parameter missing");
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
=======
>>>>>>> dev
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