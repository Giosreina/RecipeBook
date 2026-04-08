<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.recipebook.logic.User" %>
<%@ page import="com.recipebook.logic.UsersContainer" %>
<%@ page import="com.recipebook.dao.UserDao" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.stream.*" %>
<%
    UsersContainer usersContainer = ((UserDao)session.getAttribute("userDao")).obtenerUsuarios();
    if(usersContainer == null){
        usersContainer = new UsersContainer();
    }
    List<String> usernames = new ArrayList<>();
    String connection = (String) session.getAttribute("conexion");
    if(usersContainer != null){
        for(User user : usersContainer.getUsers()) {
            usernames.add(user.getUsername());
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Registro de Usuario - RecipeBook</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/registro.css">
        <script>
            let usernames = [<%= usernames.stream().map(username -> "\"" + username + "\"").collect(Collectors.joining(",")) %>];
        </script>
        <script src="./js/registro.js"></script>
    </head>
    <body>
        <div class="container">
            <header>
                <h1>Registro de usuario</h1>
            </header>
            <form id="receta-form" action="RegistroServlet" method="POST">
                <p>conectado a la DB: <%= connection %></p>
                <div class="form-group">
                    <label for="username">Nombre de usuario:</label>
                    <input type="text" id="username" name="username" required onblur="verificarUsername()">
                </div>
                <div class="form-group">
                    <label for="password">Contraseña:</label>
                    <div class="password-container">
                        <input type="password" id="password" name="password" required onblur="verificarPassword()">
                        <i class="fa fa-eye password-toggle-icon" onclick="togglePasswordVisibility('password')"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label for="confPassword">Confirmar Contraseña:</label>
                    <div class="password-container">
                        <input type="password" id="confPassword" name="confPassword" required onblur="verificarPassword()">
                        <i class="fa fa-eye password-toggle-icon" onclick="togglePasswordVisibility('confPassword')"></i>
                    </div>
                </div>
                <script>
                    function togglePasswordVisibility(id) {
                        var passwordField = document.getElementById(id);
                        var toggleIcon = passwordField.nextElementSibling;
                        if (passwordField.type === "password") {
                            passwordField.type = "text";
                            toggleIcon.classList.remove("fa-eye");
                            toggleIcon.classList.add("fa-eye-slash");
                        } else {
                            passwordField.type = "password";
                            toggleIcon.classList.remove("fa-eye-slash");
                            toggleIcon.classList.add("fa-eye");
                        }
                    }
                </script>
                <style>
                    .password-container {
                        position: relative;
                        display: flex;
                        align-items: center;
                    }
                    .password-toggle-icon {
                        position: absolute;
                        right: 10px;
                        cursor: pointer;
                    }
                </style>
                <p id="mensaje"></p>
                <div class="button-group">
                    <button id="registrarse" type="submit">Registrarse</button>
                    <a href="index.html" class="cancel-button">Cancelar</a>
                </div>
            </form>
            <div class="login-link">
                <a href="login.jsp">¿Ya tienes una cuenta? Inicia sesión</a>
            </div>
        </div>
    </body>
</html>