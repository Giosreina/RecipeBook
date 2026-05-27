-- VISTA 1: recetas_mejor_valoradas  (Simplificación)
-- Top 5 recetas con mayor promedio de valoración.
-- Consolida el JOIN más frecuente: RECETAS + VALORACION + USUARIO
CREATE OR REPLACE VIEW recetas_mejor_valoradas AS
SELECT
    u.username            AS usuario,
    r.nombre_receta       AS receta,
    AVG(v.valor)          AS valoracion_promedio
FROM recetas r
JOIN valoracion v ON r.id_receta  = v.id_receta
JOIN usuario   u ON r.id_usuario  = u.id_usuario
GROUP BY r.id_receta, u.id_usuario
ORDER BY valoracion_promedio DESC
LIMIT 5;
 
-- VISTA 2: reporte_recetas  (Reporte)
-- Promedio de calificación y número de recetas por tipo.
CREATE OR REPLACE VIEW reporte_recetas AS
SELECT
    tip.nombre_tipo       AS tipo_receta,
    AVG(v.valor)          AS promedio_valoracion,
    COUNT(r.id_receta)    AS total_recetas
FROM tipo_receta   tip
JOIN clasificacion c   ON tip.id_tipo_receta = c.id_tipo_receta
JOIN recetas       r   ON r.id_receta        = c.id_receta
JOIN valoracion    v   ON v.id_receta        = r.id_receta
GROUP BY tip.id_tipo_receta
ORDER BY total_recetas DESC;

-- VISTA 3: usuario_receta  (Seguridad)
-- Solo expone el username; oculta contraseña, correo, etc.
-- Útil para saber qué usuarios tienen al menos una receta
-- sin exponer datos sensibles.

CREATE OR REPLACE VIEW usuario_receta AS
SELECT
    u.username
FROM usuario u
LEFT JOIN recetas r ON u.id_usuario = r.id_usuario;
 

-- VISTA 4: v_recetas_rapidas  (Actualizable + WITH CHECK OPTION)
-- Solo recetas con tiempo_preparacion <= 30 minutos.
-- WITH CHECK OPTION impide insertar/actualizar filas que
-- queden fuera de la condición.
CREATE OR REPLACE VIEW v_recetas_rapidas AS
SELECT
    nombre_receta         AS receta,
    tiempo_preparacion    AS minutos,
    descripcion
FROM recetas
WHERE tiempo_preparacion <= 30
WITH CHECK OPTION;