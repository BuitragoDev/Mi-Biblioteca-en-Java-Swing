package Logica;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Funciones {

    // Método para reproducir el sonido
    public void reproducirSonido(String ruta) {
        try {
            // Cargar el sonido desde los recursos usando getResource
            URL audioUrl = getClass().getResource(ruta);

            // Verificar si el recurso fue encontrado
            if (audioUrl == null) {
                throw new Exception("El archivo de sonido no fue encontrado: " + ruta);
            }

            // Convertir la URL en AudioInputStream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl);

            // Crear el clip de audio
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Reproducir el sonido
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para agregar el listener a los botones
    public void agregarSonidoABotones(javax.swing.JPanel panel) {
        // Crea un MouseAdapter común para todos los botones
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                reproducirSonido("/clic.wav"); // Cambia a la ruta de tu sonido
            }
        };

        // Itera sobre todos los componentes del panel
        for (Component comp : panel.getComponents()) {
            // Si el componente es un JButton, le añadimos el listener
            if (comp instanceof JButton) {
                comp.addMouseListener(mouseAdapter);
            }
        }
    }

    public void crearCarpetaYCopiarImagen() {
        // Obtener el directorio del usuario
        String userHome = System.getProperty("user.home");

        // Definir la ruta para la carpeta "portadas"
        String carpetaDestino = userHome + "/Mi Biblioteca/portadas/";

        // Crear la carpeta "portadas" si no existe
        File directorio = new File(carpetaDestino);
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crear la carpeta si no existe
        }

        // Definir la ruta completa donde se copiará la imagen
        File archivoDestino = new File(carpetaDestino + "portadaLibro.png");

        // Cargar la imagen desde el JAR (dentro de resources)
        try (InputStream inputStream = getClass().getResourceAsStream("/portadaLibro.png")) {
            if (inputStream != null) {
                // Copiar el archivo del JAR al sistema de archivos
                Files.copy(inputStream, archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                System.out.println("No se encontró el archivo en /portadaLibro.png");
            }
        } catch (IOException e) {
            System.out.println("Error al copiar la imagen: " + e.getMessage());
        }
    }

}
