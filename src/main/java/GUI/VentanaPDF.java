package GUI;

import javax.swing.JFrame;
import Logica.Funciones;
import java.awt.Desktop;
import java.io.File;
import javax.swing.JLabel;

public class VentanaPDF extends javax.swing.JFrame {
    public VentanaPDF() {
        // Configurar la ventana sin barra de título y botones
        setUndecorated(true);

        // Maximizar la ventana a pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initComponents();
        // Obtén el directorio del usuario y la ruta del PDF
        String userHome = System.getProperty("user.home");
        String pdfPath = userHome + "/Mi Biblioteca/pdf/listadoLibros.pdf";

        // Asigna la ruta del PDF a jLabel2
        jLabel2.setText(pdfPath);
        Funciones misFunciones = new Funciones();
        misFunciones.agregarSonidoABotones(panelPrincipalPreguntaSalir);
        setSize(800, 200);
        VentanaPrincipal principal = new VentanaPrincipal();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipalPreguntaSalir = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnCerrarPDF = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnVerPDF = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelPrincipalPreguntaSalir.setBackground(new java.awt.Color(204, 204, 204));
        panelPrincipalPreguntaSalir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153), 5));
        panelPrincipalPreguntaSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pdf72px.png"))); // NOI18N
        panelPrincipalPreguntaSalir.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 70));

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 0));
        jLabel2.setText("C:\\Users\\TU_USUARIO\\Mi Biblioteca\\pdf");
        panelPrincipalPreguntaSalir.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 600, 50));

        btnCerrarPDF.setBackground(new java.awt.Color(0, 102, 153));
        btnCerrarPDF.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnCerrarPDF.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cancelar48px.png"))); // NOI18N
        btnCerrarPDF.setText(" Cerrar");
        btnCerrarPDF.setBorderPainted(false);
        btnCerrarPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarPDF.setFocusPainted(false);
        btnCerrarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarPDFActionPerformed(evt);
            }
        });
        panelPrincipalPreguntaSalir.add(btnCerrarPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 220, -1));

        jLabel3.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 0));
        jLabel3.setText("Tu PDF se ha generado en la ruta:");
        panelPrincipalPreguntaSalir.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 590, 50));

        btnVerPDF.setBackground(new java.awt.Color(0, 102, 153));
        btnVerPDF.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnVerPDF.setForeground(new java.awt.Color(255, 255, 255));
        btnVerPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok48px.png"))); // NOI18N
        btnVerPDF.setText(" Ver PDF");
        btnVerPDF.setBorderPainted(false);
        btnVerPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerPDF.setFocusPainted(false);
        btnVerPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerPDFActionPerformed(evt);
            }
        });
        panelPrincipalPreguntaSalir.add(btnVerPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 240, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipalPreguntaSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipalPreguntaSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarPDFActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarPDFActionPerformed

    private void btnVerPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerPDFActionPerformed
        // Define la ruta del PDF que quieres abrir
        String userHome = System.getProperty("user.home");
        String pdfPath = userHome + "/Mi Biblioteca/pdf/listadoLibros.pdf";

        try {
            // Crea un objeto File que apunte al archivo PDF
            File pdfFile = new File(pdfPath);

            // Verifica si el archivo existe
            if (pdfFile.exists()) {
                // Usa Desktop para abrir el archivo PDF
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(pdfFile); // Abre el archivo PDF
                } else {
                    System.out.println("La funcionalidad Desktop no está soportada en este sistema.");
                }
            } else {
                System.out.println("El archivo PDF no existe: " + pdfPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al abrir el PDF: " + e.getMessage());
        }
        this.dispose();
    }//GEN-LAST:event_btnVerPDFActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarPDF;
    private javax.swing.JButton btnVerPDF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel panelPrincipalPreguntaSalir;
    // End of variables declaration//GEN-END:variables
}
