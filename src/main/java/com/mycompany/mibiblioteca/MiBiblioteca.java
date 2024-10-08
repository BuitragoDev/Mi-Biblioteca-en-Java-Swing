package com.mycompany.mibiblioteca;

import GUI.VentanaPrincipal;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MiBiblioteca {

    public static void main(String[] args) {
        // Establecer el Look and Feel a FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Usa FlatLightLaf() si prefieres un tema claro
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        VentanaPrincipal principal = new VentanaPrincipal();
        principal.setVisible(true);
        principal.setLocationRelativeTo(null);
        principal.setResizable(false);
    }
}
