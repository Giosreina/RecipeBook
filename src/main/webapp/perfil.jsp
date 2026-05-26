<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.recipebook.logic.User" %>
<%@ page import="com.recipebook.logic.Receta" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Las recetas las carga PerfilServlet y las pone en el request
    @SuppressWarnings("unchecked")
    List<Receta> userRecetas = (List<Receta>) request.getAttribute("userRecetas");

    Map<String, String> typeIcons = new HashMap<>();
    typeIcons.put("DESAYUNO",   "🍳");
    typeIcons.put("ALMUERZO",   "🍝");
    typeIcons.put("CENA",       "🍽️");
    typeIcons.put("POSTRE",     "🍰");
    typeIcons.put("SNACK",      "🥨");
    typeIcons.put("BEBIDA",     "🍹");
    typeIcons.put("ENSALADA",   "🥗");
    typeIcons.put("GUARNICION", "🥔");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de Usuario - Global Recipe Book</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/navbar.css">
    <link rel="stylesheet" href="css/perfil.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="index.html" class="navbar-brand">📚 RecipeBook</a>
            <ul class="navbar-nav">
                <li><a href="index.html">Inicio</a></li>
                <li><a href="explorar.html">Explorar Recetas</a></li>
                <li><a href="subir.html">Subir Receta</a></li>
            </ul>
            <div class="navbar-user-section">
                <span class="navbar-user-info">Bienvenido, <%= currentUser.getUsername() %></span>
                <div class="navbar-divider"></div>
                <a href="PerfilServlet" class="active">Mi Perfil</a>
                <a href="LogoutServlet" class="btn-logout">Cerrar Sesión</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <header>
            <h1>Bienvenido, <%= currentUser.getUsername() %>!</h1>
            <nav>
                <a href="explorar.html" class="nav-button">Explorar Recetas</a>
            </nav>
        </header>
        <main>
            <section class="profile-info">
                <h2>Información de Usuario</h2>
                <p><strong>Nombre de usuario:</strong> <%= currentUser.getUsername() %></p>
            </section>

            <section class="user-recipes">
                <h2>Mis Recetas</h2>
                <div class="recipe-grid">
                    <% if (userRecetas != null && !userRecetas.isEmpty()) {
                        for (Receta receta : userRecetas) {
                            String iconoTipo = typeIcons.getOrDefault(
                                receta.getTipo() != null ? receta.getTipo().toUpperCase() : "", "🍽️"
                            );
                    %>
                            <%-- Usa el ID real de BD, no el índice de la lista --%>
                            <a href="VisorRecetaServlet?id=<%= receta.getId() %>" class="recipe-card">
                                <div class="recipe-image"
                                     style="background-image: url('<%= receta.getImagen() != null ? receta.getImagen() : "" %>'); background-color: #f5f5f5;"
                                     data-icon="<%= iconoTipo %>">
                                </div>
                                <div class="recipe-info">
                                    <h3><%= receta.getNombre() %></h3>
                                    <p><%= receta.getDescripcion() != null ? receta.getDescripcion() : "" %></p>
                                    <span class="recipe-type">
                                        <%= iconoTipo %> <%= receta.getTipo() != null ? receta.getTipo() : "" %>
                                    </span>
                                </div>
                            </a>
                    <%  }
                    } else { %>
                        <p>Aún no has subido ninguna receta.</p>
                    <% } %>
                </div>
            </section>

            <div class="add-recipe">
                <a href="subir.html" class="button">Agregar Nueva Receta</a>
            </div>
        </main>
        <footer>
            <p>&copy; 2025 Global Recipe Book. Todos los derechos reservados.</p>
        </footer>
    </div>
</body>
</html>

