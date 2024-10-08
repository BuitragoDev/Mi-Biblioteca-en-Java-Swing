package Logica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {

    public Connection conn;

    public Conexion() {
        try {
            Class.forName("org.sqlite.JDBC");

            // Obtener la base de datos como un recurso dentro del JAR
            InputStream input = getClass().getResourceAsStream("/db/biblioteca.s3db");

            if (input == null) {
                throw new FileNotFoundException("Base de datos no encontrada en la ruta especificada.");
            }

            String userHome = System.getProperty("user.home");
            File dbDir = new File(userHome + "/Mi Biblioteca/db");
            dbDir.mkdirs();
            File dbFile = new File(dbDir, "biblioteca.s3db");

            // Solo copiar la base de datos si no existe en la ubicación fija
            if (!dbFile.exists()) {
                try (OutputStream out = new FileOutputStream(dbFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    //System.out.println("Base de datos copiada a: " + dbFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Error al copiar la base de datos: " + e.getMessage());
                }
            } else {
                //System.out.println("La base de datos ya existe en: " + dbFile.getAbsolutePath());
            }

            // Conéctate a la base de datos en la nueva ubicación
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión establecida a la base de datos.");
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public int ejecutarSentenciaSQL(String strSentenciaSQL) {
        try (PreparedStatement pstm = conn.prepareStatement(strSentenciaSQL)) {
            pstm.execute();
            return 1;
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }

    public ResultSet consultarRegistros(String strSentenciaSQL) {
        try {
            PreparedStatement pstm = conn.prepareStatement(strSentenciaSQL);
            return pstm.executeQuery(); // Recuerda cerrar este ResultSet después de usarlo
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) { // Verifica que la conexión no sea nula y esté abierta
                conn.close(); // Cierra la conexión
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

}
