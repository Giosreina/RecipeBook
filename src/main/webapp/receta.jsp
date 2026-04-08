<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.recipebook.logic.Receta" %>
<%@ page import="com.recipebook.logic.steps.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%
    Receta receta = (Receta) session.getAttribute("receta");
    List<Paso> pasos = (List<Paso>) session.getAttribute("pasos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Receta de ${receta.getNombre()}</title>
    <link rel="stylesheet" href="./css/receta.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1 id="nombreReceta">${receta.getNombre()}</h1>
            <% if (session.getAttribute("currentUser") != null) { %>
                <h1 id="perfil"><a href="perfil.jsp">Perfil</a></h1>
            <% } %>
        </div>

        <img src="${receta.getImagen()}" alt="Imagen de la receta" class="receta-imagen"/>
        <p><strong>Tipo:</strong> ${receta.getTipo()}</p>

        
        <div class="valoracion">
            <p><strong>Valoración:</strong></p>
            <div class="estrellas" data-rating="${receta.getValor()}">
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
            </div>
            <p class="valoracion-numero">${String.format("%.1f", receta.getValor())}</p>
        </div>
        
        <div class="botones">
            <button type="button" onclick="window.location.href='./explorar.html';">Volver a Explorar</button>
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
        
        <p><strong>Descripción:</strong> ${receta.getDescripcion()}</p>
        
        <h2>Ingredientes:</h2>
        <ul class="ingredientes">
            <% for (String ingrediente : receta.getIngredientes()) { %>
                <li><%= ingrediente %></li>
            <% } %>
        </ul>
        
        <h2>Utensilios:</h2>
        <ul class="utensilios">
            <% for (String utensilio : receta.getUtensilios()) { %>
                <li><%= utensilio %></li>
            <% } %>
        </ul>
        
        <h2>Pasos:</h2>
        <ol class="pasos">
            <% for (Paso paso : pasos) { %>
                <li class="paso">
                    <p><strong>Descripción:</strong> <%= paso.getDescripcion() %></p>
                    <p><strong>Tiempo:</strong> <%= paso.getTiempo() %> minutos</p>
                    <% if (paso instanceof PasoWextras pasoExtra) { %>
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
                    <% if (paso.getImagen() != null) { %>
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
            // Aquí iría la lógica para enviar la valoración al servidor
            receta.addValoracion(document.getElementById('estrellas-input').dataset.value);
            alert('Valoración enviada');
            document.getElementById('valoracion-form').style.display = 'none';
        }
        
        // Colorear las estrellas según la valoración
        document.addEventListener('DOMContentLoaded', function() {
            const estrellas = document.querySelector('.valoracion .estrellas');
            const rating = parseFloat(estrellas.dataset.rating);
            const estrellasItems = estrellas.querySelectorAll('i');
            estrellasItems.forEach((estrella, index) => {
                if (index < rating) {
                    estrella.classList.add('active');
                }
            });
        });
        
        // Manejar la selección de estrellas en el formulario de valoración
        const estrellasInput = document.querySelectorAll('.estrellas-input i');
        estrellasInput.forEach(estrella => {
            estrella.addEventListener('click', function() {
                const value = this.dataset.value;
                estrellasInput.forEach(s => {
                    s.classList.toggle('active', s.dataset.value <= value);
                });
            });
        });
    </script>
</body>
</html>