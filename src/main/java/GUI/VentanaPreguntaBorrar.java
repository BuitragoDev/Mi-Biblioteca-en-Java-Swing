package GUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Logica.Conexion;
import Logica.Funciones;

public class VentanaPreguntaBorrar extends javax.swing.JFrame {

    private String sentenciaSQL;
    
    public VentanaPreguntaBorrar(String strSentenciaDelete) {
        // Configurar la ventana sin barra de título y botones
        setUndecorated(true);

        // Maximizar la ventana a pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initComponents();
        this.sentenciaSQL = strSentenciaDelete;
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
        btnAceptarBorrado = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelPrincipalPreguntaSalir.setBackground(new java.awt.Color(204, 204, 204));
        panelPrincipalPreguntaSalir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153), 5));
        panelPrincipalPreguntaSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pregunta72px.png"))); // NOI18N
        panelPrincipalPreguntaSalir.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 70));

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 0, 51));
        jLabel2.setText("¿ Estás seguro de que deseas borrar este libro?");
        panelPrincipalPreguntaSalir.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 550, 50));

        btnVolverAprincipal.setBackground(new java.awt.Color(0, 102, 153));
        btnVolverAprincipal.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnVolverAprincipal.setForeground(new java.awt.Color(255, 255, 255));
        btnVolverAprincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cancelar48px.png"))); // NOI18N
        btnVolverAprincipal.setText(" No, Volver");
        btnVolverAprincipal.setBorderPainted(false);
        btnVolverAprincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolverAprincipal.setFocusPainted(false);
        btnVolverAprincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverAprincipalActionPerformed(evt);
            }
        });
        panelPrincipalPreguntaSalir.add(btnVolverAprincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 250, -1));

        btnAceptarBorrado.setBackground(new java.awt.Color(0, 102, 153));
        btnAceptarBorrado.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnAceptarBorrado.setForeground(new java.awt.Color(255, 255, 255));
        btnAceptarBorrado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok48px.png"))); // NOI18N
        btnAceptarBorrado.setText(" Si, adelante");
        btnAceptarBorrado.setBorderPainted(false);
        btnAceptarBorrado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAceptarBorrado.setFocusPainted(false);
        btnAceptarBorrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarBorradoActionPerformed(evt);
            }
        });
        panelPrincipalPreguntaSalir.add(btnAceptarBorrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 260, -1));

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

    private void btnAceptarBorradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarBorradoActionPerformed
        VentanaPrincipal principal = new VentanaPrincipal();
        Conexion objConexion = new Conexion();
        int respuesta = objConexion.ejecutarSentenciaSQL(sentenciaSQL); // Ejecutar la sentencia SQL

        if (respuesta > 0) {       
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al borrar el registro.");
        }
    }//GEN-LAST:event_btnAceptarBorradoActionPerformed

    private void btnVolverAprincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverAprincipalActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverAprincipalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptarBorrado;
    private javax.swing.JButton btnVolverAprincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panelPrincipalPreguntaSalir;
    // End of variables declaration//GEN-END:variables
}
