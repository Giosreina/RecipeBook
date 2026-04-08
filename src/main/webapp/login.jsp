<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.recipebook.logic.UsersContainer" %>
<%@ page import="com.recipebook.dao.UserDao" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Inicio de Sesión - RecipeBook</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/login.css">
    </head>
    <body>
        <div class="container">
            <header>
                <h1>Inicio de Sesión</h1>
            </header>
            <form id="login-form" action="LoginServlet" method="POST">
                <div class="form-group">
                    <label for="username">Nombre de usuario:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <% if (request.getAttribute("mensaje") != null) { %>
                    <p id="mensaje" class="<%= request.getAttribute("mensajeClase") %>">
                        <%= request.getAttribute("mensaje") %>
                    </p>
                <% } %>
                <div class="button-group">
                    <button type="submit">Iniciar Sesión</button>
                    <a href="index.html" class="cancel-button">Cancelar</a>
                </div>
            </form>
            <div class="login-link">
                <a href="registro.jsp">¿No tienes una cuenta? Regístrate</a>
            </div>
        </div>
    </body>
</html>

