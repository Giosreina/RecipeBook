<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.recipebook.logic.Receta" %>
<%@ page import="com.recipebook.logic.steps.*" %>
<%@ page import="com.recipebook.logic.User" %>
<%@ page import="java.util.List" %>
<%
    // Lee del REQUEST (puesto por VisorRecetaServlet con forward)
    Receta receta = (Receta) request.getAttribute("receta");
    List<Paso> pasos = (List<Paso>) request.getAttribute("pasos");
    User currentUser = (User) session.getAttribute("currentUser");

    if (receta == null) {
        response.sendRedirect("explorar.html");
        return;
    }
    if (pasos == null) {
        pasos = receta.getPasos();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Receta de <%= receta.getNombre() %></title>
    <link rel="stylesheet" href="css/navbar.css">
    <link rel="stylesheet" href="css/receta.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="index.html" class="navbar-brand">📚 RecipeBook</a>
            <ul class="navbar-nav">
                <li><a href="index.html">Inicio</a></li>
                <li><a href="explorar.jsp">Explorar Recetas</a></li>
            </ul>
            <div class="navbar-user-section">
                <% if (currentUser != null) { %>
                    <span class="navbar-user-info">Bienvenido, <%= currentUser.getUsername() %></span>
                    <div class="navbar-divider"></div>
                    <a href="PerfilServlet">Mi Perfil</a>
                    <a href="LogoutServlet" class="btn-logout">Cerrar Sesión</a>
                <% } else { %>
                    <a href="login.jsp" class="btn-login">Iniciar Sesión</a>
                    <a href="registro.jsp">Registrarse</a>
                <% } %>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="header">
            <h1 id="nombreReceta"><%= receta.getNombre() %></h1>
            <% if (currentUser != null) { %>
                <a href="PerfilServlet">Mi Perfil</a>
            <% } %>
        </div>

        <% if (receta.getImagen() != null && !receta.getImagen().isEmpty()) { %>
            <img src="<%= receta.getImagen() %>" alt="Imagen de la receta" class="receta-imagen"/>
        <% } %>

        <p><strong>Tipo:</strong> <%= receta.getTipo() != null ? receta.getTipo() : "Sin categoría" %></p>
        <p><strong>Descripción:</strong> <%= receta.getDescripcion() != null ? receta.getDescripcion() : "" %></p>

        <div class="valoracion">
            <p><strong>Valoración:</strong></p>
            <div class="estrellas" data-rating="<%= receta.getValoracion() %>">
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
            </div>
            <p class="valoracion-numero"><%= String.format("%.1f", receta.getValoracion()) %></p>
        </div>

        <div class="botones">
            <button type="button" onclick="window.location.href='explorar.html';">Volver a Explorar</button>
            <button type="button" onclick="mostrarValoracion();">Agregar Valoración</button>
        </div>

        <div id="valoracion-form" style="display:none;">
            <h3>Agregar Valoración</h3>
            <div class="estrellas-input">
                <i class="fas fa-star" data-value="1"></i>
                <i class="fas fa-star" data-value="2"></i>
                <i class="fas fa-star" data-value="3"></i>
                <i class="fas fa-star" data-value="4"></i>
                <i class="fas fa-star" data-value="5"></i>
            </div>
            <button type="button" onclick="enviarValoracion();">Enviar Valoración</button>
        </div>

        <h2>Ingredientes:</h2>
        <ul class="ingredientes">
            <% if (receta.getIngredientes() != null) {
                for (String ingrediente : receta.getIngredientes()) { %>
                    <li><%= ingrediente %></li>
            <%  }
            } %>
        </ul>

        <h2>Utensilios:</h2>
        <ul class="utensilios">
            <% if (receta.getUtensilios() != null) {
                for (String utensilio : receta.getUtensilios()) { %>
                    <li><%= utensilio %></li>
            <%  }
            } %>
        </ul>

        <h2>Pasos:</h2>
        <ol class="pasos">
            <% for (Paso paso : pasos) { %>
                <li class="paso">
                    <p><strong>Descripción:</strong> <%= paso.getDescripcion() %></p>
                    <p><strong>Tiempo:</strong> <%= paso.getTiempo() %> minutos</p>

                    <%-- Cast clásico compatible con todos los compiladores JSP --%>
                    <% if (paso instanceof PasoWextras) {
                        PasoWextras pasoExtra = (PasoWextras) paso; %>
                        <p><strong>Utensilios:</strong></p>
                        <ul>
                            <% for (String utensilio : pasoExtra.getUtensilios()) { %>
                                <li><%= utensilio %></li>
                            <% } %>
                        </ul>
                        <p><strong>Ingredientes:</strong></p>
                        <ul>
                            <% for (String ingrediente : pasoExtra.getIngredientes()) { %>
                                <li><%= ingrediente %></li>
                            <% } %>
                        </ul>
                    <% } %>

                    <% if (paso.getImagen() != null && !paso.getImagen().isEmpty()) { %>
                        <img src="<%= paso.getImagen() %>" alt="Imagen del paso" class="paso-imagen"/>
                    <% } %>
                </li>
            <% } %>
        </ol>
    </div>

    <script>
        function mostrarValoracion() {
            document.getElementById('valoracion-form').style.display = 'block';
        }

        function enviarValoracion() {
            alert('Valoración enviada');
            document.getElementById('valoracion-form').style.display = 'none';
        }

        document.addEventListener('DOMContentLoaded', function () {
            const estrellas = document.querySelector('.valoracion .estrellas');
            if (estrellas) {
                const rating = parseFloat(estrellas.dataset.rating) || 0;
                estrellas.querySelectorAll('i').forEach((estrella, index) => {
                    if (index < rating) estrella.classList.add('active');
                });
            }

            document.querySelectorAll('.estrellas-input i').forEach(estrella => {
                estrella.addEventListener('click', function () {
                    const value = this.dataset.value;
                    document.querySelectorAll('.estrellas-input i').forEach(s => {
                        s.classList.toggle('active', s.dataset.value <= value);
                    });
                });
            });
        });
    </script>
</body>
</html>
