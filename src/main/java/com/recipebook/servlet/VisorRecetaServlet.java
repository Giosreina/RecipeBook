/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.recipebook.servlet;

import java.io.IOException;
import java.util.List;

import com.recipebook.logic.Receta;
import com.recipebook.logic.RecetasContainer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author jkqui
 */
@WebServlet(name = "VisorRecetaServlet", urlPatterns = {"/VisorRecetaServlet"})
public class VisorRecetaServlet extends HttpServlet {

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
