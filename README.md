# RecipeBook

## Integrantes del equipo
- Giovanny Sierra Reina - 20232020138
- Angel Andres Díaz Vergara - 20231020056
- Juan Felipe Guevara Olaya - 20231020117
- Mathew Stev Toro Mondragón - 20231020077
- David Sánchez Acero - 20232020049

## Descripción del proyecto
RecipeBook es una aplicación web académica desarrollada en Java con Maven. Utiliza JSP, Servlets y Jetty para la presentación web, y se conecta a PostgreSQL mediante JDBC directo.

> Importante: Este proyecto NO utiliza JPA ni Hibernate para la conexión a base de datos. El archivo `src/main/resources/META-INF/persistence.xml` existe, pero actualmente está vacío y no se usa en la lógica de acceso a datos.

## Tecnologías principales
- Java 22
- Maven
- JSP / Servlets
- Jetty Maven Plugin
- PostgreSQL
- JDBC directo

## Estructura principal del proyecto
- `pom.xml` - configuración de Maven y dependencias.
- `src/main/java/` - código fuente Java, servlets, lógica y DAO.
- `src/main/resources/` - recursos Java, incluyendo `META-INF/persistence.xml`.
- `src/main/webapp/` - interfaz web y archivos JSP/HTML/CSS/JS.
- `src/main/webapp/WEB-INF/` - configuración del servlet y Beans.
- `recipe/RecipeBookScript.sql` - script SQL para crear y poblar la base de datos.
- `target/` - salida de compilación generada por Maven.

## Diseño de la base de datos
La base de datos del proyecto está modelada como un esquema relacional para una aplicación de recetas.

- Tablas principales: `USUARIO`, `RECETAS`, `PASOS`, `INGREDIENTES`, `UTENSILIOS`, `MULTIMEDIA`, `ROL`, `MODULO`, `OPERACION`, `TIPO_RECETA` y `CLASIFICACION`.
- Constraints comunes: `PRIMARY KEY` en identificadores consecutivos (`SERIAL`), `NOT NULL` en campos obligatorios, y `UNIQUE` en campos como `username`, `correo_electronico` y `url`.
- Relaciones clave: `FOREIGN KEY` en `RECETAS.id_usuario`, `PASOS.id_receta`, `INGREDIENTES.id_receta`, `UTENSILIOS.id_receta`, `CLASIFICACION.id_receta` / `id_tipo_receta`, y referencias a `MULTIMEDIA` para imágenes y soporte multimedia.
- Reglas de integridad: se aplican constraints `CHECK` para validar valores como `valor` en una valoración y fechas de comentarios dentro de rangos razonables.
- Vistas SQL: existen vistas de consulta como `recetas_mejor_valoradas`, `reporte_recetas`, `usuario_receta` y `v_recetas_rapidas`, que simplifican el acceso a datos agregados y respaldan consultas de reporte.

El script `recipe/RecipeBookScript.sql` define este modelo y ofrece la base para crear el esquema en PostgreSQL.

## Cómo funciona la conexión a la base de datos
La conexión a PostgreSQL se gestiona desde `src/main/java/com/recipebook/dao/DAOFactory.java`.

La clase `DAOFactory` contiene los valores de conexión:
- `URL`
- `USER`
- `PASS`

Y crea una conexión nueva usando `SQLController`:

```java
public static SQLController crearConexion() {
    return new SQLController(URL, USER, PASS);
}
```

> Advertencia: Las credenciales dentro de `DAOFactory.java` son específicas del entorno local de un integrante del equipo y probablemente NO funcionarán en otros equipos. Deben ser cambiadas antes de ejecutar la aplicación.

## Requisitos previos
1. Java JDK 22 instalado.
2. Apache Maven 3.9.x o superior.
3. PostgreSQL instalado y en ejecución en el equipo local.
4. Acceso al script SQL `recipe/RecipeBookScript.sql`.

## Configuración de PostgreSQL local
1. Instale PostgreSQL en su máquina.
2. Inicie el servicio de PostgreSQL.
3. Cree la base de datos `recipebook`:

```sql
CREATE DATABASE recipebook;
```

4. Ejecute el script inicial de la base de datos:

```bash
psql -U <usuario_postgres> -d recipebook -f recipe/RecipeBookScript.sql
```

> Si usa PgAdmin, puede ejecutar el contenido de `recipe/RecipeBookScript.sql` en una nueva consulta sobre la base de datos `recipebook` usando el query tool (Copie y pegue el query dentro de RecipeBookScript.sql y ejecutelo completamente).

## Configurar credenciales en `DAOFactory.java`
Abra `src/main/java/com/recipebook/dao/DAOFactory.java` y modifique las siguientes constantes según su instalación local de PostgreSQL:

- `URL` — por ejemplo: `jdbc:postgresql://localhost:5432/recipebook`
- `USER` — nombre de usuario de PostgreSQL
- `PASS` — contraseña del usuario de PostgreSQL

Ejemplo:

```java
private static final String URL  = "jdbc:postgresql://localhost:5432/recipebook";
private static final String USER = "mi_usuario";
private static final String PASS = "mi_contraseña";
```

> No use las credenciales que aparecen en el repositorio, ya que pertenecen al entorno local de un integrante del equipo.

## Ejecutar la aplicación
Desde la raíz del proyecto, ejecute:

```bash
mvn jetty:run
```

Abra un navegador y vaya a:

```text
http://localhost:8080/
```

## Verificar que la conexión funciona
1. Revise la consola donde se ejecuta Maven. Si la aplicación arranca sin errores de conexión, Jetty se iniciará correctamente.
2. Abra la aplicación en `http://localhost:8080/`.
3. Use alguna funcionalidad que requiera acceso a datos, como iniciar sesión o explorar recetas.
4. Si la aplicación carga datos y no aparece una excepción de JDBC, la conexión con PostgreSQL está funcionando.

> Si ve errores como `FATAL`, `Connection refused` o `password authentication failed`, revise la URL, el usuario y la contraseña en `DAOFactory.java`.

## Notas adicionales
- El archivo `src/main/resources/META-INF/persistence.xml` está presente por estructura, pero no participa en la lógica actual de conexión.
- No modifique el proyecto esperando que JPA/Hibernate entre en funcionamiento sin cambios adicionales en el código.
- Para pruebas locales, mantenga el servicio de PostgreSQL activo mientras ejecuta `mvn jetty:run`.
