package com.recipebook.servlet;

import java.io.IOException;

import com.recipebook.dao.UserDao;
import com.recipebook.logic.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String nombre_1 = request.getParameter("nombre_1");
        String nombre_2 = request.getParameter("nombre_2");
        String apellido_1 = request.getParameter("apellido_1");
        String apellido_2 = request.getParameter("apellido_2");
        String urlMultimedia = request.getParameter("UrlImagen");
        String correo = request.getParameter("correo");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        UserDao userDao = (UserDao) session.getAttribute("userDao");
        System.out.println("UserDao: " + userDao);


        User newUser = new User(nombre_1, nombre_2, apellido_1, apellido_2, correo, username, password, urlMultimedia);
        System.out.println("New User: " + newUser.getUsername());

        userDao.agregarUsuario(newUser);

        session.setAttribute("currentUser", newUser);
        response.sendRedirect("perfil.jsp");
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

