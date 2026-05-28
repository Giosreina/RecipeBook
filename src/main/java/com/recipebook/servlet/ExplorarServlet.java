package com.recipebook.servlet;

import java.io.IOException;
import java.util.List;

import com.recipebook.dao.DAOFactory;
import com.recipebook.dao.SQLController;
import com.recipebook.dao.VistaDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ExplorarServlet", urlPatterns = {"/ExplorarServlet"})
public class ExplorarServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Conexión fresca por request; se cierra al terminar
        SQLController sql = DAOFactory.crearConexion();
        try {
            VistaDao vistaDao = new VistaDao(sql);

            // Vista 1 — top 5 recetas mejor valoradas
            List<VistaDao.RecetaMejorValorada> top5 = vistaDao.obtenerRecetasMejorValoradas();
            request.setAttribute("top5Recetas", top5);

            // Vista 2 — reporte por tipo (conteos por categoría)
            List<VistaDao.ReporteReceta> reporteTipos = vistaDao.obtenerReporteRecetas();
            request.setAttribute("reporteTipos", reporteTipos);

            // Vista 4 — recetas rápidas (<= 30 min)
            List<VistaDao.RecetaRapida> recetasRapidas = vistaDao.obtenerRecetasRapidas();
            request.setAttribute("recetasRapidas", recetasRapidas);

        } finally {
            sql.closeConnection();
        }

        // forward conserva los atributos del request; sendRedirect los pierde
        request.getRequestDispatcher("explorar.jsp").forward(request, response);
    }

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
}