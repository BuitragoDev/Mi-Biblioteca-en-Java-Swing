package GUI;

import javax.swing.JFrame;
import Logica.Funciones;

public class VentanaTodoOK extends javax.swing.JFrame {

    public VentanaTodoOK() {
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelPrincipalPreguntaSalir.setBackground(new java.awt.Color(204, 204, 204));
        panelPrincipalPreguntaSalir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153), 5));
        panelPrincipalPreguntaSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok72px.png"))); // NOI18N
        panelPrincipalPreguntaSalir.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 70));

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 0));
        jLabel2.setText("Enhorabuena! El libro ha sido añadido correctamente.");
        panelPrincipalPreguntaSalir.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 590, 50));

        btnVolverAprincipal.setBackground(new java.awt.Color(0, 102, 153));
        btnVolverAprincipal.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnVolverAprincipal.setForeground(new java.awt.Color(255, 255, 255));
        btnVolverAprincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok48px.png"))); // NOI18N
        btnVolverAprincipal.setText(" Aceptar");
        btnVolverAprincipal.setBorderPainted(false);
        btnVolverAprincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolverAprincipal.setFocusPainted(false);
        btnVolverAprincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverAprincipalActionPerformed(evt);
            }
        });
        panelPrincipalPreguntaSalir.add(btnVolverAprincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, 210, -1));

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

    private void btnVolverAprincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverAprincipalActionPerformed
        //VentanaPrincipal principal = new VentanaPrincipal();
        //principal.mostrarPanelVerTodos();
        //principal.ocultarPanelAgregar();
        //principal.ocultarPanelEditar();
        
        this.dispose();
    }//GEN-LAST:event_btnVolverAprincipalActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVolverAprincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panelPrincipalPreguntaSalir;
    // End of variables declaration//GEN-END:variables
}
