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

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nombre_1      = request.getParameter("nombre_1");
        String nombre_2      = request.getParameter("nombre_2");
        String apellido_1    = request.getParameter("apellido_1");
        String apellido_2    = request.getParameter("apellido_2");
        String urlMultimedia = request.getParameter("urlMultimedia");
        String correo        = request.getParameter("correo");
        String username      = request.getParameter("username");
        String password      = request.getParameter("password");
        String confPassword  = request.getParameter("confPassword");

        // Siempre usar DAOFactory como fuente única de credenciales
        SQLController sqlController = DAOFactory.crearConexion();

        if (!sqlController.isConnected()) {
            request.setAttribute("mensaje", "Error: No se pudo conectar a la base de datos");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        UserDao userDao = new UserDao(sqlController);

        // Validaciones
        if (nombre_1 == null || nombre_1.trim().isEmpty() ||
            apellido_1 == null || apellido_1.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            correo == null || correo.trim().isEmpty()) {
            request.setAttribute("mensaje", "Por favor, completa todos los campos requeridos");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (!validarEmail(correo)) {
            request.setAttribute("mensaje", "El correo electrónico no es válido");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confPassword)) {
            request.setAttribute("mensaje", "Las contraseñas no coinciden");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("mensaje", "La contraseña debe tener al menos 6 caracteres");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (username.length() < 3) {
            request.setAttribute("mensaje", "El nombre de usuario debe tener al menos 3 caracteres");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        User newUser = new User(
            nombre_1.trim(),
            nombre_2 != null ? nombre_2.trim() : null,
            apellido_1.trim(),
            apellido_2 != null ? apellido_2.trim() : null,
            correo.trim(),
            username.trim(),
            password,
            urlMultimedia != null && !urlMultimedia.trim().isEmpty() ? urlMultimedia.trim() : null
        );

        boolean success = userDao.agregarUsuario(newUser);

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", newUser);

            // Reutilizar la misma conexión para la sesión (ya está abierta y validada)
            session.setAttribute("vistaDao", new VistaDao(sqlController));
            session.setAttribute("userDao",  userDao);
            session.setAttribute("conexion",  "Conectado");

            response.sendRedirect("perfil.jsp?mensaje=Bienvenido%20" + username);
        } else {
            sqlController.closeConnection();
            request.setAttribute("mensaje", "Error al registrar el usuario. El username o correo ya existen");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    private boolean validarEmail(String email) {
        String regexEmail = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return email != null && email.matches(regexEmail);
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
