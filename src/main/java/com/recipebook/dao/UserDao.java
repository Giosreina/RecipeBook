package com.recipebook.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.recipebook.logic.Receta;
import com.recipebook.logic.User;
import com.recipebook.logic.UsersContainer;

@SuppressWarnings("CallToPrintStackTrace")
public class UserDao {
    private final SQLController sqlController;
    private final RecetaDao recetaDAO;

    public UserDao(SQLController sqlController) {
        this.sqlController = sqlController;
        this.recetaDAO = new RecetaDao(sqlController);
    }

    /**
     * Obtiene el nombre del rol según su ID.
     */
    private String obtenerRol(int idRol) {
        String selectRolIDQuery = String.format(
            "SELECT nombre_rol FROM rol WHERE id_rol = %d", idRol
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectRolIDQuery);
            if (rs.next()) {
                return rs.getString("nombre_rol");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserta una URL en la tabla MULTIMEDIA y retorna el ID generado.
     */
    private Integer insertarMultimedia(String url) {
        try {
            String insertMultimediaQuery = String.format(
                "INSERT INTO multimedia (url) VALUES ('%s') RETURNING id_multimedia",
                url
            );
            ResultSet rs = sqlController.executeQuery(insertMultimediaQuery);
            if (rs.next()) {
                return rs.getInt("id_multimedia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Agrega un usuario a la base de datos.
     */
    public boolean agregarUsuario(User user) {
        Integer idMultimediaUsuario = null;
        if (user.getUrlMultimedia() != null && !user.getUrlMultimedia().isEmpty()) {
            idMultimediaUsuario = insertarMultimedia(user.getUrlMultimedia());
        }

        try {
            String insertUserQuery = String.format(
                "INSERT INTO usuario (nombre_1, nombre_2, apellido_1, apellido_2, correo_electronico, username, password, id_multimedia, id_rol) " +
                "VALUES ('%s', %s, '%s', %s, '%s', '%s', '%s', %s, %d)",
                escapeSQLString(user.getNombre_1()),
                user.getNombre_2() != null && !user.getNombre_2().isEmpty() ? "'" + escapeSQLString(user.getNombre_2()) + "'" : "NULL",
                escapeSQLString(user.getApellido_1()),
                user.getApellido_2() != null && !user.getApellido_2().isEmpty() ? "'" + escapeSQLString(user.getApellido_2()) + "'" : "NULL",
                escapeSQLString(user.getCorreo()),
                escapeSQLString(user.getUsername()),
                escapeSQLString(user.getPassword()),
                idMultimediaUsuario != null ? idMultimediaUsuario.toString() : "NULL",
                3 
            );

            System.out.println("SQL Query: " + insertUserQuery);
            int rowsAffected = sqlController.executeUpdate(insertUserQuery);
            
            if (rowsAffected > 0) {
                String selectIdQuery = String.format(
                    "SELECT id_usuario FROM usuario WHERE username = '%s'",
                    escapeSQLString(user.getUsername())
                );
                ResultSet rs = sqlController.executeQuery(selectIdQuery);
                if (rs.next()) {
                    int userID = rs.getInt("id_usuario");

                    for (Receta receta : user.getRecetas().getRecetas()) {
                        recetaDAO.agregarReceta(receta, userID);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException al insertar usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private String obtenerImagen(int idMultimedia) {
        if (idMultimedia <= 0) {
            return null;
        }

        String selectUrlQuery = String.format(
            "SELECT url FROM multimedia WHERE id_multimedia = %d", idMultimedia
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectUrlQuery);
            if (rs.next()) {
                return rs.getString("url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene todos los usuarios de la base de datos.
     */
    public UsersContainer obtenerUsuarios() {
        UsersContainer usersContainer = new UsersContainer();
        String selectUsersQuery = "SELECT * FROM usuario";

        try {
            ResultSet rs = sqlController.executeQuery(selectUsersQuery);

            while (rs.next()) {
                Integer userID = rs.getInt("id_usuario");
                String nombre_1 = rs.getString("nombre_1");
                String nombre_2 = rs.getString("nombre_2");
                String apellido_1 = rs.getString("apellido_1");
                String apellido_2 = rs.getString("apellido_2");
                String correo = rs.getString("correo_electronico");
                int idMultimedia = rs.getInt("id_multimedia");
                String urlMultimedia = obtenerImagen(idMultimedia);
                String username = rs.getString("username");
                String password = rs.getString("password");
                int idRol = rs.getInt("id_rol");
                String rol = obtenerRol(idRol);

                User user = new User(userID, nombre_1, nombre_2, apellido_1, apellido_2, correo, username, rol, password, urlMultimedia);
                usersContainer.addUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersContainer;
    }

    /**
     * Obtiene un usuario de la base de datos por su username.
     */
    public User obtenerUsuario(String username) {
        String selectUserQuery = String.format(
            "SELECT * FROM usuario WHERE username = '%s'", escapeSQLString(username)
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectUserQuery);

            if (rs.next()) {
                Integer userID = rs.getInt("id_usuario");
                String nombre_1 = rs.getString("nombre_1");
                String nombre_2 = rs.getString("nombre_2");
                String apellido_1 = rs.getString("apellido_1");
                String apellido_2 = rs.getString("apellido_2");
                String correo = rs.getString("correo_electronico");
                int idMultimedia = rs.getInt("id_multimedia");
                String urlMultimedia = obtenerImagen(idMultimedia);
                String password = rs.getString("password");
                int idRol = rs.getInt("id_rol");
                String rol = obtenerRol(idRol);

                return new User(userID, nombre_1, nombre_2, apellido_1, apellido_2, correo, username, rol, password, urlMultimedia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene el ID de un usuario por su username.
     */
    public int obtenerUserID(String username) {
        String selectUserIDQuery = String.format(
            "SELECT id_usuario FROM usuario WHERE username = '%s'", escapeSQLString(username)
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectUserIDQuery);

            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public RecetaDao getRecetaDAO() {
        return recetaDAO;
    }

    private String escapeSQLString(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("'", "''");
    }
}