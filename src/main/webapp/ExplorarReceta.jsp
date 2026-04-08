<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.recipebook.logic.*" %>
<%@ page import="com.recipebook.dao.*" %>
<%@ page import="java.util.List" %>
<%
    UserDao userDao = (UserDao) session.getAttribute("userDao");
    RecetaDao recetaDao = userDao.getRecetaDAO();
    RecetasContainer recetaContainerDB = recetaDao.obtenerRecetas();
    RecetasContainer recetasCategoriaRC = new RecetasContainer();
    for (Receta receta : recetaContainerDB.getRecetas()) {
        if (((receta.getTipo()).toString()).equalsIgnoreCase(request.getParameter("categoria"))) {
            recetasCategoriaRC.addReceta(receta);
        }
    }
    List<Receta> recetasCategoria = recetasCategoriaRC.getRecetas();
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
        <% String categoria = (String) request.getParameter("categoria");%>
        <div class="container">
            <header>
                <h1>Recetas de <%= categoria%></h1>
                <p class="subtitle">Descubre las mejores recetas de <%= categoria%></p>
            </header>
        
            <div class="recipe-grid">
                <% if (recetasCategoria != null && !recetasCategoria.isEmpty()) {
                        session.setAttribute("listRecetas", recetasCategoria);
                        for (Receta receta : recetasCategoria) {
                            int id = recetasCategoria.indexOf(receta);
                %>
                <a href="VisorRecetaServlet?id=<%= id%>" class="recipe-card">
                    <div class="recipe-info">
                        <h3><%= receta.getNombre()%></h3>
                        <p><%= receta.getDescripcion()%></p>
                        <span class="recipe-type"><%= receta.getTipo()%></span>
                    </div>
                    <div class="recipe-image" style="background-image: url('<%= receta.getImagen()%>');"></div>
                </a>
                <% }
                    } else { %>
                <p>No hay recetas disponibles en esta categoría.</p>
                <% }%>
            </div>
        </div>
    </body>
</html>

