import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SettingsScreen extends UI
{
	private static SettingsScreen singleton;
	private JPanel panel;			// main panel of content
	private JLabel lblTitle;
	private JLabel lblIP;
	private JTextField txtIP;
	private JButton btnSave;		// saves settings information and moves to MainScreen
	private JButton btnCancel;		// moves to MainScreen

	// calls createElements(), placeElements(), and registerEventHandlers()
	private SettingsScreen()
	{ init(); }

	// implements singleton
	public static SettingsScreen getSettingsScreen()
	{
		if(singleton == null)
		{ singleton = new SettingsScreen(); }
		return singleton;
	}

	@Override
	public void execute()
	{
		readFile();
		show();
	}

	// ensures good input data
	public boolean validateFields()
	{
		String ip = txtIP.getText();

		if(ip.isEmpty())	// field left blank
		{
			JOptionPane.showMessageDialog(frame, "Server IP cannot be left blank!",
					"Update Settings Error", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		return true;
	}
	
	// writes Server connection settings to config.txt file
	public void writeFile()
	{
		try
		{ 
			FileOutputStream outfile = new FileOutputStream(Client.configFile);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outfile));
			out.write(txtIP.getText());
			//out.newLine();
			out.close();
			outfile.close();
		}
		catch (IOException e)
		{ e.printStackTrace(); }
	}

	// reads Server connection settings from config.txt file and populates fields
	public void readFile()
	{
		try
		{
			FileInputStream infile = new FileInputStream(Client.configFile); 
			BufferedReader in = new BufferedReader(new InputStreamReader(infile));
			txtIP.setText(in.readLine());
			in.close();
			infile.close(); 
		}
		catch (IOException e)
		{ e.printStackTrace(); }
	}

	@Override
	public void createElements()
	{
		panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 

		lblTitle = new JLabel("Settings");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(TITLE_FONT);

		lblIP = new JLabel("Server IP Address:");
		lblIP.setFont(BUTTON_FONT);
		lblIP.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblIP.setMinimumSize(labelSize);
		lblIP.setMaximumSize(labelSize);
		lblIP.setPreferredSize(labelSize);
		
		txtIP = new JTextField();
		txtIP.setFont(BUTTON_FONT);
		txtIP.setMargin(new Insets(0,5,0,5));
		txtIP.setMinimumSize(labelSize);
		txtIP.setMaximumSize(labelSize);
		txtIP.setPreferredSize(labelSize);

		btnSave = new JButton("Save");
		btnSave.setActionCommand("Save");
		btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSave.setFont(BUTTON_FONT);
		btnSave.setToolTipText("Save Settings information");
		btnSave.setMinimumSize(buttonSize);
		btnSave.setMaximumSize(buttonSize);
		btnSave.setPreferredSize(buttonSize);

		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCancel.setFont(BUTTON_FONT);
		btnCancel.setToolTipText("Return to Main Screen");
		btnCancel.setMinimumSize(buttonSize);
		btnCancel.setMaximumSize(buttonSize);
		btnCancel.setPreferredSize(buttonSize);
	}

	@Override
	public void placeElements()
	{
		panel.add(Box.createRigidArea(new Dimension(0, 50)));
		panel.add(lblTitle);
		panel.add(Box.createRigidArea(new Dimension(0, 120)));
		panel.add(createSettingsEntryPanel());
		panel.add(Box.createRigidArea(new Dimension(0, 120)));
		panel.add(createButtonsPanel());
		frame.add(panel);
	}

	// creates an intermediate panel to help align components correctly
	public JPanel createSettingsEntryPanel()
	{
		JPanel settingsEntryPanel = new JPanel();
		settingsEntryPanel.setLayout(new BoxLayout(settingsEntryPanel, BoxLayout.X_AXIS));
		
		settingsEntryPanel.add(lblIP);
		settingsEntryPanel.add(txtIP);

		return settingsEntryPanel;
	}

	// creates an intermediate panel to help align components correctly
	public JPanel createButtonsPanel()
	{
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		buttonsPanel.add(btnCancel);
		buttonsPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		buttonsPanel.add(btnSave);

		return buttonsPanel;
	}

	@Override
	public void registerEventHandlers()
	{
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
		{
			case("Save"):
				if (validateFields())
				{
					writeFile();
					hide();
					MainScreen.getMainScreen().execute();
				}
				break;
			case("Cancel"):
				hide();
				MainScreen.getMainScreen().execute();
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
