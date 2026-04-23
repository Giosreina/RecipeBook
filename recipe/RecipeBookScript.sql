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
    CONSTRAINT fk_utensilio_receta
    FOREIGN KEY (id_paso) REFERENCES PASOS(id_paso)
);
CREATE TABLE IF NOT EXISTS VALORACION(
    id_comentario SERIAL PRIMARY KEY,
    id_receta INT NOT NULL,
    id_usuario INT NOT NULL,
    comentario TEXT ,
    fecha_comentario TIMESTAMP DEFAULT CURRENT_DATE,
    valor INT, 
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
