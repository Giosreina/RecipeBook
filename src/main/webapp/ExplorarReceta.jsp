<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.recipebook.logic.*" %>
<%@ page import="com.recipebook.dao.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
    User currentUser = (User) session.getAttribute("currentUser");
    String categoria = request.getParameter("categoria");
    List<Receta> recetasCategoria = new ArrayList<>();

    // Conexión fresca por request — nunca de sesión
    SQLController sql = new SQLController(
        "jdbc:postgresql://localhost:5432/recipebook", "giosreina", "Kabuto43*"
    );
    try {
        if (sql.isConnected() && categoria != null) {
            RecetaDao recetaDao = new RecetaDao(sql);
            List<Receta> todas = recetaDao.obtenerRecetas();

            for (Receta receta : todas) {
                String tipo = receta.getTipo();
                if (tipo != null && tipo.equalsIgnoreCase(categoria)) {
                    recetasCategoria.add(receta);
                }
            }
        }
    } finally {
        sql.closeConnection(); // siempre se cierra
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Recetas por Categoría</title>
        <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/navbar.css">
        <link rel="stylesheet" href="css/explorarR.css">
    </head>
    <body>
        <nav class="navbar">
            <div class="navbar-container">
                <a href="index.html" class="navbar-brand">📚 RecipeBook</a>
                <ul class="navbar-nav">
                    <li><a href="index.html">Inicio</a></li>
                    <li><a href="explorar.html" class="active">Explorar Recetas</a></li>
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
            <header>
                <h1>Recetas de <%= (categoria != null) ? categoria : "Categoría Desconocida" %></h1>
                <p class="subtitle">Descubre las mejores recetas de <%= (categoria != null) ? categoria : "" %></p>
            </header>

            <div class="recipe-grid">
                <% if (!recetasCategoria.isEmpty()) {
                    for (Receta receta : recetasCategoria) { %>
                        <%-- Se pasa el ID real de la BD, no el índice de la lista --%>
                        <a href="VisorRecetaServlet?id=<%= receta.getId() %>" class="recipe-card">
                            <div class="recipe-info">
                                <h3><%= receta.getNombre() %></h3>
                                <p><%= receta.getDescripcion() != null ? receta.getDescripcion() : "" %></p>
                                <span class="recipe-type"><%= receta.getTipo() %></span>
                            </div>
                            <% if (receta.getImagen() != null && !receta.getImagen().isEmpty()) { %>
                                <div class="recipe-image" style="background-image: url('<%= receta.getImagen() %>');"></div>
                            <% } %>
                        </a>
                <%  }
                } else { %>
                    <p>No hay recetas disponibles en esta categoría.</p>
                <% } %>
            </div>
        </div>
    </body>
</html>
