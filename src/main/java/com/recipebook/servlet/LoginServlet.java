package com.recipebook.servlet;

import java.io.IOException;

import com.recipebook.dao.DAOFactory;
import com.recipebook.dao.SQLController;
import com.recipebook.dao.UserDao;
import com.recipebook.dao.VistaDao;
import com.recipebook.logic.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Conexión temporal solo para autenticar
        SQLController sqlAuth = DAOFactory.crearConexion();
        try {
            if (!sqlAuth.isConnected()) {
                request.setAttribute("mensaje", "Error: No se pudo conectar a la base de datos");
                request.setAttribute("mensajeClase", "error");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            UserDao userDao = new UserDao(sqlAuth);
            User user = userDao.obtenerUsuario(username);

            if (user == null) {
                request.setAttribute("mensaje", "El nombre de usuario no está registrado");
                request.setAttribute("mensajeClase", "error");
            } else if (!user.validatePassword(password)) {
                request.setAttribute("mensaje", "Contraseña incorrecta");
                request.setAttribute("mensajeClase", "error");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user);

                // Crear una conexión de sesión dedicada para VistaDao y UserDao
                SQLController sqlSession = DAOFactory.crearConexion();
                if (sqlSession.isConnected()) {
                    session.setAttribute("vistaDao", new VistaDao(sqlSession));
                    session.setAttribute("userDao",  new UserDao(sqlSession));
                    session.setAttribute("conexion", "Conectado");
                }

                response.sendRedirect("PerfilServlet");
                return;
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } finally {
            sqlAuth.closeConnection();
        }
    }
}

