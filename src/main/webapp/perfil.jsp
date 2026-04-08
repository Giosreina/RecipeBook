<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.recipebook.logic.User" %>
<%@ page import="com.recipebook.logic.Receta" %>
<%@ page import="com.recipebook.logic.RecetasContainer" %>
<%@ page import="com.recipebook.dao.UserDao" %>
<%@ page import="com.recipebook.dao.RecetaDao" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
    UserDao userDao = (UserDao) session.getAttribute("userDao");
    RecetaDao recetaDao = userDao.getRecetaDAO();
    RecetasContainer recetaContainer = recetaDao.obtenerRecetasPorUsuario(userDao.obtenerUserID(currentUser.getUsername()));
    List<Receta> userRecetas = recetaContainer.getRecetas();   

    Map<String, String> typeIcons = new HashMap<>();
    typeIcons.put("DESAYUNO", "🍳");
    typeIcons.put("ALMUERZO", "🍝");
    typeIcons.put("CENA", "🍽️");
    typeIcons.put("POSTRE", "🍰");
    typeIcons.put("SNACK", "🥨");
    typeIcons.put("BEBIDA", "🍹");
    typeIcons.put("ENSALADA", "🥗");
    typeIcons.put("GUARNICION", "🥔");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de Usuario - Global Recipe Book</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/perfil.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Bienvenido, <%= currentUser.getUsername() %>!</h1>
            <nav>
                <a href="explorar.html" class="nav-button">Explorar Recetas</a>
                <a href="index.html" class="nav-button">Cerrar Sesión</a>
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
                        session.setAttribute("listRecetas", userRecetas);
                        for (Receta receta : userRecetas) { 
                            String iconoTipo = typeIcons.getOrDefault(receta.getTipo(), "🍽️");
                            int id = userRecetas.indexOf(receta);
                    %>
                            <a href="VisorRecetaServlet?id=<%= id %>" class="recipe-card">
                                <div class="recipe-image" style="background-image: url('<%= receta.getImagen() %>'); background-color: #f5f5f5;" data-icon="<%= iconoTipo %>" onerror="this.innerHTML='<%= iconoTipo %>'; this.style.fontSize='48px'; this.style.display='flex'; this.style.alignItems='center'; this.style.justifyContent='center';"></div>
                                <div class="recipe-info">
                                    <h3><%= receta.getNombre() %></h3>
                                    <p><%= receta.getDescripcion() %></p>
                                    <span class="recipe-type"><%= receta.getTipo() %></span>
                                </div>
                            </a>
                    <% }
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

