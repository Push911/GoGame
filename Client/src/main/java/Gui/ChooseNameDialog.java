package Gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Exceptions.ComponentException;
import Exceptions.EmptyNameException;
import Exceptions.NameContainsSpaceException;

class ChooseNameDialog extends JDialog implements ActionListener
{
    private final GuiController parent;
    private final JLabel text;
    private final JTextField field;
    private final JPanel panel;
    private final JButton button;

    public ChooseNameDialog(GuiController parent)
    {
    	this.setModal(true);
    	this.setAlwaysOnTop(true);
        text = new JLabel("Please choose your nickname:");
        field = new JTextField();
        panel = new JPanel();
        button = new JButton("OK");
        this.parent = parent;
        this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
        
        initComponents();
    }
    
    private void initComponents()
    {
        this.setBounds(300, 300, 300, 400);
        
        Dimension size= new Dimension(300, 180);
        Dimension gap = new Dimension(300, 15);
        
        this.setSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        panel.setSize(size);  
        panel.setMinimumSize(size); 
        panel.setPreferredSize(size);
        
        this.setTitle("Go - choose your name");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        
        Dimension fieldSize = new Dimension(280, 40);
        field.setSize(fieldSize);
        field.setMinimumSize(fieldSize);
        field.setMaximumSize(fieldSize);
        
        text.setAlignmentX(CENTER_ALIGNMENT);
        button.setAlignmentX(CENTER_ALIGNMENT);
        
        Box box = new Box(BoxLayout.PAGE_AXIS);
        
        button.addActionListener(this);
        
        box.add(Box.createRigidArea(gap));
        box.add(text);
        box.add(Box.createRigidArea(gap));
        box.add(field);
        box.add(Box.createRigidArea(gap));
        box.add(button);
        panel.add(box);
        
        add(panel);
    }
    
    public void setText(String s)
    {
        text.setText(s);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
    	 {
             try {
                 String name = field.getText();

                 try {
    					name = name.trim();
    					parent.getProgramManager().sendChosenName(name);
    					this.setVisible(false);
    				} catch (ComponentException e1) {
    					System.out.println(e1.getMessage());
    				}   
    				
    			} catch (NameContainsSpaceException e2) {
    				this.setVisible(false);
    				field.setText("");
    				parent.displayChooseNameDialog("Wrong name: " + e2.getMessage() + " - name cannot contain space!");
    			
    				
    			} catch (EmptyNameException e2) {
    				this.setVisible(false);
    				
    				parent.displayChooseNameDialog("Name can't be empty!");
    			}
    	        
    	    }
    } 
}