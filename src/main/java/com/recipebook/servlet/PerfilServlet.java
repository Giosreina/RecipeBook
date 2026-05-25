package com.recipebook.servlet;

import com.recipebook.logic.User;
import com.recipebook.dao.VistaDao;
import com.recipebook.logic.Receta;
import com.recipebook.logic.RecetasContainer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/PerfilServlet")
public class PerfilServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null) {
            RecetasContainer recetasContainer = currentUser.getRecetas();
            if (recetasContainer != null) {
                List<Receta> recetas = recetasContainer.getRecetas();
                request.setAttribute("userRecetas", recetas);
            }
        }

        // NUEVO: top 5 recetas para mostrar en el perfil

        VistaDao vistaDao = (VistaDao) session.getAttribute("vistaDao");
        if (vistaDao != null) {
            List<VistaDao.RecetaMejorValorada> top5 = vistaDao.obtenerRecetasMejorValoradas();
            request.setAttribute("top5Recetas", top5);
 
            // NUEVO: usernames con recetas 
            List<String> usuariosActivos = vistaDao.obtenerUsernamesConRecetas();
            request.setAttribute("usuariosActivos", usuariosActivos);
        }

        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }
}


