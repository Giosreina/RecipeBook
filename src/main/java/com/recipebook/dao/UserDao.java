package com.recipebook.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.recipebook.logic.Receta;
import com.recipebook.logic.User;

@SuppressWarnings("CallToPrintStackTrace")
public class UserDao {
    private final SQLController sqlController;
    private final RecetaDao recetaDAO;

    public UserDao(SQLController sqlController) {
        this.sqlController = sqlController;
        this.recetaDAO = new RecetaDao(sqlController);
    }

    /**
     * Inserta una URL en la tabla MULTIMEDIA y retorna el ID generado.
     * 
     * @param url La URL de la imagen.
     * @return El ID de la multimedia insertada, o null si falla.
     */
    private String obtenerRol(int idRol) {
        String selectRolIDQuery = String.format(
            "SELECT nombre_rol FROM ROL WHERE id_rol = %d", idRol
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
    private Integer insertarMultimedia(String url) {
        try {
            String insertMultimediaQuery = String.format(
                "INSERT INTO MULTIMEDIA (url) VALUES ('%s')",
             url
            );
            sqlController.executeUpdate(insertMultimediaQuery);
            ResultSet rs = sqlController.executeQuery("SELECT SCOPE_IDENTITY() AS id_multimedia");
            rs.next();
            return rs.getInt("id_multimedia");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Agrega un usuario a la base de datos.
     * 
     * @param user Usuario a agregar.
     * @return true si el usuario se agregó correctamente, false en caso contrario.
     */
    public boolean agregarUsuario(User user) {
        // Insertar imagen del usuario en MULTIMEDIA si existe
        Integer idMultimediaUsuario = null;
        if (user.getUrlMultimedia() != null) {
            idMultimediaUsuario = insertarMultimedia(user.getUrlMultimedia());
            if (idMultimediaUsuario == null) {
                return false; // Falló la inserción de multimedia
            }
        }

        String insertUserQuery = String.format(
            "INSERT INTO USUARIO (nombre_1, nombre_2, apellido_1, apellido_2, correo_electronico, username, password, id_multimedia, id_rol) VALUES ('%s', %s, '%s', %s, '%s', '%s', '%s', %s, %d)",
            user.getNombre_1(),
            user.getNombre_2() != null ? "'" + user.getNombre_2() + "'" : "NULL",
            user.getApellido_1(),
            user.getApellido_2() != null ? "'" + user.getApellido_2() + "'" : "NULL",
            user.getCorreo(),
            user.getUsername(),
            user.getPassword(),
            idMultimediaUsuario != null ? idMultimediaUsuario.toString() : "NULL",
            1 // Asumiendo rol por defecto, ajustar según lógica de negocio
        );

        try {
            sqlController.executeUpdate(insertUserQuery);
            ResultSet rs = sqlController.executeQuery("SELECT SCOPE_IDENTITY() AS id_usuario");
            rs.next();
            int userID = rs.getInt("id_usuario");

            for (Receta receta : user.getRecetas()) {
                recetaDAO.agregarReceta(receta, userID);
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private String obtenerImagen(int idMultimedia) {
        String selectUrlQuery = String.format(
            "SELECT url FROM MULTIMEDIA WHERE id_multimedia = %d", idMultimedia
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

   
    public ArrayList<User> obtenerUsuarios() {
        ArrayList<User> usersContainer = new ArrayList<>();
        String selectUsersQuery = "SELECT * FROM Users";

        try {
            ResultSet rs = sqlController.executeQuery(selectUsersQuery);

            while (rs.next()) {
                Integer userID = rs.getInt("id_usuario");
                String nombre_1 = rs.getString("nombre_1");
                String nombre_2 = rs.getString("nombre_2");
                String apellido_1 = rs.getString("apellido_1");
                String apellido_2 = rs.getString("apellido_2");
                String correo = rs.getString("correo_electronico");
                String urlMultimedia = obtenerImagen(rs.getInt("id_multimedia"));
                String username = rs.getString("username");
                String password = rs.getString("password");
                String rol = obtenerRol(rs.getInt("id_rol"));

                User user = new User(userID, nombre_1, nombre_2, apellido_1, apellido_2, correo, username, rol,password, urlMultimedia);
                user.setRecetas(recetaDAO.obtenerRecetasPorUsuario(userID));
                usersContainer.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersContainer;
    }

    /**
     * Elimina un usuario de la base de datos.
     * 
     * @param username Nombre del usuario a eliminar.
     * @return true si el usuario se eliminó correctamente, false en caso contrario.
     */
    public boolean eliminarUsuario(String username) {
        String deleteUserQuery = String.format(
            "DELETE FROM Users WHERE Name = '%s'", username
        );

        String obtainUserIDQuery = String.format(
            "SELECT UserID FROM Users WHERE Name = '%s'", username
        );

        try {
            ResultSet userIdQ = sqlController.executeQuery(obtainUserIDQuery);
                int userID = userIdQ.getInt("UserID");
                recetaDAO.eliminarTodasRecetasPorUsuario(userID);
            sqlController.executeUpdate(deleteUserQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtiene un usuario de la base de datos por su nombre.
     * 
     * @param username Nombre del usuario a obtener.
     * @return Usuario encontrado o null si no se encuentra.
     */
    public User obtenerUsuario(String username) {
        String selectUserQuery = String.format(
            "SELECT * FROM Users WHERE Name = '%s'", username
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
                String urlMultimedia = obtenerImagen(rs.getInt("id_multimedia"));
                String password = rs.getString("password");
                String rol = obtenerRol(rs.getInt("id_rol"));

                User user = new User(userID, nombre_1, nombre_2, apellido_1, apellido_2, correo, username, rol,password, urlMultimedia);
                user.setRecetas(recetaDAO.obtenerRecetasPorUsuario(userID));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene el ID de un usuario por su nombre.
     * 
     * @param username Nombre del usuario.
     * @return ID del usuario o -1 si no se encuentra.
     */
    public int obtenerUserID(String username) {
        String selectUserIDQuery = String.format(
            "SELECT id_usuario FROM Users WHERE Name = '%s'", username
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectUserIDQuery);

            if (rs.next()) {
                return rs.getInt("UserID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Obtiene el RecetaDAO asociado.
     * 
     * @return RecetaDAO.
     */
    public RecetaDao getRecetaDAO() {
        return recetaDAO;
    }
}