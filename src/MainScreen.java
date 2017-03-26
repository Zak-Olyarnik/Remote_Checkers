import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;


public class MainScreen extends UI
{
	private static MainScreen singleton;
	private JPanel panel;			// main panel of content
	private JLabel lblTitle;
	private JButton btnGame;		// connects to server and moves to Game screen
	private JButton btnSettings;	// moves to Settings screen
	private JButton btnQuit;		// exits application

	// calls createElements(), placeElements(), and registerEventHandlers()
	private MainScreen()
	{ init(); }

	// implements singleton
	public static MainScreen getMainScreen()
	{
		if(singleton == null)
		{ singleton = new MainScreen(); }
		return singleton;
	}

	@Override
	public void execute()
	{ show(); }
	
	@Override
	public void createElements()
	{
		panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 

		lblTitle = new JLabel("Remote_Checkers");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(TITLE_FONT);

		btnGame = new JButton("Start Game");
		btnGame.setActionCommand("Start Game");
		btnGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnGame.setFont(BUTTON_FONT);
		btnGame.setToolTipText("Connect to Server");
		btnGame.setMinimumSize(buttonSize);
		btnGame.setMaximumSize(buttonSize);
		btnGame.setPreferredSize(buttonSize);

		btnSettings = new JButton("Settings");
		btnSettings.setActionCommand("Settings");
		btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSettings.setFont(BUTTON_FONT);
		btnSettings.setToolTipText("View and Edit Connection Settings");
		btnSettings.setMinimumSize(buttonSize);
		btnSettings.setMaximumSize(buttonSize);
		btnSettings.setPreferredSize(buttonSize);

		btnQuit = new JButton("Quit");
		btnQuit.setActionCommand("Quit");
		btnQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnQuit.setFont(BUTTON_FONT);
		btnQuit.setToolTipText("Exit Remote_Checkers");
		btnQuit.setMinimumSize(buttonSize);
		btnQuit.setMaximumSize(buttonSize);
		btnQuit.setPreferredSize(buttonSize);
	}

	@Override
	public void placeElements()
	{
		panel.add(Box.createRigidArea(new Dimension(0, 50)));
		panel.add(lblTitle);
		panel.add(Box.createRigidArea(new Dimension(0, 120)));
		panel.add(btnGame);
		panel.add(Box.createRigidArea(new Dimension(0, 60)));
		panel.add(btnSettings);
		panel.add(Box.createRigidArea(new Dimension(0, 60)));
		panel.add(btnQuit);
		frame.add(panel);
	}

	@Override
	public void registerEventHandlers()
	{
		btnGame.addActionListener(this);
		btnSettings.addActionListener(this);
		btnQuit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
		{
			case("Start Game"):
				hide();
				Transcription.getTranscription().connect();
				GameScreen.getGameScreen().execute();
				
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
				{
					protected Void doInBackground() throws Exception
					{                
						Transcription.getTranscription().listen();
						return null;
					}

					@Override
					protected void done()
					{
						try
						{ }
						catch (Exception e)
						{ }
					}
				};      
				worker.execute();

				break;
			case("Settings"):
				hide();
				SettingsScreen.getSettingsScreen().execute();
				break;
			case("Quit"):
				System.exit(0);
				break;
		}
	}

	@Override
	public void show()
	{
		frame.add(panel);
        frame.revalidate();
        frame.repaint();
	}

	@Override
	public void hide()
	{ frame.remove(panel); }
}
