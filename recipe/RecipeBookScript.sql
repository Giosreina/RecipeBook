CREATE TABLE IF NOT EXISTS TIPO_RECETA(
    id_tipo_receta SERIAL PRIMARY KEY,
    nombre_tipo VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT NULL
);
CREATE TABLE IF NOT EXISTS MULTIMEDIA(
    id_multimedia SERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS ROL(
    id_rol SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS MODULO(
    id_modulo SERIAL PRIMARY KEY,
nombre_modulo VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS OPERACION(
    id_operacion SERIAL PRIMARY KEY,
    accion VARCHAR(100) NOT NULL UNIQUE,
    id_modulo INT NOT NULL,
    CONSTRAINT fk_operacion_modulo
    FOREIGN KEY (id_modulo) REFERENCES MODULO(id_modulo)
);
CREATE TABLE IF NOT EXISTS ROL_OPERACION(
    id_rol INT NOT NULL,
    id_operacion INT NOT NULL,
    PRIMARY KEY (id_rol, id_operacion),
    FOREIGN KEY (id_rol) REFERENCES ROL(id_rol),
    FOREIGN KEY (id_operacion) REFERENCES OPERACION(id_operacion)
);
CREATE TABLE IF NOT EXISTS USUARIO(
    id_usuario SERIAL PRIMARY KEY,
    nombre_1 VARCHAR(20) NOT NULL,
    nombre_2 VARCHAR(20) NULL,
    apellido_1 VARCHAR(20) NOT NULL,
    apellido_2 VARCHAR(20) NULL,
    correo_electronico VARCHAR(70) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    id_multimedia INT NULL,
    id_rol INT NOT NULL,
    CONSTRAINT fk_multimedia_usuario
    FOREIGN KEY (id_multimedia) REFERENCES MULTIMEDIA(id_multimedia),
    CONSTRAINT fk_rol_usuario
    FOREIGN KEY (id_rol) REFERENCES ROL(id_rol)
);
CREATE TABLE IF NOT EXISTS RECETAS(
    id_receta SERIAL PRIMARY KEY,
    nombre_receta VARCHAR(100) NOT NULL,
    descripcion TEXT NULL,
    tiempo_preparacion INT NULL,
    id_usuario INT NOT NULL,
    id_imagen INT NULL,
    CONSTRAINT fk_multimedia_receta
    FOREIGN KEY (id_imagen) REFERENCES MULTIMEDIA(id_multimedia),
    CONSTRAINT fk_usuario_receta
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);
CREATE TABLE IF NOT EXISTS PASOS(
    id_paso SERIAL PRIMARY KEY,
    id_receta INT NOT NULL,
    descripcion TEXT NOT NULL,
    id_multimedia INT NULL,
    CONSTRAINT fk_multimedia_paso
    FOREIGN KEY (id_multimedia) REFERENCES MULTIMEDIA(id_multimedia),
    CONSTRAINT fk_receta_paso
    FOREIGN KEY (id_receta) REFERENCES RECETAS(id_receta)
);
CREATE TABLE IF NOT EXISTS INGREDIENTES(
    id_ingrediente SERIAL PRIMARY KEY,    
    id_receta INT NOT NULL,
    id_paso INT NULL,
    nombre_ingrediente VARCHAR(255) NOT NULL,
    CONSTRAINT fk_ingrediente_receta
    FOREIGN KEY (id_receta) REFERENCES RECETAS(id_receta),
    CONSTRAINT fk_ingrediente_paso
    FOREIGN KEY (id_paso) REFERENCES PASOS(id_paso)
);
CREATE TABLE IF NOT EXISTS UTENSILIOS(
    id_utensilio SERIAL PRIMARY KEY,
    id_receta INT NOT NULL,
    id_paso INT NOT NULL,
    nombre_utensilio VARCHAR(255) NOT NULL,
    CONSTRAINT fk_utensilio_receta
    FOREIGN KEY (id_receta) REFERENCES RECETAS(id_receta),
    CONSTRAINT fk_utensilio_paso
    FOREIGN KEY (id_paso) REFERENCES PASOS(id_paso)
);
CREATE TABLE IF NOT EXISTS VALORACION(
    id_valoracion SERIAL PRIMARY KEY,
    id_receta INT NOT NULL,
    id_usuario INT NOT NULL DEFAULT 1,
    comentario TEXT ,
    fecha_comentario TIMESTAMP DEFAULT CURRENT_DATE,
    valor INT  
    CONSTRAINT fec_limite CHECK (fecha_comentario <= CURRENT_DATE),
    CONSTRAINT val_limite CHECK (valor >= 1 AND valor <= 5),
    CONSTRAINT fk_valoracion_receta
    FOREIGN KEY (id_receta) REFERENCES RECETAS(id_receta),
    CONSTRAINT fk_valoracion_usuario
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);
CREATE TABLE IF NOT EXISTS CLASIFICACION(
    id_receta INT NOT NULL,
    id_tipo_receta INT NOT NULL,
    PRIMARY KEY (id_receta, id_tipo_receta),
    FOREIGN KEY (id_receta) REFERENCES RECETAS(id_receta),
    FOREIGN KEY (id_tipo_receta) REFERENCES TIPO_RECETA(id_tipo_receta)
);

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