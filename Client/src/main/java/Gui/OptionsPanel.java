package Gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import Exceptions.ComponentException;

@SuppressWarnings("serial")
public class OptionsPanel extends JPanel implements ActionListener
{
    private final GuiController parent;
    
    private JScrollPane scroll;
    private JTextArea messageArea;
    private JTextArea statisticsArea;
    private JButton passButton;
    private JButton surrenderButton;
    private JButton sendProposisionButton;
    private JButton requestResumingButton;
    private JButton acceptProposisionButton;
    private JRadioButton myTerritoryRB;
    private JRadioButton theirTerritoryRB;
    private ButtonGroup radioButtons;
    
    public OptionsPanel(GuiController parent)
    {
        this.parent = parent;
        initComponents();
    }

    private void initComponents()
    {
        Dimension panelSize = new Dimension(300, 300);
        Dimension gap = new Dimension(300, 15);
        Dimension textAreaSize = new Dimension(280, 200);
        Dimension statisticsAreaSize = new Dimension(280, 60);
        Dimension buttonSize = new Dimension(120, 30);
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        setMaximumSize(panelSize);
        setPreferredSize(panelSize);
        setMinimumSize(panelSize);
        
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setVisible(true);
        
        messageArea.setSize(textAreaSize);
        messageArea.setAlignmentX(CENTER_ALIGNMENT);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFocusable(false);
        scroll = new JScrollPane(messageArea);
        scroll.setMaximumSize(textAreaSize);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setFocusable(false);

        statisticsArea = new JTextArea();
        statisticsArea.setEditable(false);
        statisticsArea.setVisible(true);
        statisticsArea.setSize(statisticsAreaSize);
        statisticsArea.setMinimumSize(statisticsAreaSize);
        statisticsArea.setMaximumSize(statisticsAreaSize);
        statisticsArea.setPreferredSize(statisticsAreaSize);
        statisticsArea.setFocusable(false);
        statisticsArea.setAlignmentX(CENTER_ALIGNMENT);
        
        passButton = new JButton("PASS");
        passButton.setSize(buttonSize);
        passButton.setMinimumSize(buttonSize);
        passButton.setMaximumSize(buttonSize);
        passButton.setPreferredSize(buttonSize);
        passButton.addActionListener(this);
        
        surrenderButton = new JButton("SURRENDER");
        surrenderButton.setSize(buttonSize);
        surrenderButton.setMinimumSize(buttonSize);
        surrenderButton.setMaximumSize(buttonSize);
        surrenderButton.setPreferredSize(buttonSize);
        surrenderButton.addActionListener(this);
        
        Box buttonBox = new Box(BoxLayout.LINE_AXIS);
        buttonBox.add(passButton);
        buttonBox.add(Box.createRigidArea(new Dimension(20, 15)));
        buttonBox.add(surrenderButton);

        add(Box.createRigidArea(gap));
        add(scroll);
        add(Box.createRigidArea(gap));
        add(buttonBox);
        add(Box.createRigidArea(gap));
        add(statisticsArea);
        add(Box.createRigidArea(gap));
        
        sendProposisionButton = new JButton("Send proposal");
        acceptProposisionButton = new JButton("Accept proposal");
        requestResumingButton = new JButton("Request resuming the game");
        
        Dimension tbuttonSize = new Dimension(250, 30);
        sendProposisionButton.setSize(tbuttonSize);
        sendProposisionButton.setMinimumSize(tbuttonSize);
        sendProposisionButton.setMaximumSize(tbuttonSize);
        sendProposisionButton.setPreferredSize(tbuttonSize);
        sendProposisionButton.addActionListener(this);
        sendProposisionButton.setEnabled(false);
        
        acceptProposisionButton.setSize(tbuttonSize);
        acceptProposisionButton.setMinimumSize(tbuttonSize);
        acceptProposisionButton.setMaximumSize(tbuttonSize);
        acceptProposisionButton.setPreferredSize(tbuttonSize);
        acceptProposisionButton.addActionListener(this);
        acceptProposisionButton.setEnabled(false);
        
        requestResumingButton.setSize(tbuttonSize);
        requestResumingButton.setMinimumSize(tbuttonSize);
        requestResumingButton.setMaximumSize(tbuttonSize);
        requestResumingButton.setPreferredSize(tbuttonSize);
        requestResumingButton.addActionListener(this);
        requestResumingButton.setEnabled(false);
        
        radioButtons = new ButtonGroup();
        myTerritoryRB = new JRadioButton("my territory", true);
        myTerritoryRB.setEnabled(false);
        myTerritoryRB.setMinimumSize(buttonSize);
        theirTerritoryRB = new JRadioButton("opponent's territory", false);
        theirTerritoryRB.setMinimumSize(buttonSize);
        radioButtons.add(myTerritoryRB);
        radioButtons.add(theirTerritoryRB);
    	
    	theirTerritoryRB.setEnabled(false);
        
        myTerritoryRB.addActionListener(this);
        theirTerritoryRB.addActionListener(this);
        
        Box teritoriesBox = new Box(BoxLayout.PAGE_AXIS);
        teritoriesBox.add(sendProposisionButton);
        teritoriesBox.add(acceptProposisionButton);
        teritoriesBox.add(requestResumingButton);
        teritoriesBox.add(Box.createRigidArea(new Dimension(10, 10)));
        teritoriesBox.add(myTerritoryRB);
        teritoriesBox.add(theirTerritoryRB);
        teritoriesBox.setAlignmentX(CENTER_ALIGNMENT);
        add(teritoriesBox);
    }

    public void displayMessage(String input)
    {
        messageArea.append(input);
    }

    public void activateTeritoriesBox(boolean withRadioButtons)
    {
    	sendProposisionButton.setEnabled(true);
    	acceptProposisionButton.setEnabled(true);
    	requestResumingButton.setEnabled(true);
    	if (withRadioButtons)
    	{
        	myTerritoryRB.setEnabled(true);
        	theirTerritoryRB.setEnabled(true);    		
    	}
    }

    public void disactivateTeritoriesBox(boolean withResume)
    {
    	sendProposisionButton.setEnabled(false);
    	acceptProposisionButton.setEnabled(false);
    	myTerritoryRB.setEnabled(false);
    	theirTerritoryRB.setEnabled(false);
    	if(withResume) requestResumingButton.setEnabled(false);
    }
    
    public void activateButtons()
    {
    	surrenderButton.setEnabled(true);
    	passButton.setEnabled(true);
    }
    
    public void disactivateButtons(boolean withSurrender)
    {
    	if (withSurrender) surrenderButton.setEnabled(false);
    	passButton.setEnabled(false);
    }
    
    @Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(surrenderButton)){
			try {
				parent.getGameManager().sendWhiteFlag();
				parent.manageGameEnd(0, 0, false, true);
			} catch (ComponentException e) { e.printStackTrace(); }
		}
		if(arg0.getSource().equals(passButton)){
			try {
				parent.getGameManager().missTurn();
			} catch (ComponentException e) { e.printStackTrace(); }
		}
		if(arg0.getSource().equals(sendProposisionButton))
		{
			try {
				parent.getGameManager().sendProposition();
			} catch (ComponentException e) { e.printStackTrace(); }
		}
		if(arg0.getSource().equals(acceptProposisionButton))
		{
			try {
				parent.getGameManager().acceptProposition();
			} catch (ComponentException e) { e.printStackTrace(); }
		}
		if(arg0.getSource().equals(myTerritoryRB))
		{
			try {
				if (myTerritoryRB.isSelected()) parent.getGameManager().getDrawingManager().drawingMode = DrawingMode.MYTERRITORY;

			} catch (ComponentException e) { e.printStackTrace(); }
		}
		if(arg0.getSource().equals(theirTerritoryRB))
		{
			try {
				if (theirTerritoryRB.isSelected()) parent.getGameManager().getDrawingManager().drawingMode = DrawingMode.OPPONENTSTERITORY;

			} catch (ComponentException e) { e.printStackTrace(); }
		}
		if(arg0.getSource().equals(requestResumingButton))
		{
			try {
				parent.getGameManager().resumeGame(parent.getGameManager().stoneColor.other());
			} catch (ComponentException e) { e.printStackTrace(); }
		}
	}
    
    public JButton getPassButton(){	return passButton; }
    
    public JButton getSurrenderButton(){ return surrenderButton;  }

	public void displayStatistics(int black, int white) 
	{
		statisticsArea.setText("CAPTURED:\nBLACK: " + String.valueOf(black) + "\nWHITE:" + String.valueOf(white));
	}
}