<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.recipebook.dao.VistaDao" %>
<%@ page import="java.util.List" %>
<%
    @SuppressWarnings("unchecked")
    List<VistaDao.ReporteReceta> reporteTipos =
        (List<VistaDao.ReporteReceta>) request.getAttribute("reporteTipos");

    @SuppressWarnings("unchecked")
    List<VistaDao.RecetaMejorValorada> top5 =
        (List<VistaDao.RecetaMejorValorada>) request.getAttribute("top5Recetas");

    @SuppressWarnings("unchecked")
    List<VistaDao.RecetaRapida> recetasRapidas =
        (List<VistaDao.RecetaRapida>) request.getAttribute("recetasRapidas");
%>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Explora Nuestras Recetas - Global Recipe Book</title>
        <link
            href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&display=swap"
            rel="stylesheet">
        <link rel="stylesheet" href="css/explorar.css">
    </head>
    <body>
        <div class="container">
            <header>
                <h1>Explora Nuestras Recetas</h1>
                <p class="subtitle">Descubre sabores del mundo en cada
                    categoría</p>
            </header>
            <hr>
            <div class="categories">
                <a href="ExplorarReceta.jsp?categoria=Desayuno"
                    class="category">
                    <div class="category-icon">🍳</div>
                    <div class="category-name">Desayuno</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Desayuno"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
                <a href="ExplorarReceta.jsp?categoria=ALMUERZO"
                    class="category">
                    <div class="category-icon">🍝</div>
                    <div class="category-name">Almuerzo</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Almuerzo"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
                <a href="ExplorarReceta.jsp?categoria=cena"
                    class="category">
                    <div class="category-icon">🍽️</div>
                    <div class="category-name">Cena</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Cena"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
                <a href="ExplorarReceta.jsp?categoria=postre"
                    class="category">
                    <div class="category-icon">🍰</div>
                    <div class="category-name">Postre</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Postre"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
                <a href="ExplorarReceta.jsp?categoria=snack"
                    class="category">
                    <div class="category-icon">🥨</div>
                    <div class="category-name">Snack</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Snack"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
                <a href="ExplorarReceta.jsp?categoria=bebida" class="category">
                    <div class="category-icon">🍹</div>
                    <div class="category-name">Bebida</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Bebida"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
                <a href="ExplorarReceta.jsp?categoria=ensalada"
                    class="category">
                    <div class="category-icon">🥗</div>
                    <div class="category-name">Ensalada</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Ensalada"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
                <a href="ExplorarReceta.jsp?categoria=guarnicion"
                    class="category">
                    <div class="category-icon">🥔</div>
                    <div class="category-name">Guarnición</div>
                    <% if (reporteTipos != null) { for (VistaDao.ReporteReceta rep : reporteTipos)
                    { if (rep.tipoReceta.equalsIgnoreCase("Guarnición"))
                    { %><small><%= rep.totalRecetas %> recetas</small><% break; }}} %>
                </a>
            </div>
            <% if (top5 != null && !top5.isEmpty()) { %>
        <section>
            <h2>⭐ Top 5 Mejor Valoradas</h2>
            <% for (VistaDao.RecetaMejorValorada r : top5) { %>
                <p><b><%= r.receta %></b> — por <%= r.usuario %> — ⭐ <%= String.format("%.1f", r.valoracionPromedio) %></p>
            <% } %>
        </section>
        <% } %>

        <%-- NUEVO: Recetas rápidas --%>
        <% if (recetasRapidas != null && !recetasRapidas.isEmpty()) { %>
        <section>
            <h2>⚡ Listas en 30 min o menos</h2>
            <% for (VistaDao.RecetaRapida r : recetasRapidas) { %>
                <p><%= r.receta %> — 🕐 <%= r.minutos %> min</p>
            <% } %>
        </section>
        <% } %>
        </div>
    </body>
</html>