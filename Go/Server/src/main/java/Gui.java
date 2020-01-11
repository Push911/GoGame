

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Gui extends JFrame
{
    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
    int lines = 9;
    JFrame frame;
    JTable table;
    JLabel label = new JLabel();
    Gui()
    {
        frame = new JFrame("Go");
        table = new JTable(lines, lines);
        table.setRowHeight(100);
        table.setCellSelectionEnabled(false);
        dtcr.setHorizontalAlignment(label.CENTER);
        for(int i=0; i<lines; i++)
        {
            table.getColumnModel().getColumn(i).setCellRenderer(dtcr);
        }
        table.setFont(new Font("Plain", Font.PLAIN, 40));
        frame.add(table);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
