package gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class CellRenderer extends JLabel implements TableCellRenderer {

    public CellRenderer() {
        setBackground(Color.WHITE);
        setOpaque(true);
        setFont(new Font("Arial", Font.BOLD, 49));
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus, int row,
                                                   int column) {       
        if (((Character) value).charValue() == ' ') {
            setText("");
        } else {
            setText(((Character) value).toString());
        }
        return this;
    }
}
