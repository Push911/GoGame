package Gui;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel
{
    private final GuiController parent;
    private final BoardPanel board;

    public GamePanel(GuiController parent)
    {
        this.parent = parent;
        this.board = new BoardPanel(this.parent);
        initComponents();
    }

    private void initComponents()
    {
        setPreferredSize(new Dimension(500, 500));
        Box box = new Box(BoxLayout.Y_AXIS);

        box.add(Box.createVerticalGlue());
        box.add(this.board);     
        box.add(Box.createVerticalGlue());

        add(box);
    }

    public BoardPanel getBoardPanel() { return this.board; }
}