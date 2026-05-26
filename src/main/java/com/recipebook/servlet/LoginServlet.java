package com.recipebook.servlet;

import java.io.IOException;

import com.recipebook.dao.DAOFactory;
import com.recipebook.dao.SQLController;
import com.recipebook.dao.UserDao;
import com.recipebook.logic.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SQLController sql = DAOFactory.crearConexion();
        try {
            if (!sql.isConnected()) {
                request.setAttribute("mensaje", "Error: No se pudo conectar a la base de datos");
                request.setAttribute("mensajeClase", "error");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            UserDao userDao = new UserDao(sql);
            User user = userDao.obtenerUsuario(username);

            if (user == null) {
                request.setAttribute("mensaje", "El nombre de usuario no está registrado");
                request.setAttribute("mensajeClase", "error");
            } else if (!user.validatePassword(password)) {
                request.setAttribute("mensaje", "Contraseña incorrecta");
                request.setAttribute("mensajeClase", "error");
            } else {
                request.getSession().setAttribute("currentUser", user);
                response.sendRedirect("PerfilServlet");
                return;
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } finally {
            sql.closeConnection(); 
        }
    }
}

