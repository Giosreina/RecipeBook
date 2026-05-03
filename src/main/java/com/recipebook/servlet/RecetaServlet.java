package com.recipebook.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.recipebook.dao.RecetaDao;
import com.recipebook.dao.UserDao;
import com.recipebook.logic.Receta;
import com.recipebook.logic.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 
 * @author Juan Quintero
 * @author Giovanni
 * @author David
 * 
 * @version 1.0
 * @since 2024-10-01
 */
@WebServlet(name = "RecetaServlet", urlPatterns = { "/RecetaServlet" })
public class RecetaServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String tipo = request.getParameter("tipo");
        String imagen = request.getParameter("imagen");
        String descripcion = request.getParameter("descripcion");

        String ingredientesJson = request.getParameter("ingredientes");
        String[] ingredientes = new String[0];
        if (ingredientesJson != null) {
            ingredientes = new Gson().fromJson(ingredientesJson, String[].class);
        }

        String utensiliosJson = request.getParameter("utensilios");
        String[] utensilios = new String[0];
        if (utensiliosJson != null) {
            utensilios = new Gson().fromJson(utensiliosJson, String[].class);
        }

        Receta receta = new Receta(nombre, imagen, descripcion, tipo);

        String pasosJson = request.getParameter("pasos");
        if (pasosJson != null) {
            Map<String, String>[] mapPasos;
            mapPasos = new Gson().fromJson(pasosJson, Map[].class);
            
            for (Map<String, String> p : mapPasos) {
                String descripcionPaso = (String) p.get("descripcion");
                String tiempoStr = (String) p.get("tiempo");
                int tiempo = 0;
                if (tiempoStr != null && !tiempoStr.isEmpty()) {
                    tiempo = Integer.parseInt(tiempoStr);
                }
                String imagenPaso = (String) p.get("imagen");
                
                Object utensiliosPasoObj = p.get("utensilios");
                Object ingredientesPasoObj = p.get("ingredientes");
                
                String[] utensiliosPaso = new String[0];
                String[] ingredientesPaso = new String[0];
                
                if (utensiliosPasoObj != null && !utensiliosPasoObj.toString().isEmpty()) {
                    String utensiliosPasoStr = utensiliosPasoObj.toString().replaceAll("\\[|\\]|\"", "");
                    if (!utensiliosPasoStr.isEmpty()) {
                        utensiliosPaso = utensiliosPasoStr.split(",\\s*");
                    }
                }
                
                if (ingredientesPasoObj != null && !ingredientesPasoObj.toString().isEmpty()) {
                    String ingredientesPasoStr = ingredientesPasoObj.toString().replaceAll("\\[|\\]|\"", "");
                    if (!ingredientesPasoStr.isEmpty()) {
                        ingredientesPaso = ingredientesPasoStr.split(",\\s*");
                    }
                }

                if ((utensiliosPaso.length > 0 && utensiliosPaso[0].length() > 0) || 
                    (ingredientesPaso.length > 0 && ingredientesPaso[0].length() > 0)) {
                    receta.addStep(descripcionPaso, tiempo, utensiliosPaso, ingredientesPaso, imagenPaso);
                } else {
                    receta.addStep(descripcionPaso, tiempo, imagenPaso);
                }
            }
        }

        for (String ingrediente : ingredientes) {
            if (ingrediente != null && !ingrediente.isEmpty()) {
                receta.addIngrediente(ingrediente);
            }
        }
        
        for (String utensilio : utensilios) {
            if (utensilio != null && !utensilio.isEmpty()) {
                receta.addUtensilio(utensilio);
            }
        }
        
        System.out.println("Receta agregada: " + nombre);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            request.setAttribute("error", "No hay usuario autenticado");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        user.addReceta(receta);
        
        UserDao userDao = (UserDao) session.getAttribute("userDao");
        if (userDao == null) {
            request.setAttribute("error", "Error en la conexión a la base de datos");
            request.getRequestDispatcher("subir.html").forward(request, response);
            return;
        }

        int id = userDao.obtenerUserID(user.getUsername());

        RecetaDao recetaDAO = userDao.getRecetaDAO();
        boolean success = recetaDAO.agregarReceta(receta, id);

        if (success) {
            session.setAttribute("receta", receta);
            session.setAttribute("pasos", receta.getPasos());
            response.sendRedirect("./receta.jsp");
        } else {
            request.setAttribute("error", "Error al guardar la receta");
            request.getRequestDispatcher("subir.html").forward(request, response);
        }
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
