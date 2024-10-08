package Persistencia;

import Logica.Conexion;
import Logica.DateNumericDocumentFilter;
import Logica.Libro;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AbstractDocument;

public class LibroDAO {

    private Conexion conexion;

    public LibroDAO() {
        conexion = new Conexion();
    }

    public List<Libro> obtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT * FROM libros"; // Cambia esto por tu consulta SQL real

        try (ResultSet rs = conexion.consultarRegistros(query)) {
            while (rs != null && rs.next()) {
                Libro libro = new Libro();
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setGenero(rs.getString("genero"));
                libro.setNumero_paginas(rs.getInt("numero_paginas"));
                libro.setEditorial(rs.getString("editorial"));

                // Fecha Inicio --------------------------------------------
                String fechaInicioStr = rs.getString("fecha_inicio");
                java.util.Date fechaInicio = null; // Inicializa la variable
                String fechaFinStr = rs.getString("fecha_fin");
                java.util.Date fechaFin = null; // Inicializa la variable

                // Verifica que fechaInicioStr no sea null y no esté vacío
                if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        fechaInicio = formatoFecha.parse(fechaInicioStr);
                        libro.setFecha_inicio(fechaInicio);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    libro.setFecha_inicio(null); // Establece a null si la cadena está vacía o es null
                }
                
                // Verifica que fechaFinStr no sea null y no esté vacío
                if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        fechaFin = formatoFecha.parse(fechaFinStr);
                        libro.setFecha_fin(fechaFin);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    libro.setFecha_fin(null); // Establece a null si la cadena está vacía o es null
                }

                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener libros: " + e.getMessage());
        }

        return libros;
    }

    public void cerrarConexion() {
        conexion.closeConnection();
    }
}
