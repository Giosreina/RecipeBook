--
-- PostgreSQL database dump
--

\restrict GVuwQgdNC5DsdWMyKHZ46d8u9klLjFMeR9nktj3CORjdn9w1M7LRIriNUUCpCFl

-- Dumped from database version 16.13 (Ubuntu 16.13-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.13 (Ubuntu 16.13-0ubuntu0.24.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: clasificacion; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.clasificacion (
    id_receta integer NOT NULL,
    id_tipo_receta integer NOT NULL
);


ALTER TABLE public.clasificacion OWNER TO giosreina;

--
-- Name: ingredientes; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.ingredientes (
    id_ingrediente integer NOT NULL,
    id_receta integer NOT NULL,
    id_paso integer,
    nombre_ingrediente character varying(255) NOT NULL
);


ALTER TABLE public.ingredientes OWNER TO giosreina;

--
-- Name: ingredientes_id_ingrediente_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.ingredientes_id_ingrediente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ingredientes_id_ingrediente_seq OWNER TO giosreina;

--
-- Name: ingredientes_id_ingrediente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.ingredientes_id_ingrediente_seq OWNED BY public.ingredientes.id_ingrediente;


--
-- Name: modulo; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.modulo (
    id_modulo integer NOT NULL,
    nombre_modulo character varying(50) NOT NULL
);


ALTER TABLE public.modulo OWNER TO giosreina;

--
-- Name: modulo_id_modulo_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.modulo_id_modulo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.modulo_id_modulo_seq OWNER TO giosreina;

--
-- Name: modulo_id_modulo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.modulo_id_modulo_seq OWNED BY public.modulo.id_modulo;


--
-- Name: multimedia; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.multimedia (
    id_multimedia integer NOT NULL,
    url character varying(255) NOT NULL
);


ALTER TABLE public.multimedia OWNER TO giosreina;

--
-- Name: multimedia_id_multimedia_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.multimedia_id_multimedia_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.multimedia_id_multimedia_seq OWNER TO giosreina;

--
-- Name: multimedia_id_multimedia_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.multimedia_id_multimedia_seq OWNED BY public.multimedia.id_multimedia;


--
-- Name: operacion; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.operacion (
    id_operacion integer NOT NULL,
    accion character varying(100) NOT NULL,
    id_modulo integer NOT NULL
);


ALTER TABLE public.operacion OWNER TO giosreina;

--
-- Name: operacion_id_operacion_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.operacion_id_operacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.operacion_id_operacion_seq OWNER TO giosreina;

--
-- Name: operacion_id_operacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.operacion_id_operacion_seq OWNED BY public.operacion.id_operacion;


--
-- Name: pasos; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.pasos (
    id_paso integer NOT NULL,
    id_receta integer NOT NULL,
    descripcion text NOT NULL,
    id_multimedia integer
);


ALTER TABLE public.pasos OWNER TO giosreina;

--
-- Name: pasos_id_paso_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.pasos_id_paso_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pasos_id_paso_seq OWNER TO giosreina;

--
-- Name: pasos_id_paso_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.pasos_id_paso_seq OWNED BY public.pasos.id_paso;


--
-- Name: recetas; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.recetas (
    id_receta integer NOT NULL,
    nombre_receta character varying(100) NOT NULL,
    descripcion text,
    tiempo_preparacion integer,
    id_usuario integer NOT NULL,
    id_imagen integer
);


ALTER TABLE public.recetas OWNER TO giosreina;

--
-- Name: recetas_id_receta_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.recetas_id_receta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.recetas_id_receta_seq OWNER TO giosreina;

--
-- Name: recetas_id_receta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.recetas_id_receta_seq OWNED BY public.recetas.id_receta;


--
-- Name: rol; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.rol (
    id_rol integer NOT NULL,
    nombre_rol character varying(50) NOT NULL
);


ALTER TABLE public.rol OWNER TO giosreina;

--
-- Name: rol_id_rol_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.rol_id_rol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rol_id_rol_seq OWNER TO giosreina;

--
-- Name: rol_id_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.rol_id_rol_seq OWNED BY public.rol.id_rol;


--
-- Name: rol_operacion; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.rol_operacion (
    id_rol integer NOT NULL,
    id_operacion integer NOT NULL
);


ALTER TABLE public.rol_operacion OWNER TO giosreina;

--
-- Name: tipo_receta; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.tipo_receta (
    id_tipo_receta integer NOT NULL,
    nombre_tipo character varying(50) NOT NULL,
    descripcion text
);


ALTER TABLE public.tipo_receta OWNER TO giosreina;

--
-- Name: tipo_receta_id_tipo_receta_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.tipo_receta_id_tipo_receta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tipo_receta_id_tipo_receta_seq OWNER TO giosreina;

--
-- Name: tipo_receta_id_tipo_receta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.tipo_receta_id_tipo_receta_seq OWNED BY public.tipo_receta.id_tipo_receta;


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.usuario (
    id_usuario integer NOT NULL,
    nombre_1 character varying(20) NOT NULL,
    nombre_2 character varying(20),
    apellido_1 character varying(20) NOT NULL,
    apellido_2 character varying(20),
    correo_electronico character varying(70) NOT NULL,
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    id_multimedia integer,
    id_rol integer NOT NULL
);


ALTER TABLE public.usuario OWNER TO giosreina;

--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.usuario_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuario_id_usuario_seq OWNER TO giosreina;

--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.usuario_id_usuario_seq OWNED BY public.usuario.id_usuario;


--
-- Name: utensilios; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.utensilios (
    id_utensilio integer NOT NULL,
    id_receta integer NOT NULL,
    id_paso integer NOT NULL,
    nombre_utensilio character varying(255) NOT NULL
);


ALTER TABLE public.utensilios OWNER TO giosreina;

--
-- Name: utensilios_id_utensilio_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.utensilios_id_utensilio_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utensilios_id_utensilio_seq OWNER TO giosreina;

--
-- Name: utensilios_id_utensilio_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.utensilios_id_utensilio_seq OWNED BY public.utensilios.id_utensilio;


--
-- Name: valoracion; Type: TABLE; Schema: public; Owner: giosreina
--

CREATE TABLE public.valoracion (
    id_comentario integer NOT NULL,
    id_receta integer NOT NULL,
    id_usuario integer DEFAULT 1 NOT NULL,
    comentario text,
    fecha_comentario timestamp without time zone DEFAULT CURRENT_DATE NOT NULL,
    valor integer NOT NULL,
    CONSTRAINT fec_limite CHECK ((fecha_comentario <= CURRENT_DATE)),
    CONSTRAINT val_limite CHECK (((valor >= 1) AND (valor <= 5)))
);


ALTER TABLE public.valoracion OWNER TO giosreina;

--
-- Name: valoracion_id_comentario_seq; Type: SEQUENCE; Schema: public; Owner: giosreina
--

CREATE SEQUENCE public.valoracion_id_comentario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.valoracion_id_comentario_seq OWNER TO giosreina;

--
-- Name: valoracion_id_comentario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giosreina
--

ALTER SEQUENCE public.valoracion_id_comentario_seq OWNED BY public.valoracion.id_comentario;


--
-- Name: ingredientes id_ingrediente; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.ingredientes ALTER COLUMN id_ingrediente SET DEFAULT nextval('public.ingredientes_id_ingrediente_seq'::regclass);


--
-- Name: modulo id_modulo; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.modulo ALTER COLUMN id_modulo SET DEFAULT nextval('public.modulo_id_modulo_seq'::regclass);


--
-- Name: multimedia id_multimedia; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.multimedia ALTER COLUMN id_multimedia SET DEFAULT nextval('public.multimedia_id_multimedia_seq'::regclass);


--
-- Name: operacion id_operacion; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.operacion ALTER COLUMN id_operacion SET DEFAULT nextval('public.operacion_id_operacion_seq'::regclass);


--
-- Name: pasos id_paso; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.pasos ALTER COLUMN id_paso SET DEFAULT nextval('public.pasos_id_paso_seq'::regclass);


--
-- Name: recetas id_receta; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.recetas ALTER COLUMN id_receta SET DEFAULT nextval('public.recetas_id_receta_seq'::regclass);


--
-- Name: rol id_rol; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.rol ALTER COLUMN id_rol SET DEFAULT nextval('public.rol_id_rol_seq'::regclass);


--
-- Name: tipo_receta id_tipo_receta; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.tipo_receta ALTER COLUMN id_tipo_receta SET DEFAULT nextval('public.tipo_receta_id_tipo_receta_seq'::regclass);


--
-- Name: usuario id_usuario; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.usuario ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuario_id_usuario_seq'::regclass);


--
-- Name: utensilios id_utensilio; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.utensilios ALTER COLUMN id_utensilio SET DEFAULT nextval('public.utensilios_id_utensilio_seq'::regclass);


--
-- Name: valoracion id_comentario; Type: DEFAULT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.valoracion ALTER COLUMN id_comentario SET DEFAULT nextval('public.valoracion_id_comentario_seq'::regclass);


--
-- Data for Name: clasificacion; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.clasificacion (id_receta, id_tipo_receta) FROM stdin;
1	1
2	2
\.


--
-- Data for Name: ingredientes; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.ingredientes (id_ingrediente, id_receta, id_paso, nombre_ingrediente) FROM stdin;
1	1	1	Cebolla Larga
2	1	1	Tomate Maduro
3	1	3	2 Huevos
4	2	4	Pechuga de Pollo
\.


--
-- Data for Name: modulo; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.modulo (id_modulo, nombre_modulo) FROM stdin;
1	Gestión de Recetas
2	Perfil de Usuario
3	Valoraciones
\.


--
-- Data for Name: multimedia; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.multimedia (id_multimedia, url) FROM stdin;
1	https://picsum.photos/200/300?random=1
2	https://picsum.photos/200/300?random=2
3	https://picsum.photos/200/300?random=3
\.


--
-- Data for Name: operacion; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.operacion (id_operacion, accion, id_modulo) FROM stdin;
1	CREAR_RECETA	1
2	EDITAR_RECETA	2
\.


--
-- Data for Name: pasos; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.pasos (id_paso, id_receta, descripcion, id_multimedia) FROM stdin;
1	1	Picar finamente la cebolla y el tomate.	\N
2	1	Sofreír los vegetales en una sartén con poco aceite.	\N
3	1	Agregar los huevos y revolver hasta que cuajen.	\N
4	2	Cocinar el pollo en agua con especias.	\N
\.


--
-- Data for Name: recetas; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.recetas (id_receta, nombre_receta, descripcion, tiempo_preparacion, id_usuario, id_imagen) FROM stdin;
1	Huevos Pericos	Huevos revueltos tradicionales con cebolla y tomate.	15	1	2
2	Arroz con Pollo	Clásico plato colombiano con verduras y pollo desmechado.	45	2	3
\.


--
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.rol (id_rol, nombre_rol) FROM stdin;
1	Administrador
2	Cocinero
3	Usuario Final
\.


--
-- Data for Name: rol_operacion; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.rol_operacion (id_rol, id_operacion) FROM stdin;
1	1
1	2
2	1
\.


--
-- Data for Name: tipo_receta; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.tipo_receta (id_tipo_receta, nombre_tipo, descripcion) FROM stdin;
1	Desayuno	Platos ligeros para iniciar el día
2	Almuerzo	Comidas completas y balanceadas
3	Postre	Delicias dulces para después de comer
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.usuario (id_usuario, nombre_1, nombre_2, apellido_1, apellido_2, correo_electronico, username, password, id_multimedia, id_rol) FROM stdin;
1	Juan	Camilo	Pérez	Gómez	juan.perez@email.com	juanp_chef	pass123	1	1
2	María	\N	Rodríguez	López	m.rodriguez@email.com	maria_cook	secure456	1	2
\.


--
-- Data for Name: utensilios; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.utensilios (id_utensilio, id_receta, id_paso, nombre_utensilio) FROM stdin;
1	1	2	Sartén antiadherente
2	1	1	Cuchillo de cocina
3	2	4	Olla a presión
\.


--
-- Data for Name: valoracion; Type: TABLE DATA; Schema: public; Owner: giosreina
--

COPY public.valoracion (id_comentario, id_receta, id_usuario, comentario, fecha_comentario, valor) FROM stdin;
2	2	1	Quedó delicioso, muy fácil de seguir.	2026-04-28 00:00:00	5
3	1	2	Un poco salado, pero buena técnica.	2026-04-28 00:00:00	4
\.


--
-- Name: ingredientes_id_ingrediente_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.ingredientes_id_ingrediente_seq', 4, true);


--
-- Name: modulo_id_modulo_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.modulo_id_modulo_seq', 3, true);


--
-- Name: multimedia_id_multimedia_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.multimedia_id_multimedia_seq', 3, true);


--
-- Name: operacion_id_operacion_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.operacion_id_operacion_seq', 2, true);


--
-- Name: pasos_id_paso_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.pasos_id_paso_seq', 4, true);


--
-- Name: recetas_id_receta_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.recetas_id_receta_seq', 2, true);


--
-- Name: rol_id_rol_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.rol_id_rol_seq', 3, true);


--
-- Name: tipo_receta_id_tipo_receta_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.tipo_receta_id_tipo_receta_seq', 3, true);


--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.usuario_id_usuario_seq', 3, true);


--
-- Name: utensilios_id_utensilio_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.utensilios_id_utensilio_seq', 3, true);


--
-- Name: valoracion_id_comentario_seq; Type: SEQUENCE SET; Schema: public; Owner: giosreina
--

SELECT pg_catalog.setval('public.valoracion_id_comentario_seq', 7, true);


--
-- Name: clasificacion clasificacion_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.clasificacion
    ADD CONSTRAINT clasificacion_pkey PRIMARY KEY (id_receta, id_tipo_receta);


--
-- Name: ingredientes ingredientes_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.ingredientes
    ADD CONSTRAINT ingredientes_pkey PRIMARY KEY (id_ingrediente);


--
-- Name: modulo modulo_nombre_modulo_key; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT modulo_nombre_modulo_key UNIQUE (nombre_modulo);


--
-- Name: modulo modulo_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.modulo
    ADD CONSTRAINT modulo_pkey PRIMARY KEY (id_modulo);


--
-- Name: multimedia multimedia_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.multimedia
    ADD CONSTRAINT multimedia_pkey PRIMARY KEY (id_multimedia);


--
-- Name: multimedia multimedia_url_key; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.multimedia
    ADD CONSTRAINT multimedia_url_key UNIQUE (url);


--
-- Name: operacion operacion_accion_key; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.operacion
    ADD CONSTRAINT operacion_accion_key UNIQUE (accion);


--
-- Name: operacion operacion_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.operacion
    ADD CONSTRAINT operacion_pkey PRIMARY KEY (id_operacion);


--
-- Name: pasos pasos_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.pasos
    ADD CONSTRAINT pasos_pkey PRIMARY KEY (id_paso);


--
-- Name: recetas recetas_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.recetas
    ADD CONSTRAINT recetas_pkey PRIMARY KEY (id_receta);


--
-- Name: rol rol_nombre_rol_key; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_nombre_rol_key UNIQUE (nombre_rol);


--
-- Name: rol_operacion rol_operacion_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.rol_operacion
    ADD CONSTRAINT rol_operacion_pkey PRIMARY KEY (id_rol, id_operacion);


--
-- Name: rol rol_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id_rol);


--
-- Name: tipo_receta tipo_receta_nombre_tipo_key; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.tipo_receta
    ADD CONSTRAINT tipo_receta_nombre_tipo_key UNIQUE (nombre_tipo);


--
-- Name: tipo_receta tipo_receta_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.tipo_receta
    ADD CONSTRAINT tipo_receta_pkey PRIMARY KEY (id_tipo_receta);


--
-- Name: usuario usuario_correo_electronico_key; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_correo_electronico_key UNIQUE (correo_electronico);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- Name: usuario usuario_username_key; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_username_key UNIQUE (username);


--
-- Name: utensilios utensilios_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.utensilios
    ADD CONSTRAINT utensilios_pkey PRIMARY KEY (id_utensilio);


--
-- Name: valoracion valoracion_pkey; Type: CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.valoracion
    ADD CONSTRAINT valoracion_pkey PRIMARY KEY (id_comentario);


--
-- Name: clasificacion clasificacion_id_receta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.clasificacion
    ADD CONSTRAINT clasificacion_id_receta_fkey FOREIGN KEY (id_receta) REFERENCES public.recetas(id_receta);


--
-- Name: clasificacion clasificacion_id_tipo_receta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.clasificacion
    ADD CONSTRAINT clasificacion_id_tipo_receta_fkey FOREIGN KEY (id_tipo_receta) REFERENCES public.tipo_receta(id_tipo_receta);


--
-- Name: clasificacion fk_clasificacion_receta; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.clasificacion
    ADD CONSTRAINT fk_clasificacion_receta FOREIGN KEY (id_receta) REFERENCES public.recetas(id_receta) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: clasificacion fk_clasificacion_tipo_receta; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.clasificacion
    ADD CONSTRAINT fk_clasificacion_tipo_receta FOREIGN KEY (id_tipo_receta) REFERENCES public.tipo_receta(id_tipo_receta) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: ingredientes fk_ingrediente_paso; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.ingredientes
    ADD CONSTRAINT fk_ingrediente_paso FOREIGN KEY (id_paso) REFERENCES public.pasos(id_paso) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: ingredientes fk_ingrediente_receta; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.ingredientes
    ADD CONSTRAINT fk_ingrediente_receta FOREIGN KEY (id_receta) REFERENCES public.recetas(id_receta) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: pasos fk_multimedia_paso; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.pasos
    ADD CONSTRAINT fk_multimedia_paso FOREIGN KEY (id_multimedia) REFERENCES public.multimedia(id_multimedia) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: recetas fk_multimedia_receta; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.recetas
    ADD CONSTRAINT fk_multimedia_receta FOREIGN KEY (id_imagen) REFERENCES public.multimedia(id_multimedia) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: usuario fk_multimedia_usuario; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_multimedia_usuario FOREIGN KEY (id_multimedia) REFERENCES public.multimedia(id_multimedia) ON DELETE RESTRICT;


--
-- Name: operacion fk_operacion_modulo; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.operacion
    ADD CONSTRAINT fk_operacion_modulo FOREIGN KEY (id_modulo) REFERENCES public.recetas(id_receta) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: pasos fk_receta_paso; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.pasos
    ADD CONSTRAINT fk_receta_paso FOREIGN KEY (id_receta) REFERENCES public.recetas(id_receta) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: rol_operacion fk_rol_operacion_op; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.rol_operacion
    ADD CONSTRAINT fk_rol_operacion_op FOREIGN KEY (id_operacion) REFERENCES public.operacion(id_operacion) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: rol_operacion fk_rol_operacion_rol; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.rol_operacion
    ADD CONSTRAINT fk_rol_operacion_rol FOREIGN KEY (id_rol) REFERENCES public.rol(id_rol) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: usuario fk_rol_usuario; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_rol_usuario FOREIGN KEY (id_rol) REFERENCES public.rol(id_rol) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: recetas fk_usuario_receta; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.recetas
    ADD CONSTRAINT fk_usuario_receta FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: utensilios fk_utensilio_paso; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.utensilios
    ADD CONSTRAINT fk_utensilio_paso FOREIGN KEY (id_paso) REFERENCES public.pasos(id_paso) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: utensilios fk_utensilio_receta; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.utensilios
    ADD CONSTRAINT fk_utensilio_receta FOREIGN KEY (id_receta) REFERENCES public.recetas(id_receta) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: valoracion fk_valoracion_receta; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.valoracion
    ADD CONSTRAINT fk_valoracion_receta FOREIGN KEY (id_receta) REFERENCES public.recetas(id_receta) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: valoracion fk_valoracion_usuario; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.valoracion
    ADD CONSTRAINT fk_valoracion_usuario FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario) ON UPDATE CASCADE;


--
-- Name: rol_operacion rol_operacion_id_operacion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giosreina
--

ALTER TABLE ONLY public.rol_operacion
    ADD CONSTRAINT rol_operacion_id_operacion_fkey FOREIGN KEY (id_operacion) REFERENCES public.operacion(id_operacion);


--
-- PostgreSQL database dump complete
--

\unrestrict GVuwQgdNC5DsdWMyKHZ46d8u9klLjFMeR9nktj3CORjdn9w1M7LRIriNUUCpCFl