package Logica;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class LeftAlignedCellRenderer extends DefaultTableCellRenderer {
    public LeftAlignedCellRenderer() {
        setHorizontalAlignment(SwingConstants.LEFT); // Alineación a la izquierda
    }
}
