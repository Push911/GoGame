package Gui;

import Exceptions.ComponentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class PlayerList extends JDialog implements ActionListener
{
	private JList<String> list;
	private final GuiController parent;

	public PlayerList(String playersList, GuiController controller, String title){
		this.parent = controller;
		this.setTitle(title);
		this.setBounds(300, 300, 300, 400);
		
		this.setResizable(false);
		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
        Dimension size= new Dimension(300, 400);
        Dimension insideSize = new Dimension(280, 350);
        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));       
        
        setSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);		

		Box box = new Box(BoxLayout.PAGE_AXIS);
		JButton refreshButton = new JButton("Refresh");
		JButton okButton = new JButton("OK");
		JLabel label = new JLabel("<html>Currently there are no players available.<br>Please try again later.</html>");
				
		if (playersList.trim().equals(""))
		{
		    label.setSize(insideSize);
		    label.setMaximumSize(insideSize);
		    label.setAlignmentX(CENTER_ALIGNMENT);
		    okButton.setEnabled(false);
		    box.add(label);
		}
		else
		{
	        list = new JList<String>(playersList.split(" "));
			JScrollPane listScrollPane = new JScrollPane(list);
	        listScrollPane.setBounds(10, 10, 265,300);
	        list.setSelectionMode(JList.VERTICAL_WRAP);
	        list.setSelectedIndex(0);
	        box.add(listScrollPane);
		}
		
		Dimension gap = new Dimension(300, 15);
		box.add(Box.createRigidArea(gap));
		okButton.setSize(100, 50);
		refreshButton.setSize(100, 50);
		okButton.addActionListener(this);
		refreshButton.addActionListener(this);
		
		Box bottom = new Box(BoxLayout.LINE_AXIS);
		
		bottom.add(okButton);
		bottom.add(Box.createRigidArea(new Dimension(30, 15)));
		bottom.add(refreshButton);
		box.add(bottom);
		box.add(Box.createRigidArea(gap));
		add(box);
		this.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
	        if(ae.getActionCommand().equals("Ok"))
	        {   
	             parent.getProgramManager().chooseOpponent(list.getSelectedValue());
	             this.setVisible(false);
	        }
	        else if(ae.getActionCommand().equals("Refresh"))
	        {
	            parent.getProgramManager().askForList();
	            this.setVisible(false);
	        }		    
		}
		catch(ComponentException e)
		{
		    System.out.println(e.getMessage());
		}
	}
}