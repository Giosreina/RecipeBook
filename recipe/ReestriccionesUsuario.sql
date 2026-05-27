DROP USER IF EXISTS recipebooksupervisor;
DROP USER IF EXISTS recipebookadmin;
DROP USER IF EXISTS recipebookreader;
DROP ROLE IF EXISTS supervisor;
DROP ROLE IF EXISTS administrador;
DROP ROLE IF EXISTS reader;

-- Roles agrupadores (sin LOGIN)
CREATE ROLE supervisor;
CREATE ROLE administrador;
CREATE ROLE reader;

-- Usuarios con LOGIN
CREATE USER recipebooksupervisor WITH LOGIN PASSWORD 'RBsup3rv1s0r!';
CREATE USER recipebookadmin      WITH LOGIN PASSWORD 'RB4dm1n!';
CREATE USER recipebookreader     WITH LOGIN PASSWORD 'RBr34d3r!';

-- Supervisor: modera contenido, no puede insertar ni modificar
GRANT SELECT, DELETE ON usuario, recetas, valoracion TO supervisor;

-- Administrador: acceso total a tablas y secuencias
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public    TO administrador;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO administrador;

-- Reader: solo vistas, nunca tablas base (no ve passwords ni correos)
GRANT SELECT ON recetas_mejor_valoradas, reporte_recetas, v_recetas_rapidas, usuario_receta TO reader;

-- Asignar roles a usuarios
GRANT supervisor    TO recipebooksupervisor;
GRANT administrador TO recipebookadmin;
GRANT reader        TO recipebookreader;

-- REVOKE explícitos para blindar permisos
REVOKE INSERT, UPDATE ON usuario, recetas, valoracion FROM supervisor;
REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public  FROM reader;
-- Restaurar vistas al reader tras el REVOKE ALL
GRANT SELECT ON recetas_mejor_valoradas, reporte_recetas, v_recetas_rapidas, usuario_receta TO reader;

-- Verificación
SELECT grantee, table_name, privilege_type
FROM information_schema.role_table_grants
WHERE grantee IN ('supervisor', 'administrador', 'reader')
  AND table_schema = 'public'
ORDER BY grantee, table_name;

SELECT u.rolname AS usuario, r.rolname AS rol_heredado
FROM pg_auth_members m
JOIN pg_roles r ON m.roleid = r.oid
JOIN pg_roles u ON m.member = u.oid
WHERE u.rolname IN ('recipebooksupervisor', 'recipebookadmin', 'recipebookreader');