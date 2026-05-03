package com.recipebook.servlet;

import java.io.IOException;

import com.recipebook.dao.SQLController;
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
        String urlMultimedia = request.getParameter("urlMultimedia");
        String correo = request.getParameter("correo");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confPassword");
        HttpSession session = request.getSession();
        UserDao userDao = (UserDao) session.getAttribute("userDao");
        System.out.println("RegistroServlet - UserDao: " + userDao);
        System.out.println("RegistroServlet - Datos recibidos: username=" + username + ", correo=" + correo);

        if (userDao == null) {
            System.out.println("ERROR: UserDao es null, reinicializando conexión...");
            // Si UserDao es null, crear una nueva conexión
            String connectionUrl = "jdbc:postgresql://localhost:5432/recipebook";
            String dbUser = "giosreina";
            String dbPassword = "Kabuto43*";
            
            SQLController sqlController = new SQLController(connectionUrl, dbUser, dbPassword);
            if (sqlController.isConnected()) {
                userDao = new UserDao(sqlController);
                session.setAttribute("userDao", userDao);
                System.out.println("Conexión reestablecida correctamente");
            } else {
                request.setAttribute("mensaje", "Error: No se pudo conectar a la base de datos");
                request.setAttribute("mensajeClase", "error");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }
        }

        // Validar que los campos requeridos no estén vacíos
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

        // Validar formato de email
        if (!validarEmail(correo)) {
            request.setAttribute("mensaje", "El correo electrónico no es válido");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confPassword)) {
            request.setAttribute("mensaje", "Las contraseñas no coinciden");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Validar longitud de contraseña
        if (password.length() < 6) {
            request.setAttribute("mensaje", "La contraseña debe tener al menos 6 caracteres");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Validar longitud de username
        if (username.length() < 3) {
            request.setAttribute("mensaje", "El nombre de usuario debe tener al menos 3 caracteres");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        User newUser = new User(nombre_1.trim(), nombre_2 != null ? nombre_2.trim() : null, 
                                apellido_1.trim(), apellido_2 != null ? apellido_2.trim() : null, 
                                correo.trim(), username.trim(), password, 
                                urlMultimedia != null && !urlMultimedia.trim().isEmpty() ? urlMultimedia.trim() : null);
        System.out.println("Nuevo usuario: " + newUser.getUsername() + " - " + newUser.getCorreo());

        boolean success = userDao.agregarUsuario(newUser);
        System.out.println("Resultado de agregarUsuario: " + success);
        
        if (success) {
            System.out.println("Usuario registrado exitosamente");
            session.setAttribute("currentUser", newUser);
            response.sendRedirect("perfil.jsp?mensaje=Bienvenido%20" + username);
        } else {
            System.out.println("Error al registrar el usuario");
            request.setAttribute("mensaje", "Error al registrar el usuario. El username o correo ya existen");
            request.setAttribute("mensajeClase", "error");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    private boolean validarEmail(String email) {
        // Expresión regular para validar emails
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

