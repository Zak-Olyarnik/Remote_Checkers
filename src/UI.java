import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.UIManager;

// Parent class for the three screens
public abstract class UI implements ActionListener
{
	protected static JFrame frame;							// frame is treated as a singleton
	protected static Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 50);
	protected static Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 30);
	protected static Font DIALOG_FONT = new Font("Tahoma", Font.BOLD, 20);
	protected Dimension borderSize = new Dimension(100, 100);
	protected Dimension buttonSize = new Dimension(300, 70);
	protected Dimension labelSize = new Dimension(300, 50);
	protected Dimension messageSize = new Dimension(700, 40);

	public abstract void execute();					// runs initialization logic
	public abstract void createElements();			// initializes each GUI element
	public abstract void placeElements();			// places GUI elements
	public abstract void registerEventHandlers();	// adds click handlers to buttons
	public abstract void show();
	public abstract void hide();

	// doing these things in a constructor created unexpected adverse side effects.	
	protected void init()
	{
		createElements();
		placeElements();
		registerEventHandlers();
	}

	public static void setFrame()		// frame is treated as a singleton
	{
		if(frame == null)
		{
			UIManager.put("OptionPane.messageFont", DIALOG_FONT);
			
			frame = new JFrame("Remote_Checkers");
	    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	frame.setSize(1000, 1000);
	    	frame.setResizable(false);
	    	frame.setVisible(true);
		}
	}
}
