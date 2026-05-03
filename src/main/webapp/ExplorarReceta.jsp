<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.recipebook.logic.*" %>
<%@ page import="com.recipebook.dao.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
    // 1. Recuperar el DAO de la sesión con validación
    UserDao userDao = (UserDao) session.getAttribute("userDao");
    List<Receta> recetasCategoria = new ArrayList<>();
    String categoria = request.getParameter("categoria");

    if (userDao != null && categoria != null) {
        RecetaDao recetaDao = userDao.getRecetaDAO();
        RecetasContainer recetaContainerDB = new RecetasContainer(recetaDao.obtenerRecetas());
        RecetasContainer recetasCategoriaRC = new RecetasContainer();

        for (Receta receta : recetaContainerDB.getRecetas()) {
            // CORRECCIÓN: Validamos que el tipo no sea null antes de usar toString()
            Object tipoRaw = receta.getTipo();
            if (tipoRaw != null) {
                String tipoStr = tipoRaw.toString();
                if (tipoStr.equalsIgnoreCase(categoria)) {
                    recetasCategoriaRC.addReceta(receta);
                }
            }
        }
        recetasCategoria = recetasCategoriaRC.getRecetas();
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Recetas por Categoría</title>
        <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/explorarR.css">
    </head>
    <body>
        <div class="container">
            <header>
                <h1>Recetas de <%= (categoria != null) ? categoria : "Categoría Desconocida" %></h1>
                <p class="subtitle">Descubre las mejores recetas de <%= (categoria != null) ? categoria : "" %></p>
            </header>
        
            <div class="recipe-grid">
                <% if (recetasCategoria != null && !recetasCategoria.isEmpty()) {
                        // Guardamos la lista filtrada en sesión para el visor
                        session.setAttribute("listRecetas", recetasCategoria);
                        for (int i = 0; i < recetasCategoria.size(); i++) {
                            Receta receta = recetasCategoria.get(i);
                %>
                <a href="VisorRecetaServlet?id=<%= i %>" class="recipe-card">
                    <div class="recipe-info">
                        <h3><%= receta.getNombre() %></h3>
                        <p><%= receta.getDescripcion() %></p>
                        <span class="recipe-type"><%= receta.getTipo() %></span>
                    </div>
                    <div class="recipe-image" style="background-image: url('<%= receta.getImagen() %>');"></div>
                </a>
                <%      }
                    } else { %>
                    <p>No hay recetas disponibles en esta categoría o no se pudo cargar la base de datos.</p>
                <% } %>
            </div>
        </div>
    </body>
</html>

