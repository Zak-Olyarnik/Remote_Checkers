import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class GameScreen extends UI
{
	private static GameScreen singleton;
	private JPanel panel;				// main panel of content
	private JLabel lblPlayer;			// displays player color
	private JLabel lblMessage;			// displays messages from server
	private JPanel boardPanel;			// GUI representation of the checkerboard
	private Space[][] board;			// local representation of board
	private int size = 8;				// dimensions of board
	private String color;				// player's assigned color

	// calls createElements(), placeElements(), and registerEventHandlers(), then loads images
	private GameScreen()
	{
		init();
		Space.loadImages();
	}

	// implements singleton
	public static GameScreen getGameScreen()
	{
		if(singleton == null)
		{ singleton = new GameScreen(); }
		return singleton;
	}

	@Override
	public void execute()
	{
		setupBoard();
		show();
	}

	// sets player color when received from Server
	public void setColor(String color)
	{
		this.color = color;
		lblPlayer.setText("Welcome, " + color + "!");	
	}
	
	// displays string messages from Server
	public void displayMessage(String message)
	{ lblMessage.setText(message); }

	// redraws only the board spaces that have updated
	public void redraw(Board newBoard)
	{
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{
				int newContents = newBoard.getAt(row, col);
				if(board[row][col].getContents() != newContents)
				{
					board[row][col].setImage(newContents, color);
					board[row][col].revalidate();
					board[row][col].repaint();
				}
			}
		}
	}

	// draws the "new game" board, flipping depending on player color
	public void setupBoard()
	{
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{
				if (row%2 ==col%2)
				{
					if (row<3)
					{ board[row][col].setImage(Space.BLUE_PIECE, color); }
					else if (row>4)
					{ board[row][col].setImage(Space.RED_PIECE, color); }
					else
					{ board[row][col].setImage(Space.BLACK_SPACE, color); }
				}
				else
				{ board[row][col].setImage(Space.WHITE_SPACE, color); }
				if (color.equals("Red"))
				{ boardPanel.add(board[row][col]); }
			}
		}
		if (color.equals("Blue"))
		{
			for (int row=size-1; row>-1; row--)
			{
				for (int col=size-1; col>-1; col--)
				{ boardPanel.add(board[row][col]); }
			}
		}
	}

	// shows popup messages
	public int showPopup(String title, String message)
	{
		int res = JOptionPane.showOptionDialog(frame, message, title, JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		return res;
	}

	@Override
	public void createElements()
	{
		panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		lblPlayer = new JLabel();
		lblPlayer.setFont(BUTTON_FONT);
		lblPlayer.setMinimumSize(messageSize); 
		lblPlayer.setMaximumSize(messageSize); 
		lblPlayer.setPreferredSize(messageSize);
		lblPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblMessage = new JLabel();
		lblMessage.setFont(BUTTON_FONT);
		lblMessage.setMinimumSize(messageSize); 
		lblMessage.setMaximumSize(messageSize); 
		lblMessage.setPreferredSize(messageSize);
		lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);

		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(size, size));

		board = new Space[size][size];
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{ board[row][col] = new Space(row, col); }
		}
	}

	@Override
	public void placeElements()
	{
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.add(lblPlayer);
		messagePanel.add(Box.createRigidArea(new Dimension(25, 15)));
		messagePanel.add(lblMessage);

		JPanel boardHolderPanel = new JPanel();
		boardHolderPanel.setLayout(new BoxLayout(boardHolderPanel, BoxLayout.X_AXIS));
		boardHolderPanel.add(Box.createRigidArea(borderSize));
		boardHolderPanel.add(boardPanel);
		boardHolderPanel.add(Box.createRigidArea(borderSize));

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(25, 15)));
		panel.add(messagePanel);
		panel.add(Box.createRigidArea(new Dimension(35, 25)));
		panel.add(boardHolderPanel);
		panel.add(Box.createRigidArea(borderSize));
	}

	@Override
	public void registerEventHandlers()
	{		
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{ board[row][col].addMouseListener(board[row][col]); }
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{ }

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
