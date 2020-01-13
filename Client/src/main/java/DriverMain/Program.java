package DriverMain;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import Gui.GuiController;

class Program
{   
    private final ProgramManager programManager;
    private final ProgramServerTranslator translator;
    private GuiController frame;
    private final SocketClient socket;
    
    private Program()
    {
        programManager = new ProgramManager(this);
        translator = new ProgramServerTranslator(programManager);
        socket = new SocketClient(translator);
        frame = new GuiController(programManager);
        init();
    }
    
    private synchronized void init()
    {
        programManager.setTranslator(translator);
        translator.setSocket(socket);
        frame.setVisible(true);
        socket.start();
    }

    public SocketClient getSocket()
    {
        return socket;
    }
    
    public GuiController getGUI()
    {
        return frame;
    }
    
    public void reset()
    {
    	frame.dispose();
    	frame = new GuiController(programManager);
    	frame.setVisible(true);
    }
    

    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                    {
                        if ("Nimbus".equals(info.getName()))
                        {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception ignored)
                {
                }
                new Program();
            }
        });
    }
    
}