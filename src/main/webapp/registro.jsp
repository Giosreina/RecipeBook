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
        <link rel="stylesheet" href="css/navbar.css">
        <link rel="stylesheet" href="css/registro.css">
        <script>
            let usernames = [<%= usernames.stream().map(username -> "\"" + username + "\"").collect(Collectors.joining(",")) %>];
        </script>
        <script src="./js/registro.js"></script>
    </head>
    <body>
        <nav class="navbar">
            <div class="navbar-container">
                <a href="index.html" class="navbar-brand">📚 RecipeBook</a>
                <ul class="navbar-nav">
                    <li><a href="index.html">Inicio</a></li>
                    <li><a href="explorar.html">Explorar Recetas</a></li>
                </ul>
                <div class="navbar-user-section">
                    <a href="login.jsp" class="btn-login">Iniciar Sesión</a>
                    <a href="registro.jsp" class="active">Registrarse</a>
                </div>
            </div>
        </nav>
        <main>
            <div class="container">
            <header>
                <h1>Registro de usuario</h1>
            </header>
            <form id="receta-form" method="POST" action="RegistroServlet">
                <div class="form-group">
                    <label for="nombre_1">Primer Nombre: <span class="required">*</span></label>
                    <input type="text" id="nombre_1" name="nombre_1" required placeholder="Ej: Juan">
                    <small class="help-text">Solo letras, máximo 20 caracteres</small>
                </div>
                <div class="form-group">
                    <label for="nombre_2">Segundo Nombre:</label>
                    <input type="text" id="nombre_2" name="nombre_2" placeholder="Ej: Carlos">
                    <small class="help-text">Opcional - Solo letras, máximo 20 caracteres</small>
                </div>
                <div class="form-group">
                    <label for="apellido_1">Primer Apellido: <span class="required">*</span></label>
                    <input type="text" id="apellido_1" name="apellido_1" required placeholder="Ej: García">
                    <small class="help-text">Solo letras, máximo 20 caracteres</small>
                </div>
                <div class="form-group">
                    <label for="apellido_2">Segundo Apellido:</label>
                    <input type="text" id="apellido_2" name="apellido_2" placeholder="Ej: López">
                    <small class="help-text">Opcional - Solo letras, máximo 20 caracteres</small>
                </div>
                <div class="form-group">
                    <label for="correo">Correo Electrónico: <span class="required">*</span></label>
                    <input type="email" id="correo" name="correo" required placeholder="Ej: usuario@gmail.com" onblur="verificarEmail()">
                    <small class="help-text">Debe ser un correo válido (ej: usuario@gmail.com, usuario@outlook.com)</small>
                </div>
                <div class="form-group">
                    <label for="username">Nombre de usuario: <span class="required">*</span></label>
                    <input type="text" id="username" name="username" required placeholder="Ej: juangarcia" onblur="verificarUsername()">
                    <small class="help-text">Sin espacios, mínimo 3 caracteres</small>
                </div>
                <div class="form-group">
                    <label for="password">Contraseña: <span class="required">*</span></label>
                    <div class="password-container">
                        <input type="password" id="password" name="password" required placeholder="Mínimo 6 caracteres" onblur="verificarPassword()">
                        <i class="fa fa-eye password-toggle-icon" onclick="togglePasswordVisibility('password')"></i>
                    </div>
                    <small class="help-text">Mínimo 6 caracteres, incluye mayúsculas, minúsculas y números si es posible</small>
                </div>
                <div class="form-group">
                    <label for="confPassword">Confirmar Contraseña: <span class="required">*</span></label>
                    <div class="password-container">
                        <input type="password" id="confPassword" name="confPassword" required placeholder="Repite tu contraseña" onblur="verificarPassword()">
                        <i class="fa fa-eye password-toggle-icon" onclick="togglePasswordVisibility('confPassword')"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label for="urlMultimedia">URL de Imagen de Perfil:</label>
                    <input type="text" id="urlMultimedia" name="urlMultimedia" placeholder="Ej: https://ejemplo.com/imagen.jpg">
                    <small class="help-text">Opcional - URL de una imagen (ej: https://ejemplo.com/imagen.jpg)</small>
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
                    .required {
                        color: red;
                    }
                    .help-text {
                        display: block;
                        font-size: 0.85em;
                        color: #666;
                        margin-top: 4px;
                    }
                    .success {
                        background-color: #d4edda;
                        border: 1px solid #c3e6cb;
                        color: #155724;
                    }
                    .error {
                        background-color: #f8d7da;
                        border: 1px solid #f5c6cb;
                        color: #721c24;
                    }
                    #mensaje {
                        padding: 12px;
                        border-radius: 4px;
                        margin-bottom: 15px;
                        display: none;
                    }
                    #mensaje.show {
                        display: block;
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
        </main>
    </body>
</html>