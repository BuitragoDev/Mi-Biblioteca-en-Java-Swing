package GUI;

import Logica.Funciones;
import javax.swing.JFrame;

public class PreguntaSalir extends javax.swing.JFrame {

    public PreguntaSalir() {
        // Configurar la ventana sin barra de título y botones
        setUndecorated(true);

        // Maximizar la ventana a pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initComponents();
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
        btnVolverAprincipal = new javax.swing.JButton();
        btnAceptarSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelPrincipalPreguntaSalir.setBackground(new java.awt.Color(204, 204, 204));
        panelPrincipalPreguntaSalir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153), 5));
        panelPrincipalPreguntaSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pregunta72px.png"))); // NOI18N
        panelPrincipalPreguntaSalir.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 70));

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 153));
        jLabel2.setText("¿ Estás seguro de que deseas salir de la aplicación?");
        panelPrincipalPreguntaSalir.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 590, 50));

        btnVolverAprincipal.setBackground(new java.awt.Color(0, 102, 153));
        btnVolverAprincipal.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnVolverAprincipal.setForeground(new java.awt.Color(255, 255, 255));
        btnVolverAprincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cancelar48px.png"))); // NOI18N
        btnVolverAprincipal.setText(" Volver");
        btnVolverAprincipal.setBorderPainted(false);
        btnVolverAprincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolverAprincipal.setFocusPainted(false);
        btnVolverAprincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverAprincipalActionPerformed(evt);
            }
        });
        panelPrincipalPreguntaSalir.add(btnVolverAprincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, 210, -1));

        btnAceptarSalir.setBackground(new java.awt.Color(0, 102, 153));
        btnAceptarSalir.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnAceptarSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnAceptarSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok48px.png"))); // NOI18N
        btnAceptarSalir.setText(" Salir");
        btnAceptarSalir.setBorderPainted(false);
        btnAceptarSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAceptarSalir.setFocusPainted(false);
        btnAceptarSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarSalirActionPerformed(evt);
            }
        });
        panelPrincipalPreguntaSalir.add(btnAceptarSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 210, -1));

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

    private void btnAceptarSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnAceptarSalirActionPerformed

    private void btnVolverAprincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverAprincipalActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverAprincipalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptarSalir;
    private javax.swing.JButton btnVolverAprincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panelPrincipalPreguntaSalir;
    // End of variables declaration//GEN-END:variables
}
