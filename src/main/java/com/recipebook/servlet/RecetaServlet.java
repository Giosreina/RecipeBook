package com.recipebook.servlet;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.recipebook.dao.RecetaDao;
import com.recipebook.dao.SQLController;
import com.recipebook.dao.UserDao;
import com.recipebook.logic.Receta;
import com.recipebook.logic.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "RecetaServlet", urlPatterns = {"/RecetaServlet"})
public class RecetaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @SuppressWarnings("unchecked")
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ── 1. Verificar sesión ───────────────────────────────────────────
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // ── 2. Leer parámetros del formulario ─────────────────────────────
        String nombre      = request.getParameter("nombre");
        String tipo        = request.getParameter("tipo");
        String imagen      = request.getParameter("imagen");
        String descripcion = request.getParameter("descripcion");

        // Deserializar ingredientes y utensilios generales con null-safety
        String[] ingredientes = parseJsonArray(request.getParameter("ingredientes"));
        String[] utensilios   = parseJsonArray(request.getParameter("utensilios"));

        // ── 3. Construir objeto Receta ────────────────────────────────────
        Receta receta = new Receta(nombre, imagen, descripcion, tipo);

        // Agregar ingredientes y utensilios generales
        for (String ing : ingredientes) {
            if (ing != null && !ing.isBlank()) receta.addIngrediente(ing);
        }
        for (String ute : utensilios) {
            if (ute != null && !ute.isBlank()) receta.addUtensilio(ute);
        }

        // Agregar pasos
        String pasosJson = request.getParameter("pasos");
        if (pasosJson != null && !pasosJson.isBlank()) {
            Map<String, Object>[] mapPasos = new Gson().fromJson(pasosJson, Map[].class);
            for (Map<String, Object> p : mapPasos) {
                String descPaso  = getString(p, "descripcion");
                String tiempoStr = getString(p, "tiempo");
                String imgPaso   = getString(p, "imagen");
                int tiempo = 0;
                if (tiempoStr != null && !tiempoStr.isBlank()) {
                    try { tiempo = Integer.parseInt(tiempoStr); } catch (NumberFormatException ignored) {}
                }

                String[] utePaso  = parseObjectToArray(p.get("utensilios"));
                String[] ingPaso  = parseObjectToArray(p.get("ingredientes"));

                boolean tieneExtras = (utePaso.length > 0 && !utePaso[0].isBlank())
                                   || (ingPaso.length > 0 && !ingPaso[0].isBlank());

                if (tieneExtras) {
                    receta.addStep(descPaso, tiempo, utePaso, ingPaso, imgPaso);
                } else {
                    receta.addStep(descPaso, tiempo, imgPaso);
                }
            }
        }

        // ── 4. Guardar en BD con conexión fresca ──────────────────────────
        SQLController sql = new SQLController(
            "jdbc:postgresql://localhost:5432/recipebook", "giosreina", "Kabuto43*"
        );
        boolean success = false;
        try {
            if (sql.isConnected()) {
                UserDao userDao = new UserDao(sql);
                int userId = userDao.obtenerUserID(user.getUsername());
                RecetaDao recetaDAO = userDao.getRecetaDAO();
                success = recetaDAO.agregarReceta(receta, userId);
            }
        } finally {
            sql.closeConnection();
        }

        // ── 5. Responder ──────────────────────────────────────────────────
        if (success) {
            // forward para que receta.jsp lea del request (no de sesión)
            request.setAttribute("receta", receta);
            request.setAttribute("pasos", receta.getPasos());
            request.getRequestDispatcher("receta.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al guardar la receta. Intenta de nuevo.");
            request.getRequestDispatcher("subir.html").forward(request, response);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /** Parsea un JSON de array de strings con null-safety. */
    private String[] parseJsonArray(String json) {
        if (json == null || json.isBlank()) return new String[0];
        try {
            String[] result = new Gson().fromJson(json, String[].class);
            return result != null ? result : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    /** Convierte un Object (puede ser List o String) a String[]. */
    private String[] parseObjectToArray(Object obj) {
        if (obj == null) return new String[0];
        String raw = obj.toString().replaceAll("[\\[\\]\"]", "").trim();
        if (raw.isEmpty()) return new String[0];
        return raw.split(",\\s*");
    }

    /** Lee un valor del map como String de forma segura. */
    private String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : "";
    }
}