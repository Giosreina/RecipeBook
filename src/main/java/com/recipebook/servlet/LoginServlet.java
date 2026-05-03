package com.recipebook.servlet;

import com.recipebook.logic.User;
import com.recipebook.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        HttpSession session = request.getSession();
        UserDao userDao = (UserDao) session.getAttribute("userDao");
        
        if (userDao == null) {
            request.setAttribute("mensaje", "Error: No se pudo conectar a la base de datos");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("mensaje", "Por favor, ingresa el usuario y la contraseña");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        User user = userDao.obtenerUsuario(username);
        
        if (user == null) {
            request.setAttribute("mensaje", "El nombre de usuario no está registrado");
            request.setAttribute("mensajeClase", "error");
        } else if (!user.validatePassword(password)) {
            request.setAttribute("mensaje", "Contraseña incorrecta");
            request.setAttribute("mensajeClase", "error");
        } else {
            session.setAttribute("currentUser", user);
            request.setAttribute("mensaje", "Inicio de sesión exitoso");
            request.setAttribute("mensajeClase", "success");
            response.sendRedirect("perfil.jsp");
            return;
        }
        
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}

