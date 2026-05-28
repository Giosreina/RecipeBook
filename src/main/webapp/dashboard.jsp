<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.recipebook.dao.VistaDao" %>
<%@ page import="java.util.List" %>
<%--
    dashboard.jsp — Panel de control de vistas SQL
    Autor: Felipe
    Descripción: Muestra las 4 vistas de la base de datos
                 (recetas_mejor_valoradas, reporte_recetas,
                  usuario_receta, v_recetas_rapidas)
    Requiere: VistaDao en sesión (creado por IndexServlet)
--%>
<%
    // Obtener el VistaDao que ya fue creado por IndexServlet
    VistaDao vistaDao = (VistaDao) session.getAttribute("vistaDao");

    // Variables para los datos de las vistas
    List<VistaDao.RecetaMejorValorada> mejorValoradas = null;
    List<VistaDao.ReporteReceta> reporteRecetas = null;
    List<String> usuarios = null;
    List<VistaDao.RecetaRapida> recetasRapidas = null;

    boolean conectado = false;

    if (vistaDao != null) {
        conectado = true;
        mejorValoradas = vistaDao.obtenerRecetasMejorValoradas();
        reporteRecetas = vistaDao.obtenerReporteRecetas();
        usuarios       = vistaDao.obtenerUsernamesConRecetas();
        recetasRapidas = vistaDao.obtenerRecetasRapidas();
    }

    // Calcular totales para las tarjetas de resumen
    int totalRecetas = 0;
    if (reporteRecetas != null) {
        for (VistaDao.ReporteReceta rep : reporteRecetas) {
            totalRecetas += rep.totalRecetas;
        }
    }
    int totalUsuarios  = (usuarios != null) ? usuarios.size() : 0;
    int totalCategorias = (reporteRecetas != null) ? reporteRecetas.size() : 0;
    int totalRapidas    = (recetasRapidas != null) ? recetasRapidas.size() : 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Dashboard de vistas SQL del proyecto Global Recipe Book. Visualiza las recetas mejor valoradas, reportes por categoría, usuarios registrados y recetas rápidas.">
    <title>Dashboard de Vistas - Global Recipe Book</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
    <div class="container">

        <!-- ========== HEADER ========== -->
        <header>
            <h1>📊 Dashboard de Vistas</h1>
            <p class="subtitle">Resumen de datos desde las vistas de la base de datos</p>
            <nav>
                <a href="perfil.jsp"   class="nav-button">Mi Perfil</a>
                <a href="ExplorarServlet" class="nav-button">Explorar Recetas</a>
                <a href="index.html"   class="nav-button">Inicio</a>
            </nav>
        </header>

        <main>
            <% if (!conectado) { %>
                <div class="empty-state">
                    <p>⚠️ No se pudo conectar a la base de datos. Por favor, accede primero desde el
                       <a href="IndexServlet">inicio</a> para inicializar la conexión.</p>
                </div>
            <% } else { %>

            <!-- ========== TARJETAS RESUMEN ========== -->
            <h2 class="section-title">📋 Resumen General</h2>
            <div class="stats-row">
                <div class="stat-card">
                    <div class="stat-icon">📖</div>
                    <div class="stat-value"><%= totalRecetas %></div>
                    <div class="stat-label">Recetas Totales</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">👥</div>
                    <div class="stat-value"><%= totalUsuarios %></div>
                    <div class="stat-label">Usuarios Activos</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">🏷️</div>
                    <div class="stat-value"><%= totalCategorias %></div>
                    <div class="stat-label">Categorías</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">⚡</div>
                    <div class="stat-value"><%= totalRapidas %></div>
                    <div class="stat-label">Recetas Rápidas</div>
                </div>
            </div>

            <!-- ========== GRID DE VISTAS ========== -->
            <div class="dashboard-grid">

                <!-- ===== VISTA 1 — Recetas Mejor Valoradas ===== -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <span class="card-header-icon">⭐</span>
                        Vista 1 — Recetas Mejor Valoradas
                    </div>
                    <div class="card-body">
                        <% if (mejorValoradas != null && !mejorValoradas.isEmpty()) { %>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Receta</th>
                                    <th>Usuario</th>
                                    <th>Valoración</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% int pos = 1;
                                   for (VistaDao.RecetaMejorValorada r : mejorValoradas) { %>
                                <tr>
                                    <td><span class="badge badge-gold"><%= pos %></span></td>
                                    <td><strong><%= r.receta %></strong></td>
                                    <td><%= r.usuario %></td>
                                    <td>
                                        <span class="rating-stars">
                                            <% int estrellas = (int) Math.round(r.valoracionPromedio);
                                               for (int i = 0; i < estrellas; i++) { %>★<% }
                                               for (int i = estrellas; i < 5; i++) { %>☆<% } %>
                                        </span>
                                        (<%= String.format("%.1f", r.valoracionPromedio) %>)
                                    </td>
                                </tr>
                                <% pos++; } %>
                            </tbody>
                        </table>
                        <% } else { %>
                            <div class="empty-state">No hay datos de valoraciones disponibles.</div>
                        <% } %>
                    </div>
                </div>

                <!-- ===== VISTA 2 — Reporte de Recetas ===== -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <span class="card-header-icon">📊</span>
                        Vista 2 — Reporte por Categoría
                    </div>
                    <div class="card-body">
                        <% if (reporteRecetas != null && !reporteRecetas.isEmpty()) { %>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Tipo de Receta</th>
                                    <th>Total</th>
                                    <th>Promedio Valoración</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (VistaDao.ReporteReceta rep : reporteRecetas) { %>
                                <tr>
                                    <td><span class="badge badge-blue"><%= rep.tipoReceta %></span></td>
                                    <td><strong><%= rep.totalRecetas %></strong></td>
                                    <td>
                                        <span class="rating-stars">
                                            <% int estrellas2 = (int) Math.round(rep.promedioValoracion);
                                               for (int i = 0; i < estrellas2; i++) { %>★<% }
                                               for (int i = estrellas2; i < 5; i++) { %>☆<% } %>
                                        </span>
                                        (<%= String.format("%.1f", rep.promedioValoracion) %>)
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <% } else { %>
                            <div class="empty-state">No hay datos de reporte disponibles.</div>
                        <% } %>
                    </div>
                </div>

                <!-- ===== VISTA 3 — Usuarios (vista de seguridad) ===== -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <span class="card-header-icon">👥</span>
                        Vista 3 — Usuarios Registrados
                    </div>
                    <div class="card-body">
                        <% if (usuarios != null && !usuarios.isEmpty()) { %>
                        <div class="user-chips">
                            <% for (String username : usuarios) { %>
                            <div class="user-chip">
                                <span class="user-chip-icon">👤</span>
                                <%= username %>
                            </div>
                            <% } %>
                        </div>
                        <% } else { %>
                            <div class="empty-state">No hay usuarios registrados.</div>
                        <% } %>
                    </div>
                </div>

                <!-- ===== VISTA 4 — Recetas Rápidas ===== -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <span class="card-header-icon">⚡</span>
                        Vista 4 — Recetas Rápidas (≤ 30 min)
                    </div>
                    <div class="card-body">
                        <% if (recetasRapidas != null && !recetasRapidas.isEmpty()) { %>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Receta</th>
                                    <th>Tiempo</th>
                                    <th>Descripción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (VistaDao.RecetaRapida r : recetasRapidas) { %>
                                <tr>
                                    <td><strong><%= r.receta %></strong></td>
                                    <td><span class="badge badge-green">🕐 <%= r.minutos %> min</span></td>
                                    <td><%= (r.descripcion != null && r.descripcion.length() > 80)
                                            ? r.descripcion.substring(0, 80) + "…"
                                            : (r.descripcion != null ? r.descripcion : "—") %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <% } else { %>
                            <div class="empty-state">No hay recetas rápidas disponibles.</div>
                        <% } %>
                    </div>
                </div>

            </div><!-- /dashboard-grid -->

            <% } %>
        </main>

        <footer>
            <p>&copy; 2025 Global Recipe Book. Todos los derechos reservados.</p>
        </footer>

    </div>
</body>
</html>
